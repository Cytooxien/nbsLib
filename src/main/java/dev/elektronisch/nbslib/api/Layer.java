package dev.elektronisch.nbslib.api;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Objects;

public final class Layer {

    private final Int2ObjectMap<Note> noteMap = new Int2ObjectOpenHashMap<>();

    private String name;
    private byte volume;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public byte getVolume() {
        return volume;
    }

    public void setVolume(final byte volume) {
        this.volume = volume;
    }

    public Int2ObjectMap<Note> getNoteMap() {
        return noteMap;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Layer layer = (Layer) o;
        return volume == layer.volume &&
                noteMap.equals(layer.noteMap) &&
                name.equals(layer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteMap, name, volume);
    }

    @Override
    public String toString() {
        return "Layer{" +
                "noteMap=" + noteMap +
                ", name='" + name + '\'' +
                ", volume=" + volume +
                '}';
    }
}
