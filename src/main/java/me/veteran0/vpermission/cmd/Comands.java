package me.veteran0.vpermission.cmd;

import com.nametagedit.plugin.NametagEdit;
import me.veteran0.vpermission.Main;
import me.veteran0.vpermission.misc.Groups;
import me.veteran0.vpermission.misc.GroupsAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Comands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Voce nao pode fazer isso pelo console!");
            return true;
        }
        Player player = (Player) sender;
        if (command.getLabel().equalsIgnoreCase("tag")) {
            GroupsAPI groupsAPI = Groups.groups.get(Groups.playergroup.get(player));
            if (!groupsAPI.isChangetag()) {
                player.sendMessage(ChatColor.RED + "Voce nao pode alterar sua tag!");
                return true;
            } else if (Groups.playergroup.get(player).equals("default")) {
                player.sendMessage(ChatColor.RED + "Voce ja esta com a tag padrao!");
                return true;
            } else if (player.getDisplayName()
                    .equals(ChatColor.translateAlternateColorCodes('&', groupsAPI.getPrefixchat()) + player.getName()
                            + ChatColor.RESET)) {
                GroupsAPI defaultg = Groups.groups.get("default");
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', defaultg.getPrefixchat())
                        + player.getName() + ChatColor.RESET);
                NametagEdit.getApi().setPrefix(player, defaultg.getPrefixtab());
                player.sendMessage(ChatColor.GREEN + "Sua tag foi aterada para default!");
                return true;
            } else {
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', groupsAPI.getPrefixchat())
                        + player.getName() + ChatColor.RESET);
                NametagEdit.getApi().setPrefix(player, groupsAPI.getPrefixtab());
                player.sendMessage(ChatColor.GREEN + "Sua tag foi aterada ao normal!");
            }
            return true;
        }
        if (command.getLabel().equalsIgnoreCase("vpermission")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.GREEN + "VPermission v1.0" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN
                        + "By Veteran0");
                return true;
            }
            if (!player.hasPermission("vpermission.admin")) {
                player.sendMessage(ChatColor.RED + "Voce nao pode usar este comando!");
                return  true;
            }
            if (args[0].equalsIgnoreCase("group")) {
                if (args.length <= 2) {
                    player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set/create/remove>)");
                    return true;
                } else {
                    if (args[2].equalsIgnoreCase("set")) {
                        if (args.length == 3) {
                            player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <changetag/permission/prefix>)");
                            return true;
                        }
                        if (args[3].equalsIgnoreCase("changetag")) {
                            if (args.length == 4) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <changetag> <true/false>)");
                                return true;
                            }
                            if (args[4].equalsIgnoreCase("true")) {
                                Groups.cmdGroupSetchangetag(player, args[1].toLowerCase(), true);
                            } else if (args[4].equalsIgnoreCase("false")) {
                                Groups.cmdGroupSetchangetag(player, args[1].toLowerCase(), false);
                            } else {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <changetag> <true/false>)");
                            }
                            return true;
                        } else if (args[3].equalsIgnoreCase("permission")) {
                            if (args.length <= 5) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <permission> permission <add/remove>)");
                                return true;
                            }
                            if (args[5].equalsIgnoreCase("add")) {
                                if (args[4].startsWith("-")) {
                                    try {
                                        File fileplayer = new File(Groups.filegroups, args[1].toLowerCase() + ".yml");
                                        FileConfiguration fileConfiguration;
                                        fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                        List<String> list = fileConfiguration.getStringList("permissionsdeny");
                                        if (list.contains(args[4].replace("-", ""))) {
                                            player.sendMessage(ChatColor.RED + "Este grupo ja possui esta permission como deny.");
                                            return true;
                                        }
                                        list.add(args[4].replace("-", ""));
                                        fileConfiguration.set("permissionsdeny", list);
                                        fileConfiguration.save(fileplayer);
                                        player.sendMessage(ChatColor.GREEN + "Voce setou a permissao " + args[4].replace("-", "") + " como deny.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                                try {
                                    File fileplayer = new File(Groups.filegroups, args[1].toLowerCase() + ".yml");
                                    FileConfiguration fileConfiguration;
                                    fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                    List<String> list = fileConfiguration.getStringList("permissionsallow");
                                    if (list.contains(args[4])) {
                                        player.sendMessage(ChatColor.RED + "Este grupo ja possui esta permission como allow.");
                                        return true;
                                    }
                                    list.add(args[4]);
                                    fileConfiguration.set("permissionsallow", list);
                                    fileConfiguration.save(fileplayer);
                                    player.sendMessage(ChatColor.GREEN + "Voce setou a permissao " + args[4] + " como allow.");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else if (args[5].equalsIgnoreCase("remove")) {
                                if (args[4].startsWith("-")) {
                                    try {
                                        File fileplayer = new File(Groups.filegroups, args[1].toLowerCase() + ".yml");
                                        FileConfiguration fileConfiguration;
                                        fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                        List<String> list = fileConfiguration.getStringList("permissionsdeny");
                                        if (!list.contains(args[4].replace("-", ""))) {
                                            player.sendMessage(ChatColor.RED + "Este grupo nao possui esta permission como deny.");
                                            return true;
                                        }
                                        list.remove(args[4].replace("-", ""));
                                        fileConfiguration.set("permissionsdeny", list);
                                        fileConfiguration.save(fileplayer);
                                        player.sendMessage(ChatColor.GREEN + "Voce removeu a permissao " + args[4].replace("-", "") + " como deny.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                                try {
                                    File fileplayer = new File(Groups.filegroups, args[1].toLowerCase() + ".yml");
                                    FileConfiguration fileConfiguration;
                                    fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                    List<String> list = fileConfiguration.getStringList("permissionsallow");
                                    if (!list.contains(args[4])) {
                                        player.sendMessage(ChatColor.RED + "Este grupo nao possui esta permission como allow.");
                                        return true;
                                    }
                                    list.remove(args[4]);
                                    fileConfiguration.set("permissionsallow", list);
                                    fileConfiguration.save(fileplayer);
                                    player.sendMessage(ChatColor.GREEN + "Voce removeu a permissao " + args[4] + " como allow.");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <permission> permission <add/remove>)");
                            }
                            return true;
                        } else if (args[3].equalsIgnoreCase("prefix")) {
                            if (args.length <= 5) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <prefix> prefix <chat/tab>)");
                                return true;
                            }
                            if (args[5].equalsIgnoreCase("chat")) {
                                Groups.cmdGroupSetchangeprefixchat(player, args[1].toLowerCase(), args[4].toLowerCase());
                            } else if (args[5].equalsIgnoreCase("tab")) {
                                Groups.cmdGroupSetchangeprefixtab(player, args[1].toLowerCase(), args[4].toLowerCase());
                            } else {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <prefix> prefix <chat/tab>)");
                            }
                            return true;
                        } else {
                            player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set> <changetag/permission/prefix>)");
                        }
                        return true;
                    } else if (args[2].equalsIgnoreCase("create")) {
                        Groups.cmdGroupCreate(player, args[1]);
                        return true;
                    } else if (args[2].equalsIgnoreCase("remove")) {
                        Groups.cmdGroupRemove(player, args[1]);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission group <grupo> <set/create/remove>)");
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("user")) {
                if (args.length == 1) {
                    player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set>)");
                    return true;
                }
                Player usuario = Main.getPlugin().getServer().getPlayer(args[1]);
                if (usuario == null) {
                    player.sendMessage(ChatColor.RED + "Este jogador esta offline!");
                } else {
                    if (args.length == 2) {
                        File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                        FileConfiguration fileConfiguration;
                        fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                        player.sendMessage(ChatColor.GREEN + "Dados de " + usuario.getName());
                        player.sendMessage(" ");
                        player.sendMessage(ChatColor.GRAY + "Grupo: " + fileConfiguration.getString("group"));
                        player.sendMessage(ChatColor.GRAY + "First Login: " + fileConfiguration.getString("firstlogin"));
                        player.sendMessage(ChatColor.GRAY + "Last Login: " + fileConfiguration.getString("lastlogin"));
                        List<String> allow = fileConfiguration.getStringList("permissionsallow");
                        if (!allow.isEmpty()) {
                            player.sendMessage(" ");
                            player.sendMessage(ChatColor.RED + "Lista de permissoes liberadas");
                            for (int i = 0; i < allow.size(); i++) {
                                player.sendMessage(ChatColor.GRAY + allow.get(i));
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Nao possui permissoes liberadas");
                        }
                        List<String> deny = fileConfiguration.getStringList("permissionsdeny");
                        if (!deny.isEmpty()) {
                            player.sendMessage(" ");
                            player.sendMessage(ChatColor.RED + "Lista de permissoes bloqueadas");
                            for (int i = 0; i < deny.size(); i++) {
                                player.sendMessage(ChatColor.GRAY + deny.get(i));
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Nao possui permissoes bloqueadas!");
                        }
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("set")) {
                        if (args.length == 3) {
                            player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission/group>)");
                            return true;
                        }
                        if (args[3].equalsIgnoreCase("permission")) {
                            if (args.length == 4) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission> permissao)");
                                return true;
                            }
                            if (args.length == 5) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission> permissao <add/remove>)");
                                return true;
                            }
                            if (args[5].equalsIgnoreCase("add")) {
                                if (args[4].startsWith("-")) {
                                    try {
                                        File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                                        FileConfiguration fileConfiguration;
                                        fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                        List<String> list = fileConfiguration.getStringList("permissionsdeny");
                                        if (list.contains(args[4].replace("-", ""))) {
                                            player.sendMessage(ChatColor.RED + "Este jogador ja possui esta permission como deny.");
                                            return true;
                                        }
                                        list.add(args[4].replace("-", ""));
                                        fileConfiguration.set("permissionsdeny", list);
                                        fileConfiguration.save(fileplayer);
                                        player.sendMessage(ChatColor.GREEN + "Voce setou a permissao " + args[4].replace("-", "") + " como deny.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                                try {
                                    File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                                    FileConfiguration fileConfiguration;
                                    fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                    List<String> list = fileConfiguration.getStringList("permissionsallow");
                                    if (list.contains(args[4])) {
                                        player.sendMessage(ChatColor.RED + "Este jogador ja possui esta permission como allow.");
                                        return true;
                                    }
                                    list.add(args[4]);
                                    fileConfiguration.set("permissionsallow", list);
                                    fileConfiguration.save(fileplayer);
                                    player.sendMessage(ChatColor.GREEN + "Voce setou a permissao " + args[4] + " como allow.");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else if (args[5].equalsIgnoreCase("remove")) {
                                if (args[4].startsWith("-")) {
                                    try {
                                        File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                                        FileConfiguration fileConfiguration;
                                        fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                        List<String> list = fileConfiguration.getStringList("permissionsdeny");
                                        if (!list.contains(args[4].replace("-", ""))) {
                                            player.sendMessage(ChatColor.RED + "Este jogador nao possui esta permission como deny.");
                                            return true;
                                        }
                                        list.remove(args[4].replace("-", ""));
                                        fileConfiguration.set("permissionsdeny", list);
                                        fileConfiguration.save(fileplayer);
                                        player.sendMessage(ChatColor.GREEN + "Voce removeu a permissao " + args[4].replace("-", "") + " como deny.");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                                try {
                                    File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                                    FileConfiguration fileConfiguration;
                                    fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                    List<String> list = fileConfiguration.getStringList("permissionsallow");
                                    if (!list.contains(args[4])) {
                                        player.sendMessage(ChatColor.RED + "Este jogador nao possui esta permission como allow.");
                                        return true;
                                    }
                                    list.remove(args[4]);
                                    fileConfiguration.set("permissionsallow", list);
                                    fileConfiguration.save(fileplayer);
                                    player.sendMessage(ChatColor.GREEN + "Voce removeu a permissao " + args[4] + " como allow.");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission> permissao <add/remove>)");
                                return true;
                            }
                        } else if (args[3].equalsIgnoreCase("group")) {
                            if (args.length == 4) {
                                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <group> grupo)");
                                return true;
                            }
                            if (Groups.gruposyml.contains(args[4].toLowerCase())) {
                                player.sendMessage(ChatColor.GREEN + "Grupo de " + usuario.getName() + " alterado para " + args[4]);
                                try {
                                    File fileplayer = new File(Groups.fileusers, usuario.getUniqueId().toString() + ".yml");
                                    FileConfiguration fileConfiguration;
                                    fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                                    fileConfiguration.set("group", args[4].toLowerCase());
                                    fileConfiguration.save(fileplayer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                usuario.kickPlayer(ChatColor.GREEN + "Seu grupo foi alterado!");
                                return true;
                            } else {
                                player.sendMessage(ChatColor.RED + "Este grupo nao existe!");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission/group>)");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission user <user> <set> <permission/group>)");
                        return true;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Sintax Errada! (/vpermission <group/user>)");
            }
            return true;
        }
        return false;
    }
}
