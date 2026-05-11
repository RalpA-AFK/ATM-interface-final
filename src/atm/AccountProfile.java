public class AccountProfile{
    private int accountNumber;
    private String pin;
    private int balance;
    private AccountLogs accountLogs;

    public AccountProfile(int accountNumber, String pin, int balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.accountLogs = new AccountLogs("Initial", 0, balance);
    }
    public void setBalance(int newBalance) {this.balance = newBalance;}
    public int getBalance() {return this.balance;}
    public int getAccountNumber() {return this.accountNumber;}
    public String getPin() {return this.pin;}

    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            accountLogs.recordTransaction("Withdrawal", amount, this.balance - amount); 
        }
    }
    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
            accountLogs.recordTransaction("Deposit", amount, this.balance + amount);
        }
    }
}