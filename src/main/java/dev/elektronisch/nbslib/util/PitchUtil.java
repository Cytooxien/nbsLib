package dev.elektronisch.nbslib.util;

import dev.elektronisch.nbslib.api.Note;

import java.util.HashMap;
import java.util.Map;

public final class PitchUtil {

    private static final float[] PITCHES = new float[2401];
    private static final Map<Note, Float> PITCH_MAP = new HashMap<>();

    static {
        for (int i = 0; i < 2401; i++) {
            PITCHES[i] = (float) Math.pow(2, (i - 1200d) / 1200d);
        }
    }

    private PitchUtil() {

    }

    public static float getPitch(final Note note) {
        return PITCH_MAP.computeIfAbsent(note, n -> calculatePitch(note.getKey(), note.getPitch()));
    }

    public static float calculatePitch(final byte key, short pitch) {
        pitch += (key - 33) * 100;
        if (pitch < 0) pitch = 0;
        if (pitch > 2400) pitch = 2400;
        return PITCHES[pitch];
    }

}
