package dev.elektronisch.nbslib;

import org.bukkit.plugin.java.JavaPlugin;

public final class NBSLibPlugin extends JavaPlugin {

    private boolean disabling;

    @Override
    public void onDisable() {
        disabling = true;
    }

    public boolean isDisabling() {
        return disabling;
    }
}
