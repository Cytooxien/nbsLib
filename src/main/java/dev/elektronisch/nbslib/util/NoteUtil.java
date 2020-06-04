package dev.elektronisch.nbslib.util;

import org.bukkit.Sound;

public final class NoteUtil {

    private NoteUtil() {
    }

    public static Sound getInstrument(final byte instrument) {
        switch (instrument) {
            case 1:
                return Sound.BLOCK_NOTE_BLOCK_BASS;
            case 2:
                return Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
            case 3:
                return Sound.BLOCK_NOTE_BLOCK_SNARE;
            case 4:
                return Sound.BLOCK_NOTE_BLOCK_HAT;
            case 5:
                return Sound.BLOCK_NOTE_BLOCK_GUITAR;
            case 6:
                return Sound.BLOCK_NOTE_BLOCK_FLUTE;
            case 7:
                return Sound.BLOCK_NOTE_BLOCK_BELL;
            case 8:
                return Sound.BLOCK_NOTE_BLOCK_CHIME;
            case 9:
                return Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
            case 10:
                return Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
            case 11:
                return Sound.BLOCK_NOTE_BLOCK_COW_BELL;
            case 12:
                return Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
            case 13:
                return Sound.BLOCK_NOTE_BLOCK_BIT;
            case 14:
                return Sound.BLOCK_NOTE_BLOCK_BANJO;
            case 15:
                return Sound.BLOCK_NOTE_BLOCK_PLING;
            default:
                return Sound.BLOCK_NOTE_BLOCK_HARP;
        }
    }
}
