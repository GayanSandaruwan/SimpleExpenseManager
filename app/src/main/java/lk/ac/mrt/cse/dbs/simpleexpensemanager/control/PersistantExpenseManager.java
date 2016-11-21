package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;

/**
 * Created by Gayan Sandaruwan on 21-Nov-16.
 */

public class PersistantExpenseManager extends ExpenseManager {
    private Context ctx;
    public PersistantExpenseManager(Context ctx){
        //Point the constructor to the setup function or our expense manager doesnt
        //get initialized
        this.ctx = ctx;
        setup();
    }
    @Override
    public void setup(){
        //First open an existing database or create new one
        //IMPORTANT DATABASE NAME SHOULD BE YOUR INDEX NUMBER
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("140106T", ctx.MODE_PRIVATE, null);

        //If it's the first time, we have to create the databases.
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Holder VARCHAR," +
                "Balance REAL" +
                " );");

        //DONOT create a database called Transaction
        //It is a reserved keyword and will give errors in queries
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");



        //These two functions will hold our DAO instances in memory till the program exists
        PersistantAccountDAO accountDAO = new PersistantAccountDAO(mydatabase);
        //accountDAO.addAccount(new Account("Account12","Sampath bank","Manujith",500));

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistantTransactionDAO(mydatabase));
    }
}
