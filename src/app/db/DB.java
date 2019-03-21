package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A Helper class for interacting with the Database using short-commands */
public abstract class DB {
    Map<String, Account> accounts = new HashMap<>();
    ResultSet resultSet = null;

    public static PreparedStatement prep(String SQLQuery){
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static Account getAccount(Long accountNumber) {
        Account accounts = null;
        PreparedStatement ps = prep("SELECT * FROM account WHERE account_number = ? ");
        try {
            ps.setLong(1, accountNumber);
            accounts = (Account) new ObjectMapper<>(Account.class).mapOne(ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public static List<?> getAccounts(String owner){
        List<?> accounts = null;
        PreparedStatement ps = prep("SELECT * FROM account WHERE owner = ?");
        try{
            ps.setString(1, owner);
            accounts = new ObjectMapper<>(Account.class).map(ps.executeQuery());
            } catch (Exception e){
            e.printStackTrace();
        }
        return accounts;
    }

    public static User getMatchingUser(String socialNumber, String password){
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM user WHERE social_number = ? AND password = ?");
        try {
            ps.setString(1, socialNumber);
            ps.setString(2, password);
            result = (User)new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }


    public static List<?> getTransactions(long accountId){ return getTransactions(accountId, 0, 10); }
    public static List<?> getTransactions(long accountId, int offset){ return getTransactions(accountId, offset, offset + 10); }
    public static List<?> getTransactions(long accountId, int offset, int limit){
        List<?> result = null;
        PreparedStatement ps = prep("SELECT * from transaction WHERE from_account = ? OR to_account = ? LIMIT ? OFFSET ?");
        try {
            ps.setLong(1, accountId);
            ps.setLong(2,accountId);
            ps.setInt(3,limit);
            ps.setInt(4, offset);
            result = new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }



}