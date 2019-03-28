package app.Entities;


import app.annotations.Column;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Transaction {
    @Column
    private long id;
    @Column
    private Timestamp date;
    @Column("from_account")
    private long fromAccount;
    @Column("to_account")
    private long toAccount;
    @Column
    private float amount;
    @Column("account_name")
    private String accountName;
    @Column
    private String message = null;


    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                '}';
    }
}
