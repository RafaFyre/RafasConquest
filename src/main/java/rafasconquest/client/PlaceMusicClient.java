package rafasconquest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import org.jetbrains.annotations.Nullable;
import rafasconquest.PlaceMusic;
import rafasconquest.RafasConquest;

/**
 * Plays the music the server says fits where the player stands ({@link PlaceMusic}), on the music slider, looping;
 * the game's own music is kept quiet meanwhile. Leaving the place (or going to another) fades it out over
 * {@link #FADE_TICKS}; the next place's music starts once it has.
 */
@EventBusSubscriber(modid = RafasConquest.MOD_ID, value = Dist.CLIENT)
public final class PlaceMusicClient {
    private static final int FADE_TICKS = 60;
    /** Played on the client only, so never registered. */
    private static final SoundEvent SHORES = event("music.to_shores_distant");
    private static final SoundEvent WAR = event("music.dancing_steps_of_war");
    /** A city's ambient sound, on the ambient slider, heard only where no place music plays. */
    private static final SoundEvent CITY_AMBIENT = event("ambient.city_market");
    private static int place = PlaceMusic.NONE;
    private static boolean city;
    private static Track playing;
    private static Track ambient;

    private PlaceMusicClient() {
    }

    public static void set(int music, boolean inCity) {
        place = music;
        city = inCity;
    }

    private static SoundEvent event(String name) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(RafasConquest.MOD_ID, name));
    }

    /** The ambient sound's volume in a city's streets for the sky light at the player's eyes: half outdoors, muffled indoors. */
    static float ambientVolume(int skyLight) {
        float open = Mth.clamp((skyLight - 8) / 6f, 0, 1); // 14+ open sky, 8 or less well indoors
        return Mth.lerp(open, 0.12f, 0.5f);
    }

    private static final class Track extends AbstractTickableSoundInstance {
        final int place;
        boolean fading;
        /** The volume it eases toward while not fading out. */
        float target = 1;

        Track(SoundEvent event, SoundSource source, int place, float start) {
            super(event, source, RandomSource.create());
            this.place = place;
            this.volume = start;
            this.looping = true;
            this.relative = true;
            this.attenuation = SoundInstance.Attenuation.NONE;
        }

        @Override
        public void tick() {
            if (fading) {
                volume -= 1f / FADE_TICKS;
                if (volume <= 0) {
                    stop();
                }
            } else if (volume != target) {
                float step = 1f / FADE_TICKS;
                volume = volume < target ? Math.min(target, volume + step) : Math.max(target, volume - step);
            }
        }

        /** The ambient sound starts at volume 0 and fades in; without this the game would skip it. */
        @Override
        public boolean canStartSilent() {
            return true;
        }
    }

    /** Fired every client tick by the game's music manager. */
    @SubscribeEvent
    public static void onSelectMusic(SelectMusicEvent event) {
        Minecraft mc = Minecraft.getInstance();
        int want = mc.player == null ? PlaceMusic.NONE : place;
        playing = keep(mc, playing, want, want == PlaceMusic.WAR ? WAR : SHORES, SoundSource.MUSIC, 1);
        // A place's own music (docks, sanctum, post...) drowns the ambient sound out: it fades while that plays.
        ambient = keep(mc, ambient, mc.player != null && city && want == PlaceMusic.NONE ? 1 : PlaceMusic.NONE, CITY_AMBIENT, SoundSource.AMBIENT, 0);
        if (ambient != null && mc.player != null && mc.level != null) {
            // shortcut: indoors is only quieter, no low-pass; that needs OpenAL EFX filters on the sound's channel.
            ambient.target = ambientVolume(mc.level.getBrightness(LightLayer.SKY, BlockPos.containing(mc.player.getEyePosition())));
        }
        if (playing != null) {
            event.setMusic(null);
        }
    }

    /** The track to play for {@code want} (NONE for none), from the one playing: kept, faded, or started anew. */
    @Nullable
    private static Track keep(Minecraft mc, @Nullable Track track, int want, SoundEvent event, SoundSource source, float start) {
        if (track != null && (track.isStopped() || !mc.getSoundManager().isActive(track))) {
            track = null;
        }
        if (track != null) {
            // Stepping back in before it has faded brings it back up.
            track.fading = track.place != want;
        }
        // Never start one with its slider at 0: the game kills it at once, and a stream restarted every tick that way
        // can slip past the stop and hold one of the 8 streaming slots for good, until none are left.
        if (track == null && want != PlaceMusic.NONE && mc.options.getSoundSourceVolume(source) > 0) {
            track = new Track(event, source, want, start);
            mc.getSoundManager().play(track);
        }
        return track;
    }
}
