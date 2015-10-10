package gg.uhc.hatremover;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class PlayerSkinPartUpdater {

    protected final Method getHandleMethod;
    protected final Method getDataWatcherMethod;
    protected final Method updateMethod;
    protected final Method getByteMethod;

    public PlayerSkinPartUpdater() throws ReflectiveOperationException {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf(".") + 1);

        Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
        getHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");

        Class<?> entityClass = Class.forName("net.minecraft.server." + version + ".Entity");
        getDataWatcherMethod = entityClass.getDeclaredMethod("getDataWatcher");

        Class<?> dataWatcherClass = Class.forName("net.minecraft.server." + version + ".DataWatcher");
        updateMethod = dataWatcherClass.getDeclaredMethod("watch", int.class, Object.class);
        getByteMethod = dataWatcherClass.getDeclaredMethod("getByte", int.class);
    }

    public byte getSkinParts(Player player) {
        try {
            Object watcher = getDataWatcherMethod.invoke(getHandleMethod.invoke(player));
            return  (Byte) getByteMethod.invoke(watcher, 10);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return 0xF;
        }
    }

    public void setSkinParts(Player player, byte parts) {
        try {
            Object watcher = getDataWatcherMethod.invoke(getHandleMethod.invoke(player));
            updateMethod.invoke(watcher, 10, parts);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }
}
