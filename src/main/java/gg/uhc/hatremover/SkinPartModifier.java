package gg.uhc.hatremover;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;

public class SkinPartModifier extends PacketAdapter implements Listener {

    protected Map<UUID, Byte> actualFlags = Maps.newHashMap();

    protected final PlayerSkinPartUpdater skinPartUpdater;

    protected byte removeMask = 0x00;
    protected byte addMask = 0x00;

    public SkinPartModifier(Plugin plugin, PlayerSkinPartUpdater skinPartUpdater, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Client.SETTINGS);
        this.skinPartUpdater = skinPartUpdater;
    }

    protected byte getRemoveMask() {
        return removeMask;
    }

    protected void setRemoveMask(byte removeMask) {
        this.removeMask = removeMask;
    }

    protected byte getAddMask() {
        return addMask;
    }

    protected void setAddMask(byte addMask) {
        this.addMask = addMask;
    }

    /**
     * @param part the skin part to always render for players
     * @see SkinPartModifier#updateAllOnlinePlayers()
     */
    public void forceEnablePart(SkinPart part) {
        removeMask &= ~part.partFlag();
        addMask |= part.partFlag();
    }

    /**
     * @param part the skin part to never render for players
     * @see SkinPartModifier#updateAllOnlinePlayers()
     */
    public void forceDisablePart(SkinPart part) {
        removeMask |= part.partFlag();
        addMask &= ~part.partFlag();
    }

    /**
     * @param part the skin part to clear any modifications on, uses whatever the player chooses
     * @see SkinPartModifier#updateAllOnlinePlayers()
     */
    public void clearPart(SkinPart part) {
        removeMask &= ~part.partFlag();
        addMask &= ~part.partFlag();
    }

    /**
     * Updates the skin parts of all online players. Forces to run now instead of waiting for relog/settings change.
     * Should be called after any changes via forceEnablePart, forceDisablePart, clearPart
     */
    public void updateAllOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();

            if (!actualFlags.containsKey(uuid)) {
                actualFlags.put(uuid, skinPartUpdater.getSkinParts(player));
            }

            byte actual = actualFlags.get(player.getUniqueId());

            // removal flags
            actual &= ~removeMask;

            // addition flags
            actual |= addMask;

            skinPartUpdater.setSkinParts(player, actual);
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        actualFlags.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        StructureModifier<Integer> integers = event.getPacket().getIntegers();

        int skinFlags = integers.read(1);

        // store actual value
        actualFlags.put(event.getPlayer().getUniqueId(), (byte) skinFlags);

        // removal flags
        skinFlags &= ~removeMask;

        // addition flags
        skinFlags |= addMask;

        // modify packet with new flags
        integers.write(1, skinFlags);
    }
}
