package me.veteran0.vpermission.misc;

import com.nametagedit.plugin.NametagEdit;
import me.veteran0.vpermission.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Groups {

    public static File file = Main.getPlugin().getDataFolder();
    public static File filegroups = new File(file + "/groups/");
    public static File fileusers = new File(file + "/users/");
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static HashMap<String, GroupsAPI> groups = new HashMap<>();
    public static HashMap<Player, String> playergroup = new HashMap<>();
    public static ArrayList<String> gruposyml = new ArrayList<>();

    public static void carregarGrupos() {
        if (!file.exists()) {
            file.mkdir();
        }
        if (!filegroups.exists()) {
            filegroups.mkdir();
            try {
                File defaultgroup = new File(filegroups, "default.yml");
                defaultgroup.createNewFile();
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultgroup);
                fileConfiguration.set("prefixchat", "&7");
                fileConfiguration.set("prefixtab", "&7");
                fileConfiguration.set("changetag", false);
                fileConfiguration.set("permissionsallow", new ArrayList<>());
                fileConfiguration.set("permissionsdeny", new ArrayList<>());
                fileConfiguration.save(defaultgroup);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!fileusers.exists()) {
            fileusers.mkdir();
            try {
                File defaultuser = new File(fileusers, "81f64621-f9bc-478b-bf7a-105d308d2a24.yml");
                defaultuser.createNewFile();
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultuser);
                Date date = new Date();
                fileConfiguration.set("group", "default");
                fileConfiguration.set("firstlogin", formatter.format(date));
                fileConfiguration.set("lastlogin", formatter.format(date));
                fileConfiguration.set("permissionsallow", new ArrayList<>());
                fileConfiguration.set("permissionsdeny", new ArrayList<>());
                fileConfiguration.save(defaultuser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File[] listOfFiles = filegroups.listFiles();
        for (File files : listOfFiles) {
            if (files.isFile()) {
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(files);
                System.out.println(files.getName());
                groups.put(files.getName().replace(".yml", ""),
                        new GroupsAPI(fileConfiguration.getString("prefixchat"),
                                fileConfiguration.getString("prefixtab"), fileConfiguration.getBoolean("changetag"),
                                fileConfiguration.getStringList("permissionsallow"),
                                fileConfiguration.getStringList("permissionsdeny")));
                gruposyml.add(files.getName().replace(".yml", ""));
            }
        }
    }

    public static void entrar(Player player) {
        PermissionAttachment attachment = player.addAttachment(Main.getPlugin());
        try {
            File fileplayer = new File(fileusers, player.getUniqueId().toString() + ".yml");
            FileConfiguration fileConfiguration;
            Date date = new Date();
            if (fileplayer.exists()) {
                fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                fileConfiguration.set("lastlogin", formatter.format(date));

                GroupsAPI groupsAPI = groups.get(fileConfiguration.getString("group"));
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', groupsAPI.getPrefixchat())
                        + player.getName() + ChatColor.RESET);
                NametagEdit.getApi().setPrefix(player, groupsAPI.getPrefixtab());
                playergroup.put(player, fileConfiguration.getString("group"));

                for (String permi : groupsAPI.getPermissionsallow()) {
                    attachment.setPermission(permi, true);
                }
                for (String permi : groupsAPI.getPermissionsdeny()) {
                    attachment.setPermission(permi, false);
                }
                for (String permi : fileConfiguration.getStringList("permissionsallow")) {
                    attachment.setPermission(permi, true);
                }
                for (String permi : fileConfiguration.getStringList("permissionsdeny")) {
                    attachment.setPermission(permi, false);
                }
                fileConfiguration.save(fileplayer);
            } else {
                fileplayer.createNewFile();
                fileConfiguration = YamlConfiguration.loadConfiguration(fileplayer);
                fileConfiguration.set("group", "default");
                fileConfiguration.set("firstlogin", formatter.format(date));
                fileConfiguration.set("lastlogin", formatter.format(date));
                fileConfiguration.set("permissionsallow", new ArrayList<>());
                fileConfiguration.set("permissionsdeny", new ArrayList<>());
                fileConfiguration.save(fileplayer);

                GroupsAPI groupsAPI = groups.get("default");
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', groupsAPI.getPrefixchat())
                        + player.getName() + ChatColor.RESET);
                NametagEdit.getApi().setPrefix(player, groupsAPI.getPrefixtab());
                playergroup.put(player, "default");
                for (String permi : groupsAPI.getPermissionsallow()) {
                    attachment.setPermission(permi, true);
                }
                for (String permi : groupsAPI.getPermissionsdeny()) {
                    attachment.setPermission(permi, false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cmdGroupCreate(Player player, String grupo) {
        if (Groups.gruposyml.contains(grupo.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "Este grupo ja existe!");
        } else {
            try {
                File defaultgroup = new File(Groups.filegroups, grupo.toLowerCase() + ".yml");
                defaultgroup.createNewFile();
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultgroup);
                fileConfiguration.set("prefixchat", "&7");
                fileConfiguration.set("prefixtab", "&7");
                fileConfiguration.set("changetag", false);
                fileConfiguration.set("permissionsallow", new ArrayList<>());
                fileConfiguration.set("permissionsdeny", new ArrayList<>());
                fileConfiguration.save(defaultgroup);
                player.sendMessage(ChatColor.GREEN + "Grupo criado com sucesso!");
                Groups.gruposyml.add(grupo.toLowerCase());
                Groups.groups.put(grupo.toLowerCase(),
                        new GroupsAPI(fileConfiguration.getString("prefixchat"),
                                fileConfiguration.getString("prefixtab"),
                                fileConfiguration.getBoolean("changetag"),
                                fileConfiguration.getStringList("permissionsallow"),
                                fileConfiguration.getStringList("permissionsdeny")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cmdGroupSetchangetag(Player player, String grupo, boolean changetag) {
        if (!Groups.gruposyml.contains(grupo.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "Este grupo nao existe!");
        } else {
            try {
                File defaultgroup = new File(Groups.filegroups, grupo.toLowerCase() + ".yml");
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultgroup);
                fileConfiguration.set("changetag", changetag);
                fileConfiguration.save(defaultgroup);
                player.sendMessage(ChatColor.GREEN + "ChangeTag alterada para " + String.valueOf(changetag).toLowerCase());
                Groups.groups.remove(grupo.toLowerCase());
                Groups.groups.put(grupo.toLowerCase(),
                        new GroupsAPI(fileConfiguration.getString("prefixchat"),
                                fileConfiguration.getString("prefixtab"),
                                fileConfiguration.getBoolean("changetag"),
                                fileConfiguration.getStringList("permissionsallow"),
                                fileConfiguration.getStringList("permissionsdeny")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cmdGroupSetchangeprefixchat(Player player, String grupo, String chat) {
        if (!Groups.gruposyml.contains(grupo.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "Este grupo nao existe!");
        } else {
            try {
                File defaultgroup = new File(Groups.filegroups, grupo.toLowerCase() + ".yml");
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultgroup);
                fileConfiguration.set("prefixchat", chat);
                fileConfiguration.save(defaultgroup);
                player.sendMessage(ChatColor.GREEN + "Prefix alterada para " + String.valueOf(chat).toLowerCase());
                Groups.groups.remove(grupo.toLowerCase());
                Groups.groups.put(grupo.toLowerCase(),
                        new GroupsAPI(fileConfiguration.getString("prefixchat"),
                                fileConfiguration.getString("prefixtab"),
                                fileConfiguration.getBoolean("changetag"),
                                fileConfiguration.getStringList("permissionsallow"),
                                fileConfiguration.getStringList("permissionsdeny")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cmdGroupSetchangeprefixtab(Player player, String grupo, String tab) {
        if (!Groups.gruposyml.contains(grupo.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "Este grupo nao existe!");
        } else {
            try {
                File defaultgroup = new File(Groups.filegroups, grupo.toLowerCase() + ".yml");
                FileConfiguration fileConfiguration;
                fileConfiguration = YamlConfiguration.loadConfiguration(defaultgroup);
                fileConfiguration.set("prefixtab", tab);
                fileConfiguration.save(defaultgroup);
                player.sendMessage(ChatColor.GREEN + "Prefix alterada para " + String.valueOf(tab).toLowerCase());
                Groups.groups.remove(grupo.toLowerCase());
                Groups.groups.put(grupo.toLowerCase(),
                        new GroupsAPI(fileConfiguration.getString("prefixchat"),
                                fileConfiguration.getString("prefixtab"),
                                fileConfiguration.getBoolean("changetag"),
                                fileConfiguration.getStringList("permissionsallow"),
                                fileConfiguration.getStringList("permissionsdeny")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void cmdGroupRemove(Player player, String grupo) {
        if (!gruposyml.contains(grupo.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "Este grupo nao existe!");
        } else if (grupo.equalsIgnoreCase("default")) {
            player.sendMessage(ChatColor.RED + "Este grupo nao pode ser removido!");
        } else {
            File defaultgroup = new File(Groups.filegroups, grupo.toLowerCase() + ".yml");
            defaultgroup.delete();
            player.sendMessage(ChatColor.GREEN + "Grupo deletado com sucesso!");
            Groups.gruposyml.remove(grupo.toLowerCase());
        }
    }

    public static void cmdGroupSet() {

    }

}
