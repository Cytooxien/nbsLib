package dev.elektronisch.nbslib;

import dev.elektronisch.nbslib.listener.SongPlayerRangeListener;
import dev.elektronisch.nbslib.player.PositionedSongPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class NBSLibPlugin extends JavaPlugin {

    private final Set<PositionedSongPlayer> positionedSongPlayers = ConcurrentHashMap.newKeySet();
    private boolean disabling;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SongPlayerRangeListener(this), this);
    }

    @Override
    public void onDisable() {
        disabling = true;
    }

    public Set<PositionedSongPlayer> getPositionedSongPlayers() {
        return positionedSongPlayers;
    }

    public boolean isDisabling() {
        return disabling;
    }
}
