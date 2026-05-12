package atm;
import java.util.ArrayList;

public class Main{
    public static ArrayList<AccountProfile> accounts = new ArrayList<>();
    public static AccountProfile activeAccount = null;
    public static boolean isLoggedIn = false;

    public static void main(String[] args){
        // Initialize SQLite database (creates tables, seeds default accounts on first run)
        Database.init();

        // Load all accounts (and their transaction histories) from the database
        accounts = new ArrayList<>(Database.loadAllAccounts());

        //launch JFrame
        new ATMFrame().setVisible(true);
    }
    public static boolean login(int accountNumber, String pin){
        for(AccountProfile a : accounts){
            if(a.getAccountNumber() == accountNumber && a.getPin().equals(pin)){
                activeAccount = a;
                isLoggedIn = true;
                return true;
            }
        }
        return false;
    }
    public static void logout(){
        activeAccount = null;
        isLoggedIn = false;
    }
}
