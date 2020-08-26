package dev.elektronisch.nbslib.listener;

import dev.elektronisch.nbslib.NBSLibPlugin;
import dev.elektronisch.nbslib.player.PositionedSongPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Set;

public final class SongPlayerRangeListener implements Listener {

    private final NBSLibPlugin plugin;

    public SongPlayerRangeListener(final NBSLibPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        processRange(player, player.getLocation());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        for (final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        final Player player = event.getPlayer();
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
                && event.getFrom().getBlockY() == event.getTo().getBlockY()
                && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        processRange(player, event.getTo());
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        processRange(player, event.getTo());
    }

    @EventHandler
    public void onWorldChanged(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        for (final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }
        processRange(player, player.getLocation());
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        for (final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            songPlayer.playerLeftRange(player);
        }
        processRange(player, event.getRespawnLocation());
    }

    private void processRange(final Player player, final Location location) {
        for (final PositionedSongPlayer songPlayer : plugin.getPositionedSongPlayers()) {
            if (!songPlayer.getTargetLocation().getWorld().equals(location.getWorld()))
                continue;

            final Set<Player> listeningPlayers = songPlayer.getListeningPlayers();
            if (listeningPlayers.isEmpty() || listeningPlayers.contains(player)) {
                final double newDistance = player.getLocation().distanceSquared(songPlayer.getTargetLocation());
                if (newDistance > songPlayer.getDistanceSquared()) {
                    songPlayer.playerLeftRange(player);
                } else songPlayer.playerEnteredRange(player);
            }
        }
    }
}
