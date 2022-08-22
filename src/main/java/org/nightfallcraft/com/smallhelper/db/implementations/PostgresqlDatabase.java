package org.nightfallcraft.com.smallhelper.db.implementations;

import org.nightfallcraft.com.smallhelper.db.IDatabase;
import org.nightfallcraft.com.smallhelper.share.TextConfig;
import org.nightfallcraft.com.smallhelper.share.models.IpCheckDbResult;
import org.nightfallcraft.com.smallhelper.share.models.IpEntry;

import java.sql.*;
import java.util.ArrayList;

public class PostgresqlDatabase implements IDatabase {
    private static final String table = "UsersIps";
    private final Connection connection;

    public PostgresqlDatabase() throws SQLException {
        connection = dbConnect();
        checkDbTable();
    }

    @Override
    public void AddIpEntry(IpEntry ipEntry) throws SQLException {
        String query = "INSERT INTO " + table + " (UserName, Address, CreateTime) VALUES (?, ?, ?);";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, ipEntry.getPlayerName());
            s.setString(2, ipEntry.getAddress());
            s.setLong(3, ipEntry.getUpdateTime());

            s.executeUpdate();
//            ResultSet set = s.getGeneratedKeys();
//            if (set.next()) {
//                int id = set.getInt(1);
//                ipEntry.setId(id);
//            }
        }
    }

    @Override
    public ArrayList<IpCheckDbResult> GetSameIps(IpEntry ipEntry) {
        ArrayList<IpCheckDbResult> results = new ArrayList<>();
        String query = "SELECT username, address FROM " + table + " WHERE username != ? AND address LIKE ?;";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            s.setString(1, ipEntry.getPlayerName());
            s.setString(2, "%" + ipEntry.getAddress() + "%");
            try (ResultSet set = s.executeQuery()) {
                while (set.next()) {
                    results.add(getIpFromDb(set));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return results;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) connection.close();
    }

    private Connection dbConnect() throws SQLException {
        String connectionString = TextConfig.getText("mysql.connectionString");
        String database = TextConfig.getText("mysql.database");
        String username = TextConfig.getText("mysql.username");
        String password = TextConfig.getText("mysql.password");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + connectionString + "/" + database,
                username,
                password
        );
        System.out.print("[SmallHelper] Подключение к базе успешно успешно!");

        return connection;
    }

    private IpCheckDbResult getIpFromDb(ResultSet set) throws SQLException {
        String userName = set.getString("username");
        String address = set.getString("address");

        return new IpCheckDbResult(userName, address);
    }

    private void checkDbTable() {
        try (Statement s = connection.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " ("
                    + "`Id` INT AUTO_INCREMENT PRIMARY KEY,"
                    + "`UserName` VARCHAR(60),"
                    + "`Address` VARCHAR(30),"
                    + "`CreateTime` BIGINT"
                    + ");"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
