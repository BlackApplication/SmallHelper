package org.nightfallcraft.com.smallhelper.db;

import org.apache.commons.lang.ArrayUtils;
import org.nightfallcraft.com.smallhelper.db.implementations.PostgresqlDatabase;
import org.nightfallcraft.com.smallhelper.share.models.CheckIpEntryModel;
import org.nightfallcraft.com.smallhelper.share.models.IpCheckDbResult;
import org.nightfallcraft.com.smallhelper.share.models.IpEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class DataBaseCore {
    private final IDatabase postgresqlDb;

    public DataBaseCore() throws SQLException {
        postgresqlDb = new PostgresqlDatabase();
    }

    public void AddIpEntry(String playerName, String address) {
        IpEntry ipEntry = IpEntry.create(playerName, address);
        try {
            postgresqlDb.AddIpEntry(ipEntry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CheckIpEntryModel checkIpEntry(String playerName, String address) {
        String[] addressParts = address.split("\\.");
        if (addressParts.length != 4) return new CheckIpEntryModel(false, false, true);

        // Получение адреса без последнего фрагмента, для поиска похожих
        addressParts = (String[]) ArrayUtils.remove(addressParts, 3);
        String newAddress = String.join(".", addressParts);
        IpEntry ipEntry = IpEntry.create(playerName, newAddress);
        ArrayList<IpCheckDbResult> sameIps = postgresqlDb.GetSameIps(ipEntry);

        return sameIps.size() == 0
                ? new CheckIpEntryModel(false, false, false)
                : compareIpWithSame(address, sameIps);
    }

//    public void async(Runnable runnable) {
//        Bukkit.getScheduler().runTaskAsynchronously(SmallHelper.getInstance(), () -> {
//            synchronized (DataBaseCore.this) {
//                runnable.run();
//            }
//        });
//    }
//
//    public void sync(Runnable runnable) {
//        Bukkit.getScheduler().scheduleSyncDelayedTask(SmallHelper.getInstance(), runnable);
//    }

    public void close() {
        try {
            postgresqlDb.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CheckIpEntryModel compareIpWithSame(String address, ArrayList<IpCheckDbResult> sameIps) {
        for (IpCheckDbResult sameIp: sameIps) {
            if (Objects.equals(sameIp.getAddress(), address)) {
                return new CheckIpEntryModel(false, true, false);
            }
        }

        return new CheckIpEntryModel(true, false, false);
    }
}
