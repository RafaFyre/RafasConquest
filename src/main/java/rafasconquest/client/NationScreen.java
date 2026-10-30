package rafasconquest.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import org.jetbrains.annotations.Nullable;
import rafasconquest.core.TitleMap;
import rafasconquest.faction.FactionBanner;
import rafasconquest.faction.FactionBannerNbt;
import rafasconquest.map.BaseMap;
import rafasconquest.people.Demographics;
import rafasconquest.people.Homeland;
import rafasconquest.people.Person;
import rafasconquest.people.Race;
import rafasconquest.people.Realm;
import rafasconquest.people.Stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A nation's page, opened from its capital on the world map: its banner at both top corners, its name and people, what
 * it is (its rank, faith, ways, culture and race, with their shares of its people), and its peerage as a pyramid of
 * figures: the ruler at the top, the greatest nobles under them, the lesser ones in wider rows below. A figure opens
 * that character's page; Esc goes back.
 */
public class NationScreen extends Screen {
    private static final int BACKGROUND = 0xFF202226;
    private static final int BOX = 0xFF0E0F10;
    private static final int BOX_EDGE = 0xFF3C3E42;
    private static final int TEXT = 0xFFF0EEE8;
    private static final int MUTED = 0xFFA8A49C;
    private static final int GOLD = 0xFFE0C060;
    private static final int DIAMOND = 0xFF3CC26A;
    private static final int DIAMOND_EDGE = 0xFFE8F8EC;
    private static final int PLINTH = 0xFF4A4B4D;
    private static final int PLINTH_HOVER = 0xFF6A6B6E;
    /** The greatest nobles, in the row under the ruler. */
    private static final int GREAT = 3;
    /** Shares below this are not named. */
    private static final float NAMED_SHARE = 0.03F;

    private final Screen parent;
    private final BaseMap base;
    private final int nation;
    /** The ruler (or -1), then the nobles, most powerful first. */
    private final int ruler;
    private final List<Integer> nobles;
    private final List<Figure> figures = new ArrayList<>();
    @Nullable
    private ModelPart flag;
    private List<com.mojang.datafixers.util.Pair<net.minecraft.core.Holder<net.minecraft.world.level.block.entity.BannerPattern>,
            net.minecraft.world.item.DyeColor>> banner = List.of();
    /** Nobles who found no room on the screen. */
    private int unseen;

    /** A character standing on a plinth: their index, where their feet are, how big they are drawn, and their figure. */
    private record Figure(int person, int x, int feet, int scale, @Nullable AbstractRecruitEntity entity) {
        int halfWidth() {
            return scale * 3 / 4;
        }

        boolean contains(double mx, double my) {
            return mx >= x - halfWidth() && mx <= x + halfWidth() && my >= feet - scale * 2 && my <= feet + scale / 3;
        }
    }

    public NationScreen(Screen parent, BaseMap base, int nation) {
        super(Component.literal(base.nations().get(nation).name()));
        this.parent = parent;
        this.base = base;
        this.nation = nation;
        Realm realm = base.realm();
        this.ruler = realm.ruler(nation);
        List<Integer> found = new ArrayList<>();
        for (int i = 0; i < realm.people().size(); i++) {
            if (i != ruler && realm.people().get(i).nation() == nation) {
                found.add(i);
            }
        }
        found.sort(Comparator.comparingInt((Integer i) -> power(realm, i)).reversed());
        this.nobles = found;
    }

