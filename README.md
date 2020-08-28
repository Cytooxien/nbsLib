# nbsLib
**nbsLib** is a lightweight library for playing .nbs-files. 

This library serves a reformatted and clean code-base inspired by [xxmicloxx's NoteBlockAPI](https://github.com/xxmicloxx/NoteBlockAPI). Unlike the NoteBlockAPI by xxmicloxx **only 1.16.\* is fully supported**.
Since this library is only using the bukkit-api, it may also work with other versions.
(I think 1.12+)

The support of multiple versions **is not intended** and **will not be pursued**!

> **Important: Please do not contact xxmicloxx for errors occurring with this library!**

## **Examples:**

Playing a song to specific players or whole server (e.g. server-radio):
```java
final Song song = Song.createFromFile(new File("songFile.nbs"));
final SimpleSongPlayer songPlayer = new SimpleSongPlayer(song);
// If you're not adding specific players, song will be played for everybody.
        
songPlayer.addListeningPlayer(UUID.randomUUID()); // Replace with uuid of player to be added.
songPlayer.play();
```

Playing a song to a players within a certain range:
```java
final Location centerLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0);
final Song song = Song.createFromFile(new File("songFile.nbs"));
final PositionedSongPlayer songPlayer = new PositionedSongPlayer(centerLocation, song);
songPlayer.setDistance(10); // Radius in blocks

// If your not adding specific players, song will be played for everybody.
songPlayer.addListeningPlayer(UUID.randomUUID()); // Replace with uuid of player to be added.
songPlayer.play();
```

The library comes with a very simple callback-system. Callbacks may be registered within a song-player. 

Add own event-adaper:

```java
final PositionedSongPlayer songPlayer = new PositionedSongPlayer(new Location(Bukkit.getWorld("world"), 0, 100, 0),
        Song.createFromFile(new File("songFile.nbs")));

songPlayer.addEventAdapter(new PositionedSongPlayerEventAdapter() {  
    @Override
    public void onEnd() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onPlay(Song song) {

    }

    @Override
    public void onPlayerAdded(Player player) {

    }

    @Override
    public void onPlayerRemoved(Player player) {

    }

    @Override
    public void onPlayerEnteredRange(Player player) {

    }

    @Override
    public void onPlayerLeftRange(Player player) {

    }
});
songPlayer.play();
```