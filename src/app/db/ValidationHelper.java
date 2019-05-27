package app.db;

import app.Entities.Account;
import app.login.LoginController;
import javafx.scene.control.Alert;

import java.util.List;

public class ValidationHelper {

    static List<Account> allAccounts = null;

    public static boolean validateString(String message) {
        if (message.length() < 50) {
            return true;
        } else {
            errorMessage("Meddelandet kan inte vara längre än 50 tecken");
            return false;
        }
    }

    public static boolean validateBalance(double balance, float amount) {
        if (amount < balance) {
            return true;
        } else {
            errorMessage("Du har inte tillräckligt med pengar på ditt konto");
            return false;
        }
    }

    public static boolean validateAccountNumberExist(long toAccount) {
        allAccounts = (List<Account>) DB.getAllAccounts();

        for (Account account : allAccounts) {

            long accountNumber = account.getAccountNumber();
            if (accountNumber == toAccount) {
                return true;
            }
        }
        errorMessage("Kontot finns inte");
        return false;
    }

    public static boolean validateDeleteAccount(String accountType, double balance) {
        if (accountType.equals("salary")) {
            errorMessage("Du kan inte ta bort lönekonto");
            return false;
        } else if (accountType.equals("creditcard")) {
            errorMessage("Du kan inte ta bort kortkonto");
            return false;
        }   else if(validateDeleteAccountBalance(balance)){
            return true;
        }
        return false;
    }

    public static boolean validateDeleteAccountBalance(double balance) {
        if (balance > 0) {
            errorMessage("Du kan inte ta bort konto som har pengar");
            return false;
        } else if (balance < 0) {
            errorMessage("Du kan inte ta bort ett konto med negativt saldo");
            return false;
        }
        return true;
    }

    public static boolean validateName(String name) {
        if (name.length() < 12) {
            return true;
        } else {
            errorMessage("Kontonamnet kan inte vara längre än 12 tecken");
            return false;
        }
    }


    public static void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Något gick fel");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