    /** How powerful a noble is: the rank of their hold first, then how many answer to them, then their skills. */
    private static int power(Realm realm, int i) {
        Person p = realm.people().get(i);
        int rank = switch (p.hold().kind()) {
            case CAPITAL -> 4;
            case CITY -> 3;
            case TOWN -> 2;
            case CASTLE -> 1;
        };
        int skills = 0;
        for (Stat s : Stat.values()) {
            skills += s == Stat.PROWESS ? 0 : p.stat(s);
        }
        return rank * 100_000 + realm.vassals(i).size() * 1_000 + skills;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("✕"), b -> onClose()).bounds(width - 26, 6, 20, 20).build());
        if (minecraft.level != null) {
            FactionBanner design = FactionBanner.of(base.nations().get(nation).id());
            banner = BannerBlockEntity.createPatterns(FactionBannerNbt.baseColor(design), FactionBannerNbt.patterns(minecraft.level.registryAccess(), design));
            flag = minecraft.getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
        }
        layOut();
    }

    /**
     * The pyramid, across the lower part of the screen: four levels, each lower one a little behind the one above —
     * the ruler, the greatest nobles, and the lesser ones in two wide rows, the second set between the first's.
     */
    private void layOut() {
        figures.clear();
        int top = (int) (height * 0.30);
        int bottom = height - 8;
        int tall = Math.max(24, (int) ((bottom - top) / 3.4));
        int scale = Math.max(10, tall * 10 / 19);
        int step = (bottom - top - tall - tall / 5) / 3;
        int feet = top + tall;
        int centre = width / 2;
        int spacing = scale * 2;
        if (ruler >= 0) {
            figures.add(figure(ruler, centre, feet, scale));
        }
        int next = 0;
        int great = Math.min(GREAT, nobles.size());
        for (int i = 0; i < great; i++) {
            // The middle one just under the ruler; the others to either side, a little higher.
            int x = great == 1 ? centre : centre + (i == 0 ? 0 : i == 1 ? -spacing * 3 / 2 : spacing * 3 / 2);
            figures.add(figure(nobles.get(next++), x, feet + step - (i == 0 ? 0 : step / 3), scale));
        }
        int perRow = Math.max(1, (width - 40) / spacing);
        for (int row = 0; row < 2 && next < nobles.size(); row++) {
            int count = Math.min(nobles.size() - next, row == 0 ? perRow : perRow - 1);
            int rowWidth = (count - 1) * spacing;
            int y = feet + step * (row + 2);
            for (int i = 0; i < count; i++) {
                figures.add(figure(nobles.get(next++), centre - rowWidth / 2 + i * spacing, y, scale));
            }
        }
        unseen = nobles.size() - next;
    }

    /** A figure for a character, of their culture's land (the Recruits skin for its villagers). */
    private Figure figure(int person, int x, int feet, int scale) {
        AbstractRecruitEntity entity = null;
        if (minecraft.level != null) {
            entity = ModEntityTypes.RECRUIT.get().create(minecraft.level);
            if (entity != null) {
                Person p = base.realm().people().get(person);
                Homeland land = base.realm().cultures().get(p.culture()).homeland();
                int[] skins = switch (land) {
                    case DESERT, JUNGLE, SAVANNA -> new int[]{15, 19};
                    case SNOW -> new int[]{5, 10};
                    case SWAMP, TAIGA -> new int[]{5, 14};
                    case PLAINS -> new int[]{0, 14};
                };
                entity.setBiome((byte) land.ordinal());
                entity.setVariant(skins[0] + Math.floorMod(p.name().hashCode(), skins[1] - skins[0]));
            }
        }
        return new Figure(person, x, feet, scale, entity);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, BACKGROUND);
        Realm realm = base.realm();
        BaseMap.Nation n = base.nations().get(nation);
        Demographics people = realm.demographics(nation);

        // The banners, at both top corners.
        int bannerWidth = Math.max(30, Math.min(64, (int) (width * 0.075)));
        int bannerHeight = Math.min((int) (height * 0.4), bannerWidth * 27 / 10);
        int leftBanner = (int) (width * 0.07);
        drawBanner(g, leftBanner, 8, bannerWidth, bannerHeight);
        drawBanner(g, width - leftBanner - bannerWidth, 8, bannerWidth, bannerHeight);

        // The title, with the nation's people beside it.
        int boxX = leftBanner + bannerWidth + 4;
        String title = title(n);
        String population = "Population " + String.format("%,d", people.population());
        int titleWidth = font.width(title) * 3 / 2;
        int titleBoxWidth = titleWidth + font.width(population) + 30;
        g.fill(boxX, 14, boxX + titleBoxWidth, 38, BOX);
        g.renderOutline(boxX, 14, titleBoxWidth, 24, BOX_EDGE);
        g.pose().pushPose();
        g.pose().translate(boxX + 10, 20, 0);
        g.pose().scale(1.5F, 1.5F, 1);
        g.drawString(font, title, 0, 0, TEXT, false);
        g.pose().popPose();
        g.drawString(font, population, boxX + 20 + titleWidth, 23, MUTED, false);

        // What the nation is.
        List<String[]> bullets = bullets(realm, n, people);
        int listWidth = 0;
        for (String[] b : bullets) {
            listWidth = Math.max(listWidth, Math.max(font.width(b[0]) + 30, b.length > 1 ? font.width(b[1]) + 30 : 0));
        }
        int listHeight = 8;
        for (String[] b : bullets) {
            listHeight += b.length > 1 ? 22 : 13;
        }
        int listTop = 42;
        g.fill(boxX, listTop, boxX + listWidth, listTop + listHeight, BOX);
        g.renderOutline(boxX, listTop, listWidth, listHeight, BOX_EDGE);
        int y = listTop + 6;
        for (String[] b : bullets) {
            diamond(g, boxX + 10, y + 4);
            g.drawString(font, b[0], boxX + 20, y, TEXT, false);
            y += 11;
            if (b.length > 1) {
                g.drawString(font, b[1], boxX + 20, y, MUTED, false);
                y += 11;
            }
            y += 2;
        }

        // The peerage.
        Figure hovered = null;
        for (Figure f : figures) {
            if (f.contains(mouseX, mouseY)) {
                hovered = f;
            }
        }
        for (Figure f : figures) {
            int half = f.halfWidth();
            g.fill(f.x() - half, f.feet() - f.scale() / 3, f.x() + half, f.feet() + f.scale() / 3, f == hovered ? PLINTH_HOVER : PLINTH);
            if (f.entity() != null) {
                InventoryScreen.renderEntityInInventoryFollowsMouse(g, f.x(), f.feet(), f.scale(), f.x() - mouseX,
                        f.feet() - f.scale() * 1.6F - mouseY, f.entity());
            }
        }
        if (ruler < 0) {
            g.drawCenteredString(font, "No ruler", width / 2, (int) (height * 0.4), MUTED);
        }
        if (unseen > 0) {
            String more = "and " + unseen + " more noble" + (unseen == 1 ? "" : "s");
            g.drawString(font, more, width - font.width(more) - 8, height - 12, MUTED, false);
        }
        super.render(g, mouseX, mouseY, partialTick);
        if (hovered != null) {
            Person p = realm.people().get(hovered.person());
            g.renderComponentTooltip(font, List.of(Component.literal(CharacterScreen.styled(base, p)),
                    Component.literal("Holds " + base.names().get(p.hold().province()) + " (click)").withStyle(net.minecraft.ChatFormatting.GRAY)),
                    mouseX, mouseY);
        }
    }

    /** "THE EMPIRE OF ULDARESH": the nation's rank and its name, or a sanctum's own name. */
    private static String title(BaseMap.Nation n) {
        if (n.sanctum()) {
            return "THE " + n.name().toUpperCase(java.util.Locale.ROOT);
        }
        String name = n.name().endsWith(" Empire") ? n.name().substring(0, n.name().length() - " Empire".length()) : n.name();
        return ("The " + rank(n) + " of " + name).toUpperCase(java.util.Locale.ROOT);
    }

    /** What kind of nation it is by the title it holds. */
    static String rank(BaseMap.Nation n) {
        if (n.sanctum()) {
            return "Sword Sanctum";
        }
        return switch (n.tier()) {
            case EMPIRE -> "Empire";
            case KINGDOM -> "Kingdom";
            case DUCHY -> "Duchy";
            case COUNTY -> "City-state";
        };
    }

    /** The lines beside the diamonds: each a line, and maybe a quieter one under it. */
    private List<String[]> bullets(Realm realm, BaseMap.Nation n, Demographics people) {
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{rank(n)});
        if (nation < realm.nationFaith().length) {
            String faith = realm.faiths().get(realm.nationFaith()[nation]).name();
            lines.add(new String[]{"Followers of " + (faith.contains(" ") ? "the " : "") + faith, piety(realm.nationPiety()[nation])});
        }
        if (ruler >= 0) {
            lines.add(new String[]{ways(realm.people().get(ruler))});
        }
        if (nation < realm.nationCulture().length) {
            lines.add(new String[]{realm.cultures().get(realm.nationCulture()[nation]).name(),
                    "Cultures: " + shares(people.cultures(), h -> realm.cultures().get(h).name())});
        }
        Race race = Race.values()[Demographics.largest(people.races())];
        lines.add(new String[]{"Mostly " + race.title(), "Races: " + shares(people.races(), h -> Race.values()[h].title())});
        return lines;
    }

    /** "Uldareshi 72%, Frostish 20%, others 8%". */
    private static String shares(float[] shares, java.util.function.IntFunction<String> name) {
        List<Demographics.Share> named = Demographics.largest(shares, NAMED_SHARE);
        float rest = 1;
        List<String> parts = new ArrayList<>();
        for (Demographics.Share s : named.subList(0, Math.min(3, named.size()))) {
            parts.add(name.apply(s.index()) + " " + Math.round(s.share() * 100) + "%");
            rest -= s.share();
        }
        if (Math.round(rest * 100) > 0) {
            parts.add("others " + Math.round(rest * 100) + "%");
        }
        return parts.stream().collect(Collectors.joining(", "));
    }

    private static String piety(float piety) {
        return piety < 0.2F ? "Worldly" : piety < 0.4F ? "Lax in their faith" : piety < 0.6F ? "Observant" : piety < 0.8F ? "Pious" : "Devout";
    }

    /** The nation's ways, from what its ruler is best at. */
    private static String ways(Person ruler) {
        Stat best = Stat.DIPLOMACY;
        for (Stat s : Stat.values()) {
            if (s != Stat.PROWESS && ruler.stat(s) > ruler.stat(best)) {
                best = s;
            }
        }
        return switch (best) {
            case DIPLOMACY -> "Nation of Accords";
            case MARTIAL -> "Nation of Swords";
            case STEWARDSHIP -> "Nation of Laws";
            case INTRIGUE -> "Nation of Whispers";
            default -> "Nation of Scholars";
        };
    }

    /** A green diamond bullet centred on (x, y). */
    private static void diamond(GuiGraphics g, int x, int y) {
        for (int d = 0; d <= 4; d++) {
            g.fill(x - (4 - d), y - d, x + (4 - d) + 1, y - d + 1, DIAMOND_EDGE);
            g.fill(x - (4 - d), y + d, x + (4 - d) + 1, y + d + 1, DIAMOND_EDGE);
        }
        for (int d = 0; d <= 3; d++) {
            g.fill(x - (3 - d), y - d, x + (3 - d) + 1, y - d + 1, DIAMOND);
            g.fill(x - (3 - d), y + d, x + (3 - d) + 1, y + d + 1, DIAMOND);
        }
    }

    /** The nation's banner, filling the box at (x, y), as a loom shows one. */
    private void drawBanner(GuiGraphics g, int x, int y, int w, int h) {
        if (flag == null) {
            g.fill(x, y, x + w, y + h, 0xFFB01010);
            return;
        }
        Lighting.setupForFlatItems();
        PoseStack pose = g.pose();
        pose.pushPose();
        // The flag is 20 by 40 model pixels, its top edge at the origin; stretched to fill the box.
        pose.translate(x + w / 2F, y, 100);
        pose.scale(w / 1.25F, h / 2.5F, -1);
        flag.xRot = 0;
        flag.y = 0;
        BannerRenderer.renderPatterns(pose, g.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, flag, ModelBakery.BANNER_BASE,
                true, banner);
        pose.popPose();
        g.flush();
        Lighting.setupFor3DItems();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (int i = figures.size() - 1; i >= 0; i--) { // the nearer rows first, as they stand in front
                Figure f = figures.get(i);
                if (f.contains(mouseX, mouseY)) {
                    minecraft.setScreen(new CharacterScreen(this, base, f.person()));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
