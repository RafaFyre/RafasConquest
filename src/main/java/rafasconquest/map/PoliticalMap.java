package rafasconquest.map;

import rafasconquest.core.OwnershipResolver;
import rafasconquest.core.ProvinceMap;
import rafasconquest.core.TerritoryMap;
import rafasconquest.faction.FactionBanner;
import rafasconquest.net.FactionInfo;

import java.util.List;
import java.util.UUID;

/**
 * The political map, worked out from the base map and the factions: who holds each province (whoever holds its centre:
 * the City-state round it, else the Empire that would own a Farm there), each realm's colour, the two image layers,
 * and where each realm's name goes. Indexes follow {@code factions}. The game's map screen and the test previews both
 * draw from this.
 */
public record PoliticalMap(int[] parchment, int[] territory, int[] realmColors, PoliticalStyle.LabelShape[] labels) {
    public static PoliticalMap build(BaseMap base, List<FactionInfo> factions) {
        List<TerritoryMap.Claim> claims = factions.stream().map(FactionInfo::claim).toList();
        List<OwnershipResolver.Candidate> empires = TerritoryMap.empires(claims);
        List<UUID> order = factions.stream().map(FactionInfo::id).toList();
        int[] provinceHolder = new int[base.centres().size()];
        for (int p = 0; p < provinceHolder.length; p++) {
            ProvinceMap.Centre c = base.centres().get(p);
            provinceHolder[p] = TerritoryMap.holder(c.x(), c.z(), claims, empires).map(order::indexOf).orElse(-1);
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
        return new PoliticalMap(PoliticalStyle.parchment(base), PoliticalStyle.territory(base, holders, colors), colors,
                PoliticalStyle.labelShapes(base, holders, factions.size()));
    }
}
