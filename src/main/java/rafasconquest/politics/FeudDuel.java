package rafasconquest.politics;

import com.talhanation.recruits.entities.AbstractRecruitEntity;
import com.talhanation.recruits.init.ModEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;
import rafasconquest.map.BaseMap;
import rafasconquest.worldgen.ConquestRuntime;

import java.util.List;
import java.util.Random;

/**
 * A feud's duel fought for real, where players can watch: the two champions stand as soldiers in their nation's best
 * harness, their health and blows by their prowess, and are set on each other. A champion who yields stops when badly
 * hurt; one who won't (see {@link Feuds#toTheDeath}) fights on until one of them falls. A duel that drags on with no
 * blow struck, or runs too long, goes to the less hurt. One duel at a time.
 */
final class FeudDuel {
    /** A champion who yields does so below this share of their health. */
    static final float YIELD_AT = 0.3f;
    static final int LONGEST = 3 * 60 * 20;
    /** No blow struck in this long: decided by strength. */
    static final int STALL = 60 * 20;

    @Nullable
    private static FeudDuel current;

    private final long feud;
    private final AbstractRecruitEntity a;
    private final AbstractRecruitEntity b;
    private final boolean deathA;
    private final boolean deathB;
    private int ticks;
    private int lastBlow;
    private float healthA;
    private float healthB;

    private FeudDuel(long feud, AbstractRecruitEntity a, AbstractRecruitEntity b, boolean deathA, boolean deathB) {
        this.feud = feud;
        this.a = a;
        this.b = b;
        this.deathA = deathA;
        this.deathB = deathB;
        this.healthA = a.getHealth();
        this.healthB = b.getHealth();
    }

    /** Sets the champions on the field; false if they can't be (another duel is on). */
    static boolean start(MinecraftServer server, BaseMap map, Feud feud) {
        Feud.Clash c = feud.clash();
        if (current != null || c == null) {
            return false;
        }
        ServerLevel level = server.overworld();
        MapLands lands = MapLands.of(map);
        Politics.Front front = Wars.front(server, map);
        AbstractRecruitEntity a = champion(level, map, lands, front, feud, c, true);
        AbstractRecruitEntity b = champion(level, map, lands, front, feud, c, false);
        if (a == null || b == null) {
            for (Entity e : new Entity[]{a, b}) {
                if (e != null) {
                    e.discard();
                }
            }
            return false;
        }
        current = new FeudDuel(feud.id(), a, b, c.toTheDeath(true), c.toTheDeath(false));
        server.getPlayerList().broadcastSystemMessage(Component.literal(Wars.say("feud.duel", "a", a.getName().getString(), "b", b.getName().getString(),
                "lord_a", Feuds.name(feud.lord(true), lands, front), "lord_b", Feuds.name(feud.lord(false), lands, front),
                "x", Math.round(c.x()), "z", Math.round(c.z()))).withStyle(ChatFormatting.RED), false);
        return true;
    }

    @Nullable
    private static AbstractRecruitEntity champion(ServerLevel level, BaseMap map, MapLands lands, Politics.Front front, Feud feud,
                                                  Feud.Clash c, boolean sideA) {
        AbstractRecruitEntity e = ModEntityTypes.RECRUIT.get().create(level);
        if (e == null) {
            return null;
        }
        int x = (int) Math.round(c.x() + (sideA ? -3 : 3));
        int z = (int) Math.round(c.z());
        int y = level.getChunk(x >> 4, z >> 4).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x & 15, z & 15) + 1;
        e.moveTo(x + 0.5, y, z + 0.5, sideA ? -90 : 90, 0);
        e.finalizeSpawn(level, level.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.EVENT, null);
        rafasconquest.gear.Armoury.dressTier(e, 3, map, map.nations().get(feud.nation(sideA)).id());
        int champion = c.champion(sideA);
        int prowess = Feuds.prowess(champion, lands);
        e.setCustomName(Component.literal(champion >= 0 ? lands.person(champion).name()
                : Feuds.name(feud.lord(sideA), lands, front) + "'s sworn sword"));
        e.setCustomNameVisible(true);
        AttributeInstance health = e.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(20 + prowess);
        }
        AttributeInstance damage = e.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(damage.getBaseValue() + prowess / 10.0);
        }
        e.setHealth(e.getMaxHealth());
        e.setHunger(100);
        e.setMoral(100);
        level.addFreshEntity(e);
        return e;
    }

    /** Every tick: keeps them at each other, and ends the duel when one falls, yields, or it drags on. */
    static void tick(MinecraftServer server) {
        FeudDuel d = current;
        if (d == null) {
            return;
        }
        d.ticks++;
        boolean aGone = d.a.isRemoved() && d.a.getRemovalReason() != Entity.RemovalReason.KILLED;
        boolean bGone = d.b.isRemoved() && d.b.getRemovalReason() != Entity.RemovalReason.KILLED;
        if (aGone || bGone) {
            // shortcut: unloaded with everyone gone (or cleared by hand): fought again by strength; a champion left in an unloaded chunk stays there.
            current = null;
            d.a.discard();
            d.b.discard();
            PoliticsData.get(server).politics.realClashCancelled(d.feud);
            return;
        }
        boolean aDown = d.a.isDeadOrDying();
        boolean bDown = d.b.isDeadOrDying();
        if (aDown || bDown) {
            d.end(server, bDown && !aDown || aDown && bDown && d.a.getHealth() >= d.b.getHealth(), true);
            return;
        }
        if (d.a.getTarget() != d.b) {
            d.a.setTarget(d.b);
        }
        if (d.b.getTarget() != d.a) {
            d.b.setTarget(d.a);
        }
        if (d.a.getHealth() < d.healthA || d.b.getHealth() < d.healthB) {
            d.lastBlow = d.ticks;
        }
        d.healthA = d.a.getHealth();
        d.healthB = d.b.getHealth();
        float fa = d.a.getHealth() / d.a.getMaxHealth();
        float fb = d.b.getHealth() / d.b.getMaxHealth();
        if (!d.deathA && fa < YIELD_AT) {
            d.end(server, false, false);
        } else if (!d.deathB && fb < YIELD_AT) {
            d.end(server, true, false);
        } else if (d.ticks >= LONGEST || d.ticks - d.lastBlow >= STALL) {
            boolean aWon = fa == fb ? new Random(d.feud).nextBoolean() : fa > fb;
            d.end(server, aWon, false);
        }
    }

    private void end(MinecraftServer server, boolean aWon, boolean loserDied) {
        current = null;
        for (AbstractRecruitEntity e : List.of(a, b)) {
            if (e.isAlive()) {
                e.setTarget(null);
                e.discard();
            }
        }
        BaseMap map = ConquestRuntime.map();
        PoliticsData data = PoliticsData.get(server);
        if (map == null) {
            data.politics.realClashCancelled(feud);
            return;
        }
        int before = data.politics.chronicle().size();
        data.politics.realDuel(feud, aWon, loserDied, Wars.now(server), MapLands.of(map), new Random(server.overworld().getGameTime()),
                Wars.front(server, map));
        List<Politics.Event> news = data.politics.chronicle();
        for (int i = before; i < news.size(); i++) {
            server.getPlayerList().broadcastSystemMessage(Component.literal(Wars.say("feud.news", "text", news.get(i).text())).withStyle(ChatFormatting.GOLD), false);
        }
        data.setDirty();
        PoliticsSync.toAll(server);
    }
}
