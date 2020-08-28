package dev.elektronisch.nbslib.player;

import dev.elektronisch.nbslib.NBSLibPlugin;
import dev.elektronisch.nbslib.song.Song;
import dev.elektronisch.nbslib.song.SongPlayerEventAdapter;
import dev.elektronisch.nbslib.song.SongSelectionMode;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSongPlayer<E extends SongPlayerEventAdapter> {

    private static final Random RANDOM = new Random();
    protected static final NBSLibPlugin PLUGIN = JavaPlugin.getPlugin(NBSLibPlugin.class);

    protected final Song[] songs;
    protected final Set<UUID> listeningPlayers = ConcurrentHashMap.newKeySet();
    protected final Set<E> eventAdapters = new HashSet<>();

    protected Song currentSong;
    protected int currentSongIndex;
    protected SongSelectionMode selectionMode = SongSelectionMode.ONCE;
    protected SoundCategory soundCategory = SoundCategory.MASTER;
    protected byte volume = 100;
    protected boolean playing;
    protected short currentTick;

    public AbstractSongPlayer(final Song... songs) {
        if (songs.length == 0) {
            throw new IllegalArgumentException("No songs given");
        }

        this.songs = songs;
        playSong(0);
    }

    public void play() {
        if (playing) {
            return;
        }
        playing = true;
        Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
            while (playing) {
                if (PLUGIN.isDisabling()) break;

                final long startTime = System.currentTimeMillis();
                if (currentTick++ > currentSong.getLength()) {
                    currentTick = -1;
                    if (selectionMode == SongSelectionMode.REPEAT) {
                        continue;
                    } else if (selectionMode == SongSelectionMode.NEXT) {
                        next();
                        eventAdapters.forEach(adapter -> adapter.onPlay(currentSong));
                        continue;
                    } else if (selectionMode == SongSelectionMode.SHUFFLE) {
                        currentSong = songs[RANDOM.nextInt(songs.length)];
                        eventAdapters.forEach(adapter -> adapter.onPlay(currentSong));
                        continue;
                    }
                    playing = false;
                    eventAdapters.forEach(SongPlayerEventAdapter::onEnd);
                    continue;
                }

                if (listeningPlayers.isEmpty()) {
                    Bukkit.getOnlinePlayers().forEach(this::handleTick);
                } else {
                    final Iterator<UUID> iterator = listeningPlayers.iterator();
                    while (iterator.hasNext()) {
                        final UUID uuid = iterator.next();
                        final Player player = Bukkit.getPlayer(uuid);
                        if (player == null) iterator.remove();

                        handleTick(player);
                    }
                }

                final long duration = System.currentTimeMillis() - startTime;
                final float delayMillis = currentSong.getDelay() * 50;
                if (duration < delayMillis) {
                    try {
                        Thread.sleep((long) (delayMillis - duration));
                    } catch (final InterruptedException ignored) {
                    }
                }
            }
        });
    }

    public void pause() {
        if (!playing) return;

        playing = false;
        eventAdapters.forEach(SongPlayerEventAdapter::onPause);
    }

    public void next() {
        int nextIndex = currentSongIndex + 1;
        if (nextIndex >= songs.length) {
            nextIndex = 0;
        }
        playSong(nextIndex);
    }

    public void playSong(int index) {
        this.currentSong = songs[index];
        this.currentTick = -1;
        this.eventAdapters.forEach(adapter -> adapter.onPlay(currentSong));
    }

    public abstract void handleTick(final Player player);

    public void addListeningPlayer(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        listeningPlayers.add(uuid);
        eventAdapters.forEach(adapter -> adapter.onPlayerAdded(player));
    }

    public void removeListeningPlayer(final UUID uuid) {
        listeningPlayers.remove(uuid);

        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        eventAdapters.forEach(adapter -> adapter.onPlayerRemoved(player));
    }

    public Set<UUID> getListeningPlayers() {
        return listeningPlayers;
    }

    public void addEventAdapter(final E adapter) {
        eventAdapters.add(adapter);
    }

    public SongSelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(final SongSelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(final SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(final byte volume) {
        this.volume = volume;
    }
}
