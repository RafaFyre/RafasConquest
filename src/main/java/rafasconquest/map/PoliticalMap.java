package rafasconquest.map;

import rafasconquest.faction.FactionBanner;
import rafasconquest.net.FactionInfo;

import java.util.List;
import java.util.UUID;

/**
 * The political map, worked out from the base map and the factions: who holds each province (the nation that took up
 * its title on the base map, if it is among {@code factions}; a province's Farms belong to the same), each realm's
 * colour, the two image layers, and where each realm's name goes. Indexes follow {@code factions}. The game's map
 * screen and the test previews both draw from this.
 */
public record PoliticalMap(int[] provinceHolders, int[] parchment, int[] territory, int[] realmColors, PoliticalStyle.LabelShape[] labels) {
    public static PoliticalMap build(BaseMap base, List<FactionInfo> factions) {
        List<UUID> order = factions.stream().map(FactionInfo::id).toList();
        int[] provinceHolder = new int[base.centres().size()];
        for (int p = 0; p < provinceHolder.length; p++) {
            BaseMap.Nation nation = base.holderOf(p);
            provinceHolder[p] = nation == null ? -1 : order.indexOf(nation.id());
        }
        int[] holders = PoliticalStyle.holders(base, provinceHolder);

        int[] bannerRgb = new int[factions.size()];
        long[] ids = new long[factions.size()];
        for (int i = 0; i < factions.size(); i++) {
            UUID id = factions.get(i).id();
            bannerRgb[i] = PoliticalStyle.DYE_RGB[FactionBanner.of(id).base()];
            ids[i] = id.getMostSignificantBits() ^ id.getLeastSignificantBits();
        }
        int[] colors = PoliticalStyle.realmColors(bannerRgb, ids, PoliticalStyle.neighbours(holders, base.width(), base.height()));
        return new PoliticalMap(provinceHolder, PoliticalStyle.parchment(base), PoliticalStyle.territory(base, holders, colors), colors,
                PoliticalStyle.labelShapes(base, holders, factions.size()));
    }
}
