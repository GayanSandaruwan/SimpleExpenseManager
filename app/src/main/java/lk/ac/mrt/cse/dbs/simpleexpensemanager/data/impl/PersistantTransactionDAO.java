package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Gayan Sandaruwan on 21-Nov-16.
 */

public class PersistantTransactionDAO implements TransactionDAO {  private SQLiteDatabase expenseDatabase;

    public PersistantTransactionDAO(SQLiteDatabase database){
        this.expenseDatabase = database;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO TransactionLog (Account_no,Type,Amt,Log_date) VALUES (?,?,?,?)";
        SQLiteStatement statement = expenseDatabase.compileStatement(sql);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor cursor = expenseDatabase.rawQuery("SELECT * FROM TransactionLog",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(cursor.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(cursor.getLong(cursor.getColumnIndex("Log_date"))),
                        cursor.getString(cursor.getColumnIndex("Account_no")),
                        (cursor.getInt(cursor.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("Amt")));
                transactions.add(t);
            }while (cursor.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor cursor = expenseDatabase.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(cursor.getLong(cursor.getColumnIndex("Log_date"))),
                        cursor.getString(cursor.getColumnIndex("Account_no")),
                        (cursor.getInt(cursor.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        cursor.getDouble(cursor.getColumnIndex("Amt")));
                transactions.add(t);
            } while (cursor.moveToNext());
        }

        return transactions;
    }
}
