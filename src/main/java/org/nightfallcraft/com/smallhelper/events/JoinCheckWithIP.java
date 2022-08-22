package org.nightfallcraft.com.smallhelper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nightfallcraft.com.smallhelper.SmallHelper;
import org.nightfallcraft.com.smallhelper.share.TextConfig;
import org.nightfallcraft.com.smallhelper.share.enums.UserIpWarningType;

import java.util.ArrayList;
import java.util.Objects;

public class JoinCheckWithIP extends AbstractPlayerEvent {
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String address = player.getAddress().getHostString();

        checkIpWarningAndSendToWatchers(player.getName(), address);

        AddedUserIpToDB(player.getName(), address);
        ReconnectUserToListeners(player);
    }

    private void ReconnectUserToListeners(Player reconnectPlayer) {
        ArrayList<Player> joinWatchingPlayers = getWatchingPlayers();
        for (Player player: joinWatchingPlayers) {
            if (Objects.equals(player.getName(), reconnectPlayer.getName())) {
                removeWatchingPlayer(player);
                addWatchingPlayer(reconnectPlayer);
            }
        }

        LeaveCheck leaveListener = SmallHelper.getLeaveListener();
        ArrayList<Player> leaveWatchingPlayers = leaveListener.getWatchingPlayers();
        for (Player player: leaveWatchingPlayers) {
            if (Objects.equals(player.getName(), reconnectPlayer.getName())) {
                leaveListener.removeWatchingPlayer(player);
                leaveListener.addWatchingPlayer(reconnectPlayer);
            }
        }
    }

    private void AddedUserIpToDB(String userName, String address) {
        SmallHelper.getDbCore().AddIpEntry(userName, address);
    }

    private void checkIpWarningAndSendToWatchers(String userName, String address) {
        SmallHelper.getDbCore().checkIpEntry(userName, address, (checkIpPlayer) -> {
            UserIpWarningType userWarning = UserIpWarningType.Clear;
            if (checkIpPlayer.isBadAddress()) {
                userWarning = UserIpWarningType.Bad;
            } else if (checkIpPlayer.isTwink()) {
                userWarning = UserIpWarningType.Twink;
            } else if (checkIpPlayer.isIntersect()) {
                userWarning = UserIpWarningType.Warning;
            }

            String ipWarningColor = getColorWarning(userWarning);
            String ipWarningText = getTextWarning(userWarning);
            for (Player wp : watchingPlayers) {
                wp.sendMessage(TextConfig.getText("messages.playerJoin")
                        .replace("{Player}", userName)
                        .replace("{Address}", ipWarningColor + address + ipWarningText));
            }
        });
    }

    private String getColorWarning(UserIpWarningType ipWarning) {
        return ipWarning == UserIpWarningType.Clear
                ? TextConfig.getText("messages.ipWarning.clear.color")
                : ipWarning == UserIpWarningType.Warning
                    ? TextConfig.getText("messages.ipWarning.indent.color")
                    : ipWarning == UserIpWarningType.Twink
                        ? TextConfig.getText("messages.ipWarning.twink.color")
                        : TextConfig.getText("messages.ipWarning.bad.color");
    }

    private String getTextWarning(UserIpWarningType ipWarning) {
        return ipWarning == UserIpWarningType.Clear
                ? TextConfig.getText("messages.ipWarning.clear.suffix")
                : ipWarning == UserIpWarningType.Warning
                    ? TextConfig.getText("messages.ipWarning.indent.suffix")
                    : ipWarning == UserIpWarningType.Twink
                        ? TextConfig.getText("messages.ipWarning.twink.suffix")
                        : TextConfig.getText("messages.ipWarning.bad.suffix");
    }
}
