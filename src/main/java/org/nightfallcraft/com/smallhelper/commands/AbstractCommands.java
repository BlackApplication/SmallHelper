package org.nightfallcraft.com.smallhelper.commands;

import org.bukkit.command.*;
import org.nightfallcraft.com.smallhelper.SmallHelper;
import org.nightfallcraft.com.smallhelper.share.TextConfig;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommands implements CommandExecutor, TabCompleter {
    public AbstractCommands(String command) {
        PluginCommand pluginCommand = SmallHelper.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        execute(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return filter(complete(sender, args), args);
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    public List<String> complete(CommandSender sender, String[] args) { return null; }

    protected boolean hasPermission(CommandSender sender, String permission, String command) {
        if (!sender.hasPermission(permission)) {
            if (command != null) sender.sendMessage(TextConfig.getText("messages.general.noPermission")
                    .replace("{Command}", command));
            return false;
        }

        return true;
    }

    private List<String> filter(List<String> existing, String[] args) {
        if (existing == null) return null;
        String last = args[args.length - 1];
        List<String> result = new ArrayList<>();
        for (String arg : existing) {
            if (arg.toLowerCase().startsWith(last.toLowerCase())) result.add(arg);
        }

        return result;
    }
}
