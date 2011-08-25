package net.D3GN.MiracleM4n.mChannel;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;

public class MConfigListener {
    mChannel plugin;
    Boolean hasChanged = false;

   	public MConfigListener(mChannel plugin) {
		this.plugin = plugin;
	}

    protected void loadConfig() {
        Configuration config = plugin.config;
        config.load();
        plugin.joinMessage = config.getBoolean("Join-Message", plugin.joinMessage);
    }

    protected void defaultConfig() {
        Configuration config = plugin.config;
        config.save();
        config.setHeader(
            "# mChannel configuration file",
            ""
        );
        config.setProperty("Join-Message", plugin.joinMessage);
        config.save();
    }

    protected void checkConfig() {
        Configuration config = plugin.config;
        PluginDescriptionFile pdfFile = plugin.getDescription();
        config.load();

        if (config.getProperty("Join-Message") == null) {
            config.setProperty("Join-Message", plugin.joinMessage);
            hasChanged = true;
        }

        if (hasChanged) {
            config.setHeader(
                "# mChannel configuration file",
                ""
            );

            plugin.console.sendMessage("[" + pdfFile.getName() + "]" + " config.yml has been updated.");
            config.save();
        }
    }
}
