package app.account;


import app.Entities.Account;
import app.Entities.Transaction;
import app.db.DB;
import app.transaction.TransactionController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountController {

    Account account;
//    private Transaction Transaction;

    @FXML VBox transactionBox;

    @FXML
    private void initialize(){
        System.out.println("initialize account");
        Platform.runLater(this::loadMoreTransactions);
//        loadMoreTransactions();
    }

    public void setAccount(long number){
        account = DB.getAccount(number);
    }

    void loadMoreTransactions(){
        List<Transaction> transactions = DB.getTransactions(account.getAccountNumber());
        displayTransaction(transactions);
    }

    void displayTransaction(List<Transaction> transactions){
        // For every transaction, do the following:
        for (Transaction transaction : transactions)
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/transaction/transaction.fxml" ) );
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene( fxmlInstance );

            TransactionController controller = loader.getController();
            System.out.println(transaction);
            controller.setTransaction(transaction);

            transactionBox.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void clickLoadTransactions(Event e) { loadMoreTransactions(); }
}
