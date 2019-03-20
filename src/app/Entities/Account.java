package app.Entities;

import app.annotations.Column;

public class Account {
    @Column("account_number")
    private int accountNumber;
    @Column("account_name")
    private String accountName;
    @Column
    private String owner;
    @Column("account_type")
    private String accountType;
    @Column
    private float balance;

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getOwner() {
        return owner;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountName='" + accountName + '\'' +
                ", owner='" + owner + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }
}
