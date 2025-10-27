package me.superchirok1.playeraccesslist.lists;

import me.superchirok1.playeraccesslist.data.JSONData;
import me.superchirok1.playeraccesslist.data.YAMLData;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.MainConfig;

import java.util.ArrayList;
import java.util.List;

public class Whitelist {

    private final PlayerAccessList plugin;
    private final JSONData jsonData;
    private final YAMLData yamlData;
    private final boolean useJson;

    public Whitelist(PlayerAccessList plugin) {
        this.plugin = plugin;
        this.jsonData = new JSONData(plugin.getDataFolder());
        this.yamlData = new YAMLData(plugin.getDataFolder());
        this.useJson = new MainConfig(plugin).getDataStorage().equalsIgnoreCase("JSON");
    }

    public void add(String name) {
        if (useJson) {
            if (!jsonData.getData().getWhitelist().contains(name)) {
                jsonData.getData().getWhitelist().add(name);
                jsonData.saveData();
                jsonData.loadData();
            }
        } else {
            List<String> list = yamlData.getYaml().getStringList("whitelist");
            if (!list.contains(name)) {
                list.add(name);
                yamlData.getYaml().set("whitelist", list);
                yamlData.saveData();
                yamlData.reload();
            }
        }
    }

    public void remove(String name) {
        if (useJson) {
            if (jsonData.getData().getWhitelist().contains(name)) {
                jsonData.getData().getWhitelist().remove(name);
                jsonData.saveData();
                jsonData.loadData();
            }
        } else {
            List<String> list = yamlData.getYaml().getStringList("whitelist");
            if (list.contains(name)) {
                list.remove(name);
                yamlData.getYaml().set("whitelist", list);
                yamlData.saveData();
                yamlData.reload();
            }
        }
    }

    public boolean has(String name) {
        if (useJson) {
            return jsonData.getData().getWhitelist().contains(name);
        } else {
            return yamlData.getYaml().getStringList("whitelist").contains(name);
        }
    }

    public List<String> getList() {
        List<String> result;
        if (useJson) {
            result = new ArrayList<>(jsonData.getData().getWhitelist());
        } else {
            result = yamlData.getYaml().getStringList("whitelist");
        }
        return result.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .toList();
    }

    public void reload() {
        if (useJson) {
            jsonData.loadData();
        } else {
            yamlData.reload();
        }
    }
}
