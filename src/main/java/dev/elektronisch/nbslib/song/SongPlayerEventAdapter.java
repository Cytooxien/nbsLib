package dev.elektronisch.nbslib.song;

import org.bukkit.entity.Player;

public interface SongPlayerEventAdapter {

    void onEnd();

    void onPause();

    void onPlay(Song song);

    void onPlayerAdded(Player player);

    void onPlayerRemoved(Player player);
}
