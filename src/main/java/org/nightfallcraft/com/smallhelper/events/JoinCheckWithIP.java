package org.nightfallcraft.com.smallhelper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.nightfallcraft.com.smallhelper.SmallHelper;
import org.nightfallcraft.com.smallhelper.share.TextConfig;
import org.nightfallcraft.com.smallhelper.share.enums.UserIpWarningType;
import org.nightfallcraft.com.smallhelper.share.models.CheckIpEntryModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class JoinCheckWithIP extends AbstractPlayerEvent {
    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String address = player.getAddress().getHostString();

        UserIpWarningType ipWarning = checkIpWarning(player.getName(), address);

        String ipWarningColor = getColorWarning(ipWarning);
        String ipWarningText = getTextWarning(ipWarning);
        for (Player wp : watchingPlayers) {
            wp.sendMessage(TextConfig.getText("messages.playerJoin")
                    .replace("{Player}", player.getName())
                    .replace("{Address}", ipWarningColor + address + ipWarningText));
        }

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

    private UserIpWarningType checkIpWarning(String userName, String address) {
        CheckIpEntryModel checkIpPlayer = SmallHelper.getDbCore().checkIpEntry(userName, address);

        UserIpWarningType userWarning = UserIpWarningType.Clear;
        if (checkIpPlayer.isBadAddress()) {
            userWarning = UserIpWarningType.Bad;
        } else if (checkIpPlayer.isTwink()) {
            userWarning = UserIpWarningType.Twink;
        } else if (checkIpPlayer.isIntersect()) {
            userWarning = UserIpWarningType.Warning;
        }

        return userWarning;
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
