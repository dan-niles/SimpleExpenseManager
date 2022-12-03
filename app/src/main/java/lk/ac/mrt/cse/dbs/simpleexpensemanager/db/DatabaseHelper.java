package lk.ac.mrt.cse.dbs.simpleexpensemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "200421U.DB";
    static final int DATABASE_VERSION = 1;

    // Table names
    public static final String ACCOUNT_TABLE = "_account";
    public static final String TRANSACTION_TABLE = "_transaction";

    // Columns for "account" table
    public static final String BANK_NAME = "bankName";
    public static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String BALANCE = "balance";
    public static final String ACCOUNT_NO = "accountNo";

    // Columns for "transaction" table
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String EXPENSE_TYPE = "expenseType";
    public static final String AMOUNT = "amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for accounts
        db.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(" +
                        ACCOUNT_NO + " TEXT PRIMARY KEY, " +
                        BANK_NAME + " TEXT NOT NULL, " +
                        ACCOUNT_HOLDER_NAME + " TEXT NOT NULL, " +
                        BALANCE + " REAL NOT NULL)"
        );

        // Create table for transactions
        db.execSQL("CREATE TABLE " + TRANSACTION_TABLE + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DATE + " TEXT NOT NULL, " +
                        EXPENSE_TYPE + " TEXT NOT NULL, " +
                        AMOUNT + " REAL NOT NULL, " +
                        ACCOUNT_NO + " TEXT," +
                        "FOREIGN KEY (" + ACCOUNT_NO + ") REFERENCES " + ACCOUNT_TABLE + "(" + ACCOUNT_NO + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);
    }
}
