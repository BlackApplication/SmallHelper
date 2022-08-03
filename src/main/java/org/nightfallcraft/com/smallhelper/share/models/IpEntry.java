package org.nightfallcraft.com.smallhelper.share.models;

import java.util.Objects;

public class IpEntry {

    private int id;
    private final String playerName;
    private final String address;
    private final long updateTime;

    public IpEntry(String playerName, String address, long updateTime) {
        this.playerName = playerName;
        this.address = address;
        this.updateTime = updateTime;
    }

    public static IpEntry create(String playerName, String address) {
        return new IpEntry(playerName, address, System.currentTimeMillis());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getAddress() {
        return address;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpEntry ipEntry = (IpEntry) o;
        return id == ipEntry.id && updateTime == ipEntry.updateTime && Objects.equals(playerName, ipEntry.playerName) && Objects.equals(address, ipEntry.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerName, address, updateTime);
    }
}
