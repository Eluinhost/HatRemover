package gg.uhc.hatremover;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Entry extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new HatRemover(this, ListenerPriority.NORMAL));
    }
}
