package me.veteran0.vpermission.events;

import me.veteran0.vpermission.misc.Groups;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    @EventHandler
    void entrar(PlayerJoinEvent event) {
        Groups.entrar(event.getPlayer());
    }

    @EventHandler
    void sair(PlayerQuitEvent event) {
        Groups.playergroup.remove(event.getPlayer());
    }
}
