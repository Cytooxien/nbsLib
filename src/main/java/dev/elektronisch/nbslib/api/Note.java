package dev.elektronisch.nbslib.api;

import java.util.Objects;

public final class Note {

    private final byte instrument, key, velocity;
    private final short pitch;

    public Note(final byte instrument, final byte key, final byte velocity, final short pitch) {
        this.instrument = instrument;
        this.key = key;
        this.velocity = velocity;
        this.pitch = pitch;
    }

    public byte getInstrument() {
        return instrument;
    }

    public byte getKey() {
        return key;
    }

    public byte getVelocity() {
        return velocity;
    }

    public short getPitch() {
        return pitch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return instrument == note.instrument &&
                key == note.key &&
                velocity == note.velocity &&
                pitch == note.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, key, velocity, pitch);
    }

    @Override
    public String toString() {
        return "Note{" +
                "instrument=" + instrument +
                ", key=" + key +
                ", velocity=" + velocity +
                ", pitch=" + pitch +
                '}';
    }
}
