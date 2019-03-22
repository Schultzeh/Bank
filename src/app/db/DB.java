package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    public static List<Transaction> getTransactions(long accountId){ return getTransactions(accountId, 0, 10); }
    public static List<Transaction> getTransactions(long accountId, int offset){ return getTransactions(accountId, offset, offset + 10); }
    public static List<Transaction> getTransactions(long accountId, int offset, int limit){
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * from transaction WHERE from_account = ? OR to_account = ? LIMIT ? OFFSET ?");
        try {
            ps.setLong(1, accountId);
            ps.setLong(2,accountId);
            ps.setInt(3,limit);
            ps.setInt(4, offset);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }

    public static void makeTransaction(long fromAccount, long toAccount, float amount, String message){
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call create_transaction(?,?,?,?)}");
            cstmt.setLong(1, fromAccount);
            cstmt.setLong(2,toAccount);
            cstmt.setFloat(3,amount);
            cstmt.setString(4,message);
            cstmt.executeUpdate();
        }   catch(SQLException e){
            e.printStackTrace();
        }
    }



}