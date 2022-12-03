package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.db.DatabaseHelper;

public class PersistentAccountDAO implements AccountDAO {
    private final DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public PersistentAccountDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumbersList = new ArrayList<String>();

        // Prepare query to fetch all account numbers
        String[] columns = new String[]{DatabaseHelper.ACCOUNT_NO};
        Cursor cursor = db.query(DatabaseHelper.ACCOUNT_TABLE, columns, null, null, null, null, null);

        // Iterate through each record and add to the accountNumbersList
        while (cursor != null && cursor.moveToNext()) {
            String accountNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_NO));
            accountNumbersList.add(accountNumber);
        }

        // Close the cursor middleware
        assert cursor != null;
        cursor.close();

        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<Account>();

        // Prepare query to fetch all accounts
        String[] columns = new String[]{DatabaseHelper.ACCOUNT_NO, DatabaseHelper.BANK_NAME, DatabaseHelper.ACCOUNT_HOLDER_NAME, DatabaseHelper.BALANCE};
        Cursor cursor = db.query(DatabaseHelper.ACCOUNT_TABLE, columns, null, null, null, null, null);

        // Iterate through records, create account object and add to accountsList
        while (cursor != null && cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_NO));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.BANK_NAME));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_HOLDER_NAME));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.BALANCE));

            Account account = new Account(accountNo, bankName, accountHolderName, balance);
            accountsList.add(account);
        }

        // Close the cursor middleware
        assert cursor != null;
        cursor.close();

        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = null;

        // Prepare query to fetch a specific accounts
        String[] columns = new String[]{DatabaseHelper.ACCOUNT_NO, DatabaseHelper.BANK_NAME, DatabaseHelper.ACCOUNT_HOLDER_NAME, DatabaseHelper.BALANCE};
        Cursor cursor = db.query(DatabaseHelper.ACCOUNT_TABLE, columns, DatabaseHelper.ACCOUNT_NO + " = ?", new String[]{accountNo}, null, null, null);

        // Check if account exists in db
        if (cursor != null) {
            cursor.moveToFirst();

            // Create account object
            account = new Account(accountNo,
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.BANK_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_HOLDER_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.BALANCE)));
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();

        // Store values to be inserted in contentValues
        contentValues.put(DatabaseHelper.ACCOUNT_NO, account.getAccountNo());
        contentValues.put(DatabaseHelper.BANK_NAME, account.getBankName());
        contentValues.put(DatabaseHelper.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        contentValues.put(DatabaseHelper.BALANCE, account.getBalance());

        // Insert account into db
        db.insert(DatabaseHelper.ACCOUNT_TABLE, null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        // Check if account exists and delete it from db
        Account account = getAccount(accountNo);
        if (account != null) {
            db.delete(DatabaseHelper.ACCOUNT_TABLE, DatabaseHelper.ACCOUNT_NO + " = ?", new String[]{accountNo});
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        // Check if account exists and fetch it from db
        Account account = getAccount(accountNo);
        if (account != null) {
            double currentBalance = account.getBalance();
            double finalBalance = currentBalance;

            // Specific implementation based on the transaction type
            switch (expenseType) {
                case EXPENSE:
                    finalBalance = currentBalance - amount;
                    break;
                case INCOME:
                    finalBalance = currentBalance + amount;
                    break;
            }

            // Update balance for account in db
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.BALANCE, finalBalance);
            db.update(DatabaseHelper.ACCOUNT_TABLE, contentValues, DatabaseHelper.ACCOUNT_NO + " = ?", new String[]{accountNo});
        }
    }
}
