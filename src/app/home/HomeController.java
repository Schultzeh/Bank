package app.home;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.db.ValidationHelper;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static app.db.DB.getFiveLatestTransactions;

public class HomeController {

    @FXML Label nameLabel;
    @FXML BorderPane pane;
    @FXML VBox fiveTransactionsBox;
    @FXML VBox accountOverview;
    @FXML TitledPane newTransaction;
    @FXML TextField transactionToAcc;
    @FXML TextField transactionMessage;
    @FXML TextField transactionAmount;
    @FXML Button transactionSend;
    @FXML ChoiceBox transactionFromList;
    @FXML TextField newAccountNameInput;
    @FXML Button createNewAccount;
    @FXML Label totalBalance;

    Account account = new Account();

    List<Account> userAccounts = null;

    @FXML
    void initialize(){
        nameLabel.setText(LoginController.getUser().getFirstName());
        generateAccounts();
        generateChoiceBox();
        loadLatestTransactions();
        totalBalance.setText("" + DB.getTotalBalance(LoginController.getUser().getSocialNumber()));
    }

    @FXML
    void generateAccounts() {

        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        userAccounts.forEach(account -> {
            HBox accountBox = new HBox();
            Label accountNameLabel = new Label(account.getAccountName());
            Label accountBalanceLabel = new Label("" + account.getBalance());
            Label accountDelete = new Label();
            Label accountRename = new Label();
            ImageView trashcanImg = new ImageView("app/home/delete.png");
            ImageView renameImg = new ImageView("app/home/rename.png");
            setAccoutsLabel(accountNameLabel, accountBalanceLabel, trashcanImg, renameImg, accountDelete, accountRename);
            accountBox.getChildren().addAll(accountNameLabel, accountBalanceLabel, accountRename, accountDelete);
            accountOverview.getChildren().addAll(accountBox);
            accountNameLabel.setOnMouseClicked(event -> {
                try {
                    goToAccount(account.getAccountNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            accountDelete.setOnMouseClicked(event -> deleteAccountPopup(account.getAccountNumber(), account.getAccountName(), account.getAccountType(), account.getBalance()));
            accountRename.setOnMouseClicked(event -> renameAccountPopup(account.getAccountNumber(), account.getAccountName()));
        });
    }

    void setAccoutsLabel(Label accountNameLabel, Label accountBalanceLabel, ImageView trashcanImg, ImageView renameImg, Label accountDelete, Label accountRename){
        accountNameLabel.setStyle("-fx-background-color: #77ffdd;");
        accountBalanceLabel.setStyle("-fx-background-color: #77ffdd;");
        accountNameLabel.setMinSize(75, 30);
        accountNameLabel.setPadding(new Insets(0,0,0,5));
        accountBalanceLabel.setMinSize(90, 30);
        accountBalanceLabel.setAlignment(Pos.CENTER_RIGHT);
        accountBalanceLabel.setPadding(new Insets(0,5,0,0));
        accountDelete.setMinHeight(30);
        accountDelete.setGraphic(trashcanImg);
        accountRename.setMinHeight(30);
        accountRename.setGraphic(renameImg);
        accountRename.setPadding(new Insets(0,5,0,5));
        trashcanImg.setFitHeight(20);
        trashcanImg.setFitWidth(20);
        renameImg.setFitHeight(20);
        renameImg.setFitWidth(20);
    }

    @FXML
    void makeTransaction() {
        String message = transactionMessage.getText();
        long fromAccount = Long.parseLong(transactionFromList.getValue().toString());
        long toAccount = Long.parseLong(transactionToAcc.getText());
        float amount = Float.parseFloat(transactionAmount.getText());
        double balance = DB.getBalance(fromAccount);
        if (ValidationHelper.validateString(message) && ValidationHelper.validateBalance(balance, amount) && ValidationHelper.validateAccountNumberExist(toAccount)) {
            DB.makeTransaction(fromAccount, toAccount, amount, message);
            try {
                goToAccount(fromAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML void makeCardTransaction(){
        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        userAccounts.forEach(account -> {
            if(account.getAccountType().equals("creditcard")){
                Long fromAccount = account.getAccountNumber();
                DB.makeCardTransaction(fromAccount);
            }
        });
    }

    @FXML
    void generateChoiceBox(){
        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        transactionFromList.getItems().clear();
        userAccounts.forEach(account -> {
            transactionFromList.getItems().addAll(account.getAccountNumber());
        });
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
        AccountController controller = loader.getController();
        controller.setAccount(number);
        pane.setCenter(fxmlInstance);
    }

    @FXML
    void createAccount(){
        String accountName = newAccountNameInput.getText();
        String accountOwner = LoginController.getUser().getSocialNumber();
        DB.createAccount(accountName, accountOwner);
        accountOverview.getChildren().clear();
        generateAccounts();
        generateChoiceBox();
    }

    @FXML
    void deleteAccount(long accountNumber){
        DB.deleteAccount(accountNumber);
        accountOverview.getChildren().clear();
        transactionFromList.getItems().clear();
        generateChoiceBox();
        generateAccounts();
    }

    private void deleteAccountPopup(long accountNumber, String accountName, String accountType, double balance) {
            if(ValidationHelper.validateDeleteAccount(accountType, balance)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Radera konto?");
            alert.setHeaderText("Vill du verkligen radera " + accountName + "?");

            ButtonType btnYes = new ButtonType("Ja");
            ButtonType btnNo = new ButtonType("Nej");

            alert.getButtonTypes().setAll(btnYes, btnNo);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()) {
                if (result.get() == btnYes) {
                    deleteAccount(accountNumber);
                }
            }
        }
    }

    private void renameAccountPopup(long accountNumber, String accountName) {
        TextInputDialog dialog = new TextInputDialog("Ange namn på konto");
        dialog.setTitle("Byt namn");
        dialog.setHeaderText("Byt namn på " + accountName);
        dialog.setContentText("Nytt namn");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Ok");
        ((Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Avbryt");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if(ValidationHelper.validateName(dialog.getEditor().getText())){
            renameAccount(dialog.getEditor().getText(), accountNumber);
        }
        }
    }

    @FXML
    void renameAccount(String accountName, long accountNumber){
        DB.renameAccount(accountName, accountNumber);
        accountOverview.getChildren().clear();
        generateAccounts();
    }

    @FXML void loadLatestTransactions(){
        List<Transaction> transactions =  DB.getFiveLatestTransactions(LoginController.getUser().getSocialNumber());
        displayTransaction(transactions, account.getAccountName());
    }

    void displayTransaction(List<Transaction> transactions, String accountName){
        //For every transaction, do the following:
        for (Transaction transaction : transactions)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource( "/app/transaction/transaction.fxml"));
                Parent fxmlInstance = loader.load();
                Scene scene = new Scene( fxmlInstance );
                TransactionController controller = loader.getController();
                controller.setTransaction(transaction, account.getAccountNumber(), accountName);
                fiveTransactionsBox.getChildren().add(scene.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
