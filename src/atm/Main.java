package atm;
import java.util.ArrayList; 

public class Main{
    public static ArrayList<AccountProfile> accounts = new ArrayList<>();
    public static AccountProfile activeAccount = null; 
    public static boolean isLoggedIn = false;

    public static void main(String[] args){
        accounts.add(new AccountProfile(7456, "3239", 1000000));
        accounts.add(new AccountProfile(2120, "0329", 7500));
        accounts.add(new AccountProfile(5409, "6859", 2500));
        
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