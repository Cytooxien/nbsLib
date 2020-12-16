package dev.elektronisch.nbslib.player;

import dev.elektronisch.nbslib.song.*;
import dev.elektronisch.nbslib.util.InstrumentUtil;
import dev.elektronisch.nbslib.util.PitchUtil;
import org.bukkit.entity.Player;

public final class SimpleSongPlayer extends AbstractSongPlayer<SongPlayerEventAdapter> {

    public SimpleSongPlayer(final Song... songs) {
        super(songs);
    }

    public SimpleSongPlayer(final SongSelectionMode selectionMode, final Song... songs) {
        super(selectionMode, songs);
    }

    @Override
    public void handleTick(final Player player) {
        for (final Layer layer : currentSong.getLayerMap().values()) {
            final Note note = layer.getNoteMap().get(currentTick);
            if (note == null) continue;

            final float relativeVolume = (layer.getVolume() * volume * note.getVelocity()) / 1_000_000F;
            final float pitch = PitchUtil.getPitch(note);
            player.playSound(player.getEyeLocation(), InstrumentUtil.getInstrument(note.getInstrument()), soundCategory, relativeVolume, pitch);
        }
    }
}
