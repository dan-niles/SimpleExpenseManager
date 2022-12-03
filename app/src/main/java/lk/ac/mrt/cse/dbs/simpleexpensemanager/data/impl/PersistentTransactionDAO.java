package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelper;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public PersistentTransactionDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValues = new ContentValues();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        // Store values to be inserted in contentValues
        contentValues.put(DatabaseHelper.DATE, df.format(date));
        contentValues.put(DatabaseHelper.ACCOUNT_NO, accountNo);
        contentValues.put(DatabaseHelper.EXPENSE_TYPE, String.valueOf(expenseType));
        contentValues.put(DatabaseHelper.AMOUNT, amount);

        // Insert account into db
        db.insert(DatabaseHelper.TRANSACTION_TABLE, null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionLogList = new ArrayList<Transaction>();

        // Prepare query to fetch all accounts
        String[] columns = new String[]{DatabaseHelper.ID, DatabaseHelper.DATE, DatabaseHelper.ACCOUNT_NO, DatabaseHelper.EXPENSE_TYPE, DatabaseHelper.AMOUNT};
        Cursor cursor = db.query(DatabaseHelper.TRANSACTION_TABLE, columns, null, null, null, null, null);

        // Iterate through records, create account object and add to accountsList
        while (cursor != null && cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_NO));

            String date_str = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE));
            Date date = null;
            try {
                date = new SimpleDateFormat("dd-MM-yyyy").parse(date_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EXPENSE_TYPE)));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.AMOUNT));

            Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
            transactionLogList.add(transaction);
        }

        // Close the cursor middleware
        assert cursor != null;
        cursor.close();

        return transactionLogList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionLogList = new ArrayList<Transaction>();

        // Prepare query to fetch all accounts
        String[] columns = new String[]{DatabaseHelper.ID, DatabaseHelper.DATE, DatabaseHelper.ACCOUNT_NO, DatabaseHelper.EXPENSE_TYPE, DatabaseHelper.AMOUNT};
        Cursor cursor = db.query(DatabaseHelper.TRANSACTION_TABLE, columns, null, null, null, null, null, String.valueOf(limit));

        // Iterate through records, create account object and add to accountsList
        while (cursor != null && cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_NO));

            String date_str = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE));
            Date date = null;
            try {
                date = new SimpleDateFormat("dd-MM-yyyy").parse(date_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EXPENSE_TYPE)));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.AMOUNT));

            Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
            transactionLogList.add(transaction);
        }

        // Close the cursor middleware
        assert cursor != null;
        cursor.close();

        return transactionLogList;
    }
}
