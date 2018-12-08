package me.veteran0.vpermission;

import me.veteran0.vpermission.cmd.Comands;
import me.veteran0.vpermission.events.Events;
import me.veteran0.vpermission.misc.Groups;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin plugin;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(Plugin plugin) {
        Main.plugin = plugin;
    }

    @Override
    public void onEnable() {
        setPlugin(this);
        Groups.carregarGrupos();
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("tag").setExecutor(new Comands());
        getCommand("vpermission").setExecutor(new Comands());
    }
}
