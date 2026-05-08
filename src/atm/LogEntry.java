public class LogEntry{ 
    private String transactionType;
    private int amount;
    private int balance;
    public LogEntry(String transactionType, int amount, int balance) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
    }
    public String getTransactionType(){return this.transactionType;}
    public int getAmount(){return this.amount;}
    public int getBalance(){return this.balance;}
}