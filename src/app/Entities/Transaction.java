package app.Entities;


import app.annotations.Column;

import java.sql.Time;
import java.sql.Timestamp;
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
    @Column
    private String message;


    public String getMessage() { return message; }
    public float getAmount() { return amount; }
    public Timestamp getDate() { return date; }

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
