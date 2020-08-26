package dev.elektronisch.nbslib.player;

import dev.elektronisch.nbslib.NBSLibPlugin;
import dev.elektronisch.nbslib.song.Song;
import dev.elektronisch.nbslib.song.SongPlayerEventAdapter;
import dev.elektronisch.nbslib.song.SongSelectionMode;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractSongPlayer {

    private static final Random RANDOM = new Random();
    private static final NBSLibPlugin PLUGIN = JavaPlugin.getPlugin(NBSLibPlugin.class);

    protected final Song[] songs;
    protected final Set<Player> listeningPlayers = ConcurrentHashMap.newKeySet();
    protected final Set<SongPlayerEventAdapter> eventAdapters = new HashSet<>();

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

                long startTime = System.currentTimeMillis();
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

                final Collection<? extends Player> players = listeningPlayers.isEmpty() ? Bukkit.getOnlinePlayers() : listeningPlayers;
                players.forEach(player -> {
                    if (player != null && player.isOnline()) handleTick(player);
                });

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

    public void addListeningPlayer(final Player player) {
        listeningPlayers.add(player);
        eventAdapters.forEach(adapter -> adapter.onPlayerAdded(player));
    }

    public void removeListeningPlayer(final Player player) {
        listeningPlayers.remove(player);
        eventAdapters.forEach(adapter -> adapter.onPlayerRemoved(player));
    }

    public void addEventAdapter(final SongPlayerEventAdapter adapter) {
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
