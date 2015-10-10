package gg.uhc.hatremover;

import com.google.common.base.Optional;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StripCommand implements CommandExecutor {

    protected final Plugin plugin;
    protected final SkinPartModifier modifier;

    protected byte noParts = 0x00;
    protected byte allParts;

    protected Optional<BukkitRunnable> runnable = Optional.absent();
    protected byte savedRemove;
    protected byte savedAdd;

    public StripCommand(Plugin plugin, SkinPartModifier modifier) {
        this.plugin = plugin;
        this.modifier = modifier;

        allParts = 0x00;
        for (SkinPart part : SkinPart.values()) {
            allParts |= part.partFlag();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (runnable.isPresent()) {
            sender.sendMessage(ChatColor.RED + "Already running!");
            return true;
        }

        savedRemove = modifier.getRemoveMask();
        savedAdd = modifier.getAddMask();

        modifier.setAddMask(noParts);
        modifier.setRemoveMask(allParts);
        modifier.updateAllOnlinePlayers();

        Bukkit.broadcastMessage("" + ChatColor.AQUA + ChatColor.BOLD + "STRIP TIME!");

        BukkitRunnable r = new RestoreFlagsRunnable();
        r.runTaskLater(plugin, 20 * 5);

        runnable = Optional.of(r);
        return true;
    }

    protected class RestoreFlagsRunnable extends BukkitRunnable {
        @Override
        public void run() {
            modifier.setAddMask(savedAdd);
            modifier.setRemoveMask(savedRemove);
            modifier.updateAllOnlinePlayers();
            runnable = Optional.absent();
        }
    }
}
