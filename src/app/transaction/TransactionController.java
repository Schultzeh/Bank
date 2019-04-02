package app.transaction;


import app.Entities.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.PopupWindow;
import javafx.util.Duration;
import java.time.format.DateTimeFormatter;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;
    @FXML Label toAccount;
    @FXML Label fromAccount;
    @FXML ImageView messageImage;


    @FXML
    private void initialize(){
//        System.out.println("initialize transaction");
    }

    public void setTransaction(Transaction transaction, long number, String accountName) {
        date.setText("" + transaction.getDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        amount.setText(" " + transaction.getAmount());
        messageImage.setVisible(false);
        toAccount.setText("Till: "+ transaction.getToAccount());
        fromAccount.setText("Från: "+ transaction.getFromAccount());
        styleTransaction(transaction, number, accountName);
    }

    void styleTransaction(Transaction transaction, long number, String accountName){
        if(!transaction.getMessage().isEmpty()){
            Tooltip tt = new Tooltip(transaction.getMessage());
            tt.setShowDelay(Duration.seconds(0.1));
            tt.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_RIGHT);
            messageImage.setVisible(true);
            message.setTooltip(tt);
        }
        if(transaction.getFromAccount() == number){
            fromAccount.setText("Från: "+ accountName);
            amount.setText("-" + transaction.getAmount());
            amount.setStyle("-fx-text-fill: red;");
        }
        if (transaction.getToAccount() == number) {
            toAccount.setText("Till: " + accountName);
        }
    }
}