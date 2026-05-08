public class Main(){
    public static void main(String[] args){
        AccountProfile account1 = new AccountProfile(123456, 1234, 1000);
        System.out.println("Account Number: " + account1.getAccountNumber());
        System.out.println("Initial Balance: " + account1.getBalance());

        account1.deposit(500);
        System.out.println("Balance after deposit of $500: " + account1.getBalance());

        account1.withdraw(200);
        System.out.println("Balance after withdrawal of $200: " + account1.getBalance());

    }
}