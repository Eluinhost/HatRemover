package gg.uhc.hatremover;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.plugin.Plugin;

public class HatRemover extends PacketAdapter {

    public HatRemover(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Client.SETTINGS);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        StructureModifier<Integer> integers = event.getPacket().getIntegers();

        int skinFlags = integers.read(1);

        // remove hat flag
        skinFlags &= ~0x40;

        integers.write(1, skinFlags);
    }
}
