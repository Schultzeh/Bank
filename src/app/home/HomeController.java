package app.home;

import app.Entities.Account;
import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    Account account;

    List<Account> userAccounts = null;

    @FXML
    void initialize(){
        nameLabel.setText(LoginController.getUser().getFirstName());
        generateAccounts();
        generateChoiceBox();
        System.out.println(DB.getFiveLatestTransactions(LoginController.getUser().getSocialNumber()));
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
            accountDelete.setOnMouseClicked(event -> deleteAccountPopup(account.getAccountNumber(), account.getAccountName()));
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
    void makeTransaction(){
        String message = transactionMessage.getText();
        long fromAccount = Long.parseLong(transactionFromList.getValue().toString());
        long toAccount = Long.parseLong(transactionToAcc.getText());
        float amount = Long.parseLong(transactionAmount.getText());
        DB.makeTransaction(fromAccount, toAccount, amount, message);
        try {
            goToAccount(fromAccount);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML void makeCardTransaction(){
        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        userAccounts.forEach(account -> {
            if(account.getAccountType().equals("creditcard")){
                Long fromAccount = account.getAccountNumber();
                System.out.println(fromAccount);
                DB.makeCardTransaction(fromAccount);
            }
        });
    }

    @FXML
    void generateChoiceBox(){
        userAccounts = (List<Account>)DB.getAccounts(LoginController.getUser().getSocialNumber());
        userAccounts.forEach(account -> {
            System.out.println(account);
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
    }

    @FXML
    void deleteAccount(long accountNumber){
        DB.deleteAccount(accountNumber);
        accountOverview.getChildren().clear();
        generateAccounts();
    }

    private void deleteAccountPopup(long accountNumber, String accountName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Radera konto?");
        alert.setHeaderText("Vill du verkligen radera " + accountName + "?");

        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");

        alert.getButtonTypes().setAll(btnYes, btnNo);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnYes) {
                deleteAccount(accountNumber);
            } else if (result.get() == btnNo) {
                System.out.println("User clicked No!");
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
            renameAccount(dialog.getEditor().getText(), accountNumber);
            System.out.println(accountName + accountNumber);
        }
    }

    @FXML
    void renameAccount(String accountName, long accountNumber){
        DB.renameAccount(accountName, accountNumber);
        accountOverview.getChildren().clear();
        generateAccounts();
    }

    @FXML void loadLatestTransactions(){
        DB.getFiveLatestTransactions(LoginController.getUser().getSocialNumber());
    }
}
