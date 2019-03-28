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
        PreparedStatement ps = prep("SELECT * from transaction WHERE from_account = ? OR to_account = ? ORDER BY date DESC LIMIT ? OFFSET ?");
        try {
            ps.setLong(1, accountId);
            ps.setLong(2,accountId);
            ps.setInt(3,limit);
            ps.setInt(4, offset);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;

    }

    public static List<Transaction> getFiveLatestTransactions(String accountId){
        List<Transaction> result = null;
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call list_latest_five_transactions(?)}");
            cstmt.setString(1, accountId);
            result = (List<Transaction>) (List<?>)new ObjectMapper<>(Transaction.class).map(cstmt.executeQuery());
        }   catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void makeTransaction(long fromAccount, long toAccount, float amount, String message){
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call create_transaction(?,?,?,?)}");
            cstmt.setLong(1, fromAccount);
            cstmt.setLong(2,toAccount);
            cstmt.setFloat(3,amount);
            cstmt.setString(4,message);
            cstmt.execute();
        }   catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void makeCardTransaction(long fromAccount){
        System.out.println("röv " + fromAccount);
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call create_transaction(?,?,?,?)}");
            cstmt.setLong(1,fromAccount);
            cstmt.setLong(2, 64574453L);
            cstmt.setFloat(3, 200.f);
            cstmt.setString(4, "Kortköp");
            cstmt.execute();
        }   catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void createAccount(String accountName, String accountOwner){
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call create_account(?,?)}");
            cstmt.setString(1, accountName);
            cstmt.setString(2, accountOwner);
            cstmt.executeUpdate();
        }   catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteAccount(long accountNumber){
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call delete_account(?)}");
            cstmt.setLong(1, accountNumber);
            cstmt.executeUpdate();
        }   catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void renameAccount(String accountName, long accountNumber){
        CallableStatement cstmt;
        try{
            cstmt = Database.getInstance().getConn().prepareCall("{call rename_account(?,?)}");
            cstmt.setString(1, accountName);
            cstmt.setLong(2, accountNumber);
            cstmt.executeUpdate();
        }   catch (SQLException e){
            e.printStackTrace();
        }
    }



}