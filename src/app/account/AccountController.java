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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountController {

    Account account;
//    private Transaction Transaction;

    @FXML public VBox transactionBox;
    @FXML Button loadAllTransactionsBtn;
    @FXML Label accountNameHeadline;
    @FXML Label accountNumberHeadline;
    @FXML Label accountBalanceHeadline;

    @FXML
    private void initialize(){
        System.out.println("initialize account");
        Platform.runLater(this::loadTransactions);
    }

    public void setAccount(long number){
        account = DB.getAccount(number);
    }

    void loadTransactions(){
        List<Transaction> transactions = DB.getTransactions(account.getAccountNumber());
        accountNameHeadline.setText(account.getAccountName());
        accountNumberHeadline.setText("" + account.getAccountNumber());
        accountBalanceHeadline.setText("" + account.getBalance());
        displayTransaction(transactions);
    }

    @FXML
    public void reloadTransactions(){
        transactionBox.getChildren().clear();
        loadTransactions();
    }

    void loadMoreTransactions(){
        List<Transaction> transactions = DB.getTransactions(account.getAccountNumber(), 10);
        displayTransaction(transactions);
        loadAllTransactionsBtn.setVisible(false);
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
            controller.setTransaction(transaction, account.getAccountNumber());

            transactionBox.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void clickLoadTransactions(Event e) { loadMoreTransactions(); }
}
