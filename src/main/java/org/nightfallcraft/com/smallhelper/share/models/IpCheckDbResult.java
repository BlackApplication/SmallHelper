package org.nightfallcraft.com.smallhelper.share.models;

public class IpCheckDbResult {
    private final String UserName;
    private final String Address;

    public IpCheckDbResult(String userName, String address) {
        UserName = userName;
        Address = address;
    }

    public String getAddress() { return Address; }

    public String getUserName() { return UserName; }
}
