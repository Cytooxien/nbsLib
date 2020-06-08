package dev.elektronisch.nbslib.player;

import dev.elektronisch.nbslib.song.Layer;
import dev.elektronisch.nbslib.song.Note;
import dev.elektronisch.nbslib.song.Song;
import dev.elektronisch.nbslib.util.NoteUtil;
import dev.elektronisch.nbslib.util.PitchUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class PositionedSongPlayer extends AbstractSongPlayer {

    private static final float DISTANCE_MULTIPLIER = 1F / 16F;

    private Location targetLocation;
    private int distance = 16, distanceSquared = distance * distance;

    public PositionedSongPlayer(final Location targetLocation, final Song... songs) {
        super(songs);
        this.targetLocation = targetLocation;
    }

    @Override
    public void handleTick(final Player player) {
        if (isInRange(player)) {
            for (final Layer layer : currentSong.getLayerMap().values()) {
                final Note note = layer.getNoteMap().get(currentTick);
                if (note == null) continue;

                final float relativeVolume = ((layer.getVolume() * volume * note.getVelocity()) / 1_000_000F) * distance * DISTANCE_MULTIPLIER;
                final float pitch = PitchUtil.getPitch(note);

                player.playSound(targetLocation, NoteUtil.getInstrument(note.getInstrument()), soundCategory, relativeVolume, pitch);
            }
        }
    }

    public void setTargetLocation(final Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
        this.distanceSquared = distance * distance;
    }

    public int getDistanceSquared() {
        return distanceSquared;
    }

    public boolean isInRange(final Player player) {
        return Objects.equals(player.getLocation().getWorld(), targetLocation.getWorld()) && player.getLocation().distanceSquared(targetLocation) <= distanceSquared;
    }

}
