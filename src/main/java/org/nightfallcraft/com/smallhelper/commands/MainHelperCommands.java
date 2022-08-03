package org.nightfallcraft.com.smallhelper.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nightfallcraft.com.smallhelper.SmallHelper;
import org.nightfallcraft.com.smallhelper.events.AbstractPlayerEvent;
import org.nightfallcraft.com.smallhelper.events.LeaveCheck;
import org.nightfallcraft.com.smallhelper.share.TextConfig;

import java.util.ArrayList;
import java.util.List;

public class MainHelperCommands extends AbstractCommands {
    public MainHelperCommands() {
        super("sh");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!hasPermission(sender, "smallhelper.sh", label)) {
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(TextConfig.getText("messages.mainHelper.getHelp"));
            return;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(TextConfig.getText("messages.mainHelper.help"));
                return;
            }

            if (args[0].equalsIgnoreCase("jc") || args[0].equalsIgnoreCase("joincheck")) {
                if (!hasPermission(sender, "smallhelper.jc", label + " " + args[0])) {
                    return;
                }

                updateListener(SmallHelper.getJoinListener(), (Player) sender);
                return;
            }

            if (args[0].equalsIgnoreCase("lc") || args[0].equalsIgnoreCase("leavecheck")) {
                if (!hasPermission(sender, "smallhelper.lc", label + " " + args[0])) {
                    return;
                }

                updateListener(SmallHelper.getLeaveListener(), (Player) sender);
                return;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!hasPermission(sender, "smallhelper.reload", label + " " + args[0])) {
                    return;
                }

                SmallHelper.getInstance().reloadConfig();
                sender.sendMessage(TextConfig.getText("messages.mainHelper.successReload"));
                return;
            }
        }

        sender.sendMessage(TextConfig.getText("messages.mainHelper.getHelp"));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1 && hasPermission(sender, "smallhelper.sh", null)) return Lists.newArrayList("help" ,"reload", "jc", "joincheck", "lc", "leavecheck");

        return Lists.newArrayList();
    }

    private void updateListener(AbstractPlayerEvent listener, Player player) {
        ArrayList<Player> players = listener.getWatchingPlayers();
        if (players.contains(player)) {
            listener.removeWatchingPlayer(player);
            player.sendMessage(TextConfig.getText(listener instanceof LeaveCheck ? "messages.mainHelper.leaveCheckOff" : "messages.mainHelper.joinCheckOff"));
            return;
        }

        listener.addWatchingPlayer(player);
        player.sendMessage(TextConfig.getText(listener instanceof LeaveCheck ? "messages.mainHelper.leaveCheckOn" : "messages.mainHelper.joinCheckOn"));
    }
}
