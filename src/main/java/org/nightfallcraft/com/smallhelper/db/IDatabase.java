package org.nightfallcraft.com.smallhelper.db;

import org.nightfallcraft.com.smallhelper.share.models.IpCheckDbResult;
import org.nightfallcraft.com.smallhelper.share.models.IpEntry;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDatabase {
    void AddIpEntry(IpEntry ipEntry) throws SQLException;
    ArrayList<IpCheckDbResult> GetSameIps(IpEntry ipEntry);
    void close() throws SQLException;
}
