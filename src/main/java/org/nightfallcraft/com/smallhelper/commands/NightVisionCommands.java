package org.nightfallcraft.com.smallhelper.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.nightfallcraft.com.smallhelper.SmallHelper;
import org.nightfallcraft.com.smallhelper.share.TextConfig;

import java.util.List;
import java.util.Objects;

public class NightVisionCommands extends AbstractCommands {
    public NightVisionCommands() {
        super("nv");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!hasPermission(sender, "smallhelper.nv", label)) {
            return;
        }

        if (args.length == 0) {
            setNVEffectForPlayer((Player) sender, null);
            return;
        }

        if (args.length == 1) {
            if (!hasPermission(sender, "smallhelper.nv.others", label + " " + args[0])) {
                return;
            }

            setNVEffectForOtherPlayer(sender, args[0]);
            return;
        }

        sender.sendMessage(TextConfig.getText("messages.nv.unCorrectCommand"));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1 && hasPermission(sender, "smallhelper.nv.others", null)) return null;

        return Lists.newArrayList();
    }

    private void setNVEffectForOtherPlayer(CommandSender sender, String playerName) {
        Player player = SmallHelper.getInstance().getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(TextConfig.getText("messages.nv.userNotFound")
                    .replace("{Player}", playerName));
            return;
        }

        setNVEffectForPlayer(player, sender);
    }

    /**
     Set night vision for player
     @param player who needs to be set nv effect
     @param sender who set nv effect (set null if no sender)
     */
    private void setNVEffectForPlayer(Player player, CommandSender sender) {
        for(PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().equals(PotionEffectType.NIGHT_VISION)) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                if (isSenderNullOrEqualPlayer(player, sender)) {
                    player.sendMessage(TextConfig.getText("messages.nv.nvIsOff"));
                } else {
                    sender.sendMessage(TextConfig.getText("messages.nv.OffNvForOtherPlayer")
                            .replace("{Player}", player.getName()));
                    player.sendMessage(TextConfig.getText("messages.nv.nvIsOffByOtherPlayer")
                            .replace("{Player}", sender.getName()));
                }

                return;
            }
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        if (isSenderNullOrEqualPlayer(player, sender)) {
            player.sendMessage(TextConfig.getText("messages.nv.nvIsOn"));
        } else {
            sender.sendMessage(TextConfig.getText("messages.nv.OnNvForOtherPlayer")
                    .replace("{Player}", player.getName()));
            player.sendMessage(TextConfig.getText("messages.nv.nvIsOnByOtherPlayer")
                    .replace("{Player}", sender.getName()));
        }
    }

    private static boolean isSenderNullOrEqualPlayer(Player player, CommandSender sender) {
        return sender == null || Objects.equals(sender.getName(), player.getName());
    }
}
