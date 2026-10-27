package rafasconquest.dev;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import rafasconquest.data.ConquestSavedData;
import rafasconquest.faction.FactionType;
import rafasconquest.map.BaseMap;
import rafasconquest.map.PoliticalMap;
import rafasconquest.map.PoliticalStyle;
import rafasconquest.net.FactionInfo;
import rafasconquest.poi.PoiType;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Renders a dev world's political map to a PNG, from its saved base map and faction data, with the same
 * {@link PoliticalMap} the game's map screen uses (names and icons approximated). Run with
 * {@code gradlew mapPreview -Pworld=run/<world> -Pout=<png>}.
 */
public final class MapPreview {
    private MapPreview() {
    }

    public static void main(String[] args) throws IOException {
        Path world = Path.of(args[0]);
        Path out = Path.of(args[1]);
        BaseMap base = BaseMap.decode(Files.readAllBytes(world.resolve("data/rafasconquest_basemap.bin")));
        CompoundTag saved = NbtIo.readCompressed(world.resolve("data/rafasconquest_world.dat").toFile());
        ConquestSavedData data = ConquestSavedData.load(saved.getCompound("data"));
        long seed = NbtIo.readCompressed(world.resolve("level.dat").toFile())
                .getCompound("Data").getCompound("WorldGenSettings").getLong("seed");
        List<FactionInfo> factions = FactionInfo.collect(data, BlockPos.ZERO, seed);
        PoliticalMap political = PoliticalMap.build(base, factions);

        int w = base.width();
        int h = base.height();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(0x1E2126));
        g.fillRect(0, 0, w, h);
        BufferedImage layer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        layer.setRGB(0, 0, w, h, political.parchment(), 0, w);
        g.drawImage(layer, 0, 0, null);
        layer.setRGB(0, 0, w, h, political.territory(), 0, w);
        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(layer, 0, 0, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        double bpp = base.blocksPerPixel();
        double cx = -base.minX() / bpp;
        double cz = -base.minZ() / bpp;
        double r = base.stormWallRadius() / bpp;
        g.setColor(new Color(0x6B4FA8));
        g.setStroke(new BasicStroke(1.5F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{4, 4}, 0));
        g.drawOval((int) (cx - r), (int) (cz - r), (int) (2 * r), (int) (2 * r));

        for (int i = 0; i < factions.size(); i++) {
            FactionInfo f = factions.get(i);
            for (PoiType type : new PoiType[]{PoiType.CAPITAL, PoiType.CITY}) {
                for (BlockPos pos : f.structures().getOrDefault(type, List.of())) {
                    int x = (int) ((pos.getX() - base.minX()) / bpp);
                    int z = (int) ((pos.getZ() - base.minZ()) / bpp);
                    g.setColor(new Color(0x1E2126));
                    g.fillRect(x - 4, z - 4, 9, 9);
                    g.setColor(new Color(type == PoiType.CAPITAL ? 0xD9B13B : 0xC8CDD2));
                    g.drawRect(x - 4, z - 4, 8, 8);
                }
            }
            PoliticalStyle.LabelShape shape = political.labels()[i];
            if (shape == null) {
                continue;
            }
            boolean empire = f.type() == FactionType.EMPIRE;
            String name = empire ? f.name().toUpperCase(Locale.ROOT) : f.name();
            Font font = new Font(Font.SERIF, Font.BOLD, empire ? 14 : 9);
            g.setFont(font);
            double lx = (shape.centerX() - base.minX()) / bpp;
            double lz = (shape.centerZ() - base.minZ()) / bpp;
            if (!empire) {
                lx = (f.origin().getX() - base.minX()) / bpp;
                lz = (f.origin().getZ() - base.minZ()) / bpp + 13;
            }
            double angle = empire && shape.elongated() ? Math.max(-Math.PI / 3, Math.min(Math.PI / 3, shape.angle())) : 0;
            int plain = g.getFontMetrics().stringWidth(name);
            double target = empire ? 0.6 * shape.length() / bpp : plain;
            double spacing = name.length() > 1 ? Math.max(0, Math.min(2.5 * plain / name.length(), (target - plain) / (name.length() - 1))) : 0;
            double width = plain + spacing * (name.length() - 1);
            AffineTransform saved2 = g.getTransform();
            g.translate(lx, lz);
            g.rotate(angle);
            double cursor = -width / 2;
            for (int c = 0; c < name.length(); c++) {
                String letter = name.substring(c, c + 1);
                g.setColor(new Color(0xE9DFC6));
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        g.drawString(letter, (float) cursor + dx, 5F + dy);
                    }
                }
                g.setColor(new Color(0x231C14));
                g.drawString(letter, (float) cursor, 5F);
                cursor += g.getFontMetrics().stringWidth(letter) + spacing;
            }
            g.setTransform(saved2);
        }
        g.dispose();
        ImageIO.write(image, "png", out.toFile());

        // Every Farm on the map should belong to whoever holds its province.
        int farms = 0;
        int mismatched = 0;
        java.util.Map<java.util.UUID, Integer> byFaction = new java.util.HashMap<>();
        for (var poi : data.pois()) {
            if (poi.type() != PoiType.FARM) {
                continue;
            }
            var centre = base.provinceAt(poi.pos().getX(), poi.pos().getZ());
            if (centre == null) {
                continue;
            }
            farms++;
            int holder = political.provinceHolders()[base.centres().indexOf(centre)];
            java.util.UUID expected = holder < 0 ? null : factions.get(holder).id();
            if (!java.util.Objects.equals(expected, poi.owner())) {
                mismatched++;
            }
            if (poi.owner() != null) {
                byFaction.merge(poi.owner(), 1, Integer::sum);
            }
        }
        System.out.println("Farms on the map: " + farms + ", owned by someone other than their province's holder: " + mismatched);
        for (FactionInfo f : factions) {
            if (f.type() == FactionType.CITY_STATE && byFaction.containsKey(f.id())) {
                System.out.println("  City-state " + f.name() + " holds " + byFaction.get(f.id()) + " farms");
            }
        }
        System.out.println("Wrote " + out.toAbsolutePath() + ": " + w + " x " + h + ", " + base.centres().size() + " provinces, "
                + factions.size() + " factions");
    }
}
