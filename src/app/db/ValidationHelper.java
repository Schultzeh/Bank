package app.db;

import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

    public static boolean validateString(String message){
        if(message.length() < 50){
            return true;
        }   else {
            errorMessage("Meddelandet kan inte vara längre än 50 tecken");
            return false;
        }
    }

    public static boolean validateName(String name){
        if(name.length() < 12){
            return true;
        }   else {
            errorMessage("Kontonamnet kan inte vara längre än 12 tecken");
            return false;
        }
    }


    public static void errorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Något gick fel");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
