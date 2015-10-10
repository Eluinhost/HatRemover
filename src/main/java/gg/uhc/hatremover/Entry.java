package gg.uhc.hatremover;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.google.common.base.Converter;
import com.google.common.base.Enums;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    protected static final Converter<String, SkinPart> SKIN_PART_CONVERTER = Enums.stringConverter(SkinPart.class);

    @Override
    public void onEnable() {
        FileConfiguration configuration = getConfig();
        configuration.options().copyDefaults(true);
        saveConfig();

        PlayerSkinPartUpdater skinPartUpdater;
        try {
            skinPartUpdater = new PlayerSkinPartUpdater();
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            getLogger().severe("This version of bukkit/spigot is not supported, are you using the correct version?");
            setEnabled(false);
            return;
        }

        SkinPartModifier modifier = new SkinPartModifier(this, skinPartUpdater, ListenerPriority.NORMAL);

        try {
            for (SkinPart remove : SKIN_PART_CONVERTER.convertAll(configuration.getStringList("force remove parts"))) {
                modifier.forceDisablePart(remove);
            }

            for (SkinPart add : SKIN_PART_CONVERTER.convertAll(configuration.getStringList("force enable parts"))) {
                modifier.forceEnablePart(add);
            }

            // update any online players so their skins are updated immediately
            modifier.updateAllOnlinePlayers();

            ProtocolLibrary.getProtocolManager().addPacketListener(modifier);
            getServer().getPluginManager().registerEvents(modifier, this);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            getLogger().severe("Invalid configuration found, plugin is disabled");
            setEnabled(false);
        }
    }
}
