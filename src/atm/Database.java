package atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all SQLite persistence for accounts and transaction logs.
 * Database file (atm.db) is created in the project root on first run.
 */
public class Database {

    private static final String DB_URL = "jdbc:sqlite:atm.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /** Creates tables if missing and seeds default accounts on first run. */
    public static void init() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "  account_number INTEGER PRIMARY KEY," +
                "  pin            TEXT NOT NULL," +
                "  balance        INTEGER NOT NULL" +
                ")"
            );
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS logs (" +
                "  id               INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  account_number   INTEGER NOT NULL," +
                "  transaction_type TEXT NOT NULL," +
                "  amount           INTEGER NOT NULL," +
                "  balance          INTEGER NOT NULL," +
                "  FOREIGN KEY(account_number) REFERENCES accounts(account_number)" +
                ")"
            );
            seedIfEmpty(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedIfEmpty(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts")) {
            if (rs.next() && rs.getInt(1) == 0) {
                seedAccount(conn, 7456, "3239", 1000000);
                seedAccount(conn, 2120, "0329", 7500);
                seedAccount(conn, 5409, "6859", 2500);
            }
        }
    }

    private static void seedAccount(Connection conn, int accountNumber, String pin, int balance) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO accounts (account_number, pin, balance) VALUES (?, ?, ?)")) {
            ps.setInt(1, accountNumber);
            ps.setString(2, pin);
            ps.setInt(3, balance);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO logs (account_number, transaction_type, amount, balance) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, accountNumber);
            ps.setString(2, "Initial");
            ps.setInt(3, 0);
            ps.setInt(4, balance);
            ps.executeUpdate();
        }
    }

    /** Loads every account along with its full transaction history. */
    public static List<AccountProfile> loadAllAccounts() {
        List<AccountProfile> result = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT account_number, pin, balance FROM accounts")) {
            while (rs.next()) {
                int accountNumber = rs.getInt("account_number");
                String pin = rs.getString("pin");
                int balance = rs.getInt("balance");
                AccountProfile a = new AccountProfile(accountNumber, pin, balance);
                for (LogEntry entry : loadLogsFor(accountNumber)) {
                    a.getLogs().addLoadedEntry(entry);
                }
                result.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<LogEntry> loadLogsFor(int accountNumber) {
        List<LogEntry> result = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT transaction_type, amount, balance FROM logs WHERE account_number = ? ORDER BY id")) {
            ps.setInt(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new LogEntry(
                        rs.getString("transaction_type"),
                        rs.getInt("amount"),
                        rs.getInt("balance")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void updateBalance(int accountNumber, int balance) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE accounts SET balance = ? WHERE account_number = ?")) {
            ps.setInt(1, balance);
            ps.setInt(2, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertLogEntry(int accountNumber, String type, int amount, int balance) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO logs (account_number, transaction_type, amount, balance) VALUES (?, ?, ?, ?)")) {
            ps.setInt(1, accountNumber);
            ps.setString(2, type);
            ps.setInt(3, amount);
            ps.setInt(4, balance);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
