package dev.elektronisch.nbslib.player;

import dev.elektronisch.nbslib.NBSLibPlugin;
import dev.elektronisch.nbslib.song.Layer;
import dev.elektronisch.nbslib.song.Note;
import dev.elektronisch.nbslib.song.PositionedSongPlayerEventAdapter;
import dev.elektronisch.nbslib.song.Song;
import dev.elektronisch.nbslib.util.NoteUtil;
import dev.elektronisch.nbslib.util.PitchUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PositionedSongPlayer extends AbstractSongPlayer<PositionedSongPlayerEventAdapter> {

    private static final NBSLibPlugin PLUGIN = Bukkit.getServicesManager().load(NBSLibPlugin.class);
    private static final float DISTANCE_MULTIPLIER = 1F / 16F;

    private Location targetLocation;
    private int distance = 16, distanceSquared = distance * distance;
    private final Set<UUID> playersInRange = ConcurrentHashMap.newKeySet();

    public PositionedSongPlayer(final Location targetLocation, final Song... songs) {
        super(songs);
        this.targetLocation = targetLocation;
    }

    @Override
    public void handleTick(final Player player) {
        if (!playersInRange.contains(player.getUniqueId())) return;

        for (final Layer layer : currentSong.getLayerMap().values()) {
            final Note note = layer.getNoteMap().get(currentTick);
            if (note == null) continue;

            final float relativeVolume = ((layer.getVolume() * volume * note.getVelocity()) / 1_000_000F) * distance * DISTANCE_MULTIPLIER;
            final float pitch = PitchUtil.getPitch(note);

            player.playSound(targetLocation, NoteUtil.getInstrument(note.getInstrument()), soundCategory, relativeVolume, pitch);
        }
    }

    @Override
    public void play() {
        super.play();
        PLUGIN.getPositionedSongPlayers().add(this);
    }

    @Override
    public void pause() {
        super.pause();
        PLUGIN.getPositionedSongPlayers().remove(this);
    }

    public void playerEnteredRange(final Player player) {
        playersInRange.add(player.getUniqueId());
        eventAdapters.forEach(adapter -> adapter.onPlayerEnteredRange(player));
    }

    public void playerLeftRange(final Player player) {
        playersInRange.remove(player.getUniqueId());
        eventAdapters.forEach(adapter -> adapter.onPlayerLeftRange(player));
    }

    public Location getTargetLocation() {
        return targetLocation;
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
}
