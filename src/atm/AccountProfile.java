public class AccountProfile(){
    AccountLogs accountlogs = new AccountLogs("Initial", 0, 0);
    public AccountProfile(int accountNumber, int pin, int balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }
    public void setBalance(int newBalance) {this.balance = newBalance;}
    public int getBalance() {return this.balance;}
    public int getAccountNumber() {return this.accountNumber;}
    public int getPin() {return this.pin;}

    public void withdraw(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
        }
    }
    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }
}