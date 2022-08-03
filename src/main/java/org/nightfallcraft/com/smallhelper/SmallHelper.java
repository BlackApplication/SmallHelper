package org.nightfallcraft.com.smallhelper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.nightfallcraft.com.smallhelper.commands.MainHelperCommands;
import org.nightfallcraft.com.smallhelper.commands.NightVisionCommands;
import org.nightfallcraft.com.smallhelper.db.DataBaseCore;
import org.nightfallcraft.com.smallhelper.events.JoinCheckWithIP;
import org.nightfallcraft.com.smallhelper.events.LeaveCheck;

import java.sql.SQLException;

public final class SmallHelper extends JavaPlugin {
    private static SmallHelper instance;
    private static JoinCheckWithIP joinListener;
    private static LeaveCheck leaveListener;
    private DataBaseCore dbCore;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        createDbCore();

        new MainHelperCommands();
        new NightVisionCommands();

        initListeners();
    }

    @Override
    public void onDisable() {
        if (dbCore != null) {
            try {
                dbCore.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SmallHelper getInstance() { return instance; }

    public static JoinCheckWithIP getJoinListener() { return joinListener; }

    public static LeaveCheck getLeaveListener() { return leaveListener; }

    public static DataBaseCore getDbCore() { return instance.dbCore; }

    private void initListeners() {
        joinListener = new JoinCheckWithIP();
        Bukkit.getPluginManager().registerEvents(joinListener, this);
        leaveListener = new LeaveCheck();
        Bukkit.getPluginManager().registerEvents(leaveListener, this);
    }

    private void createDbCore() {
        try {
            dbCore = new DataBaseCore();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
