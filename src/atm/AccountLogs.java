import java.util.ArrayList;

public class AccountLogs {
    ArrayList<LogEntry> entries;
    public AccountLogs(String transactionType, int amount, int balance) {
        this.entries = new ArrayList<>();
        LogEntry initialEntry = new LogEntry(transactionType, amount, balance);
        this.entries.add(initialEntry);
    }
    public void recordTransaction(String transactionType, int amount, int balance){
        LogEntry newEntry = new LogEntry(transactionType, amount, balance);
        this.entries.add(newEntry);
    }
    public ArrayList<LogEntry> getEntries(){
        return this.entries;
    }
}