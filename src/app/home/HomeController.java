package app.home;

import app.Entities.Account;
import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML Label nameLabel;
    @FXML BorderPane pane;
    @FXML VBox accountsBox;
    @FXML VBox accountOverview;
    @FXML TitledPane newTransaction;
    @FXML TextField transactionFromAccount;
    @FXML TextField transactionToAcc;
    @FXML TextField transactionMessage;
    @FXML TextField transactionAmount;
    @FXML Button transactionSend;

    private Object Account;

    List<Account> userAccounts = null;

    @FXML
    void initialize(){
        // load accounts from db using LoginController.user.getId() and display them
        nameLabel.setText(LoginController.getUser().getFirstName());
        generateAccounts();
    }

    @FXML
    void generateAccounts() {

        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        userAccounts.forEach(account -> {
            Account = account;
            Button accountBtn = new Button("" + account.getAccountName());
            accountBtn.setMinSize(100, 30);
            accountOverview.getChildren().addAll(accountBtn);
            accountBtn.setOnAction(event -> {
                try {
                    goToAccount(account.getAccountNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @FXML
    void makeTransaction(){
        String message = transactionMessage.getText();
        long fromAccount = Long.parseLong(transactionFromAccount.getText());
        long toAccount = Long.parseLong(transactionToAcc.getText());
        float amount = Long.parseLong(transactionAmount.getText());
        DB.makeTransaction(fromAccount, toAccount, amount, message);
    }

    @FXML
    void goToHomeController(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/home/home.fxml"));
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene(fxmlInstance);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void goToAccount(long number) throws IOException {

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/account/account.fxml" ) );
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene( fxmlInstance);
        AccountController controller = loader.getController();
        // Make sure that you display "the correct account" based on which one you clicked on
        controller.setAccount(number);
        pane.setCenter(fxmlInstance);
        // If you don't want to have/use the static variable Main.stage
//        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
//        Main.stage.setScene(scene);
//        Main.stage.show();

    }
}
