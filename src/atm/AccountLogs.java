package atm;
import java.util.ArrayList;

public class AccountLogs {
    private final int accountNumber;
    private ArrayList<LogEntry> entries;

    public AccountLogs(int accountNumber) {
        this.accountNumber = accountNumber;
        this.entries = new ArrayList<>();
    }

    /** Adds a log entry that was loaded from the database (no DB re-write). */
    public void addLoadedEntry(LogEntry e) {
        this.entries.add(e);
    }

    /** Adds a new transaction to memory AND persists it to the database. */
    public void recordTransaction(String transactionType, int amount, int balance){
        LogEntry newEntry = new LogEntry(transactionType, amount, balance);
        this.entries.add(newEntry);
        Database.insertLogEntry(accountNumber, transactionType, amount, balance);
    }

    public ArrayList<LogEntry> getEntries(){
        return this.entries;
    }
}
