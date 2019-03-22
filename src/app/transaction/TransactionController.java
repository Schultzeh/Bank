package app.transaction;


import app.Entities.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.PopupWindow;
import javafx.util.Duration;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;
    @FXML Label toOrFrom;
    @FXML ImageView messageImage;


    @FXML
    private void initialize(){
        System.out.println("initialize transaction");
    }

    public void setTransaction(Transaction transaction) {
        date.setText("" + transaction.getDate());
        amount.setText("" + transaction.getAmount());
        messageImage.setVisible(false);
        if(transaction.getMessage() != null) {
            Tooltip tt = new Tooltip(transaction.getMessage());
            tt.setShowDelay(Duration.seconds(0.1));
            tt.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_RIGHT);
            messageImage.setVisible(true);
            message.setTooltip(tt);

        }
        toOrFrom.setText(""+ transaction.getFromAccount());
    }
}
