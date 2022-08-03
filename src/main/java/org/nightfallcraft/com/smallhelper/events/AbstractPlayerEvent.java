package org.nightfallcraft.com.smallhelper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public abstract class AbstractPlayerEvent implements Listener {
    protected final ArrayList<Player> watchingPlayers = new ArrayList<>();

    public ArrayList<Player> getWatchingPlayers() {
        return watchingPlayers;
    }

    public void addWatchingPlayer(Player addPlayer) {
        this.watchingPlayers.add(addPlayer);
    }

    public void removeWatchingPlayer(Player removePlayer) {
        this.watchingPlayers.remove(removePlayer);
    }
}
