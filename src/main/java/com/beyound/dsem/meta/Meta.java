package com.beyound.dsem.meta;

import com.beyound.dsem.meta.GUI.DeviceGUI;
import com.beyound.dsem.meta.GUI.ModuleGUI;
import com.beyound.dsem.meta.commands.MetaCommand;
import com.beyound.dsem.meta.commands.SampleCommand;
import com.beyound.dsem.meta.events.MetaEvent;
import com.beyound.dsem.meta.events.SampleEvent;
import com.beyound.dsem.meta.mqtt.ClientAgent;
import com.beyound.dsem.meta.mqtt.MinecraftMqttClient;
import com.beyound.dsem.meta.registry.sync.SyncManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("all")
public final class Meta extends JavaPlugin {
    private static Meta plugin;
    public YamlConfiguration config;
    public Logger log;
    public final String prefix = "§2[meta.dsem] ";

    private MinecraftMqttClient client;

    public static Meta getInstance(){ return plugin;}
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        log = getLogger();
        log.info(prefix+"plugin is enabled");
        plugin.getServer().getPluginManager().registerEvents(new MetaEvent(),plugin);
        plugin.getServer().getPluginManager().registerEvents(new SampleEvent(),plugin);

        getCommand("meta").setExecutor(new MetaCommand());
        getCommand("sample").setExecutor(new SampleCommand());
        Thread thread = new Thread(SyncManager.getInstance());
        thread.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log.info(prefix+"plugin is disabled");
    }
}
