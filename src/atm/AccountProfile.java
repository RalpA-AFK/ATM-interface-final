package atm;
public class AccountProfile{
    private int accountNumber;
    private String pin;
    private int balance;
    private AccountLogs accountLogs;

    public AccountProfile(int accountNumber, String pin, int balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.accountLogs = new AccountLogs(accountNumber);
    }
    public void setBalance(int newBalance) {this.balance = newBalance;}
    public int getBalance() {return this.balance;}
    public int getAccountNumber() {return this.accountNumber;}
    public String getPin() {return this.pin;}
    public AccountLogs getLogs() {return this.accountLogs;}

    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            Database.updateBalance(this.accountNumber, this.balance);
            accountLogs.recordTransaction("Withdrawal", amount, this.balance);
        }
    }
    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
            Database.updateBalance(this.accountNumber, this.balance);
            accountLogs.recordTransaction("Deposit", amount, this.balance);
        }
    }
}
