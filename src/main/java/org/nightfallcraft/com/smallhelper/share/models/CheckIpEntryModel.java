package org.nightfallcraft.com.smallhelper.share.models;

public class CheckIpEntryModel {
    private final boolean IsIntersect;
    private final boolean IsTwink;
    private final boolean IsBadAddress;

    public CheckIpEntryModel(boolean isIntersect, boolean isTwink, boolean IsBadAddress) {
        this.IsIntersect = isIntersect;
        this.IsTwink = isTwink;
        this.IsBadAddress = IsBadAddress;
    }

    public boolean isIntersect() { return IsIntersect; }

    public boolean isTwink() { return IsTwink; }

    public boolean isBadAddress() { return IsBadAddress; }
}
