package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Gayan Sandaruwan on 21-Nov-16.
 */

public class PersistantAccountDAO implements AccountDAO {   private SQLiteDatabase expenseDatabase;

    public PersistantAccountDAO(SQLiteDatabase database){
        this.expenseDatabase = database;
    }
    @Override
    public List<String> getAccountNumbersList() {
        Cursor cursor = expenseDatabase.rawQuery("SELECT Account_no FROM Account",null);
        List<String> accountNumbersList = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do {
                accountNumbersList.add(cursor.getString(cursor.getColumnIndex("Account_no")));
            } while (cursor.moveToNext());
        }
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor cursor = expenseDatabase.rawQuery("SELECT * FROM Account",null);
        List<Account> accountList = new ArrayList<Account>();


        if(cursor.moveToFirst()) {
            int bank = cursor.getColumnIndex("Bank");
            int holder=cursor.getColumnIndex("Holder");
            int balance=cursor.getColumnIndex("Balance");
            do {
                Account account = new Account(cursor.getString(cursor.getColumnIndex("Account_no")),
                        cursor.getString(bank),
                        cursor.getString(holder),
                        cursor.getDouble(balance));
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = expenseDatabase.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(cursor.moveToFirst()) {
            int bank = cursor.getColumnIndex("Bank");
            int holder=cursor.getColumnIndex("Holder");
            int balance=cursor.getColumnIndex("Balance");
            int accountno =cursor.getColumnIndex("Account_no");
            do {
                account = new Account(
                        cursor.getString(accountno),
                        cursor.getString(bank),
                        cursor.getString(holder),
                        cursor.getDouble(balance));
            } while (cursor.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = expenseDatabase.compileStatement(sql);

        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();



    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement statement = expenseDatabase.compileStatement(sql);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Balance = Balance + ? WHERE Account_no = ?";
        SQLiteStatement statement = expenseDatabase.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.bindString(2,accountNo);
        statement.executeUpdateDelete();
        //notifyAll();
    }
}
