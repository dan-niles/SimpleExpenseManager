package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

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
        while(cursor != null && cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(dbHelper.ACCOUNT_NO));
            accountNumbersList.add(accountNumber);
        }

        // Close the cursor middleware
        cursor.close();

        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return null;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

    }
}
