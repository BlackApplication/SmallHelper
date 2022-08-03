package org.nightfallcraft.com.smallhelper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.nightfallcraft.com.smallhelper.share.TextConfig;

public class LeaveCheck extends AbstractPlayerEvent {
    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (Player wp : watchingPlayers) {
            wp.sendMessage(TextConfig.getText("messages.playerLeave")
                    .replace("{Player}", player.getName()));
        }
    }
}
