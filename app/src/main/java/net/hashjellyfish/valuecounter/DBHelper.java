package net.hashjellyfish.valuecounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.hashjellyfish.valuecounter.VBContract.VBEntry;

public class DBHelper extends SQLiteOpenHelper {

    private static final int dbVersion = 1;
    private static final String VALUE_BUNDLES_FILENAME = "valueBundles.sqlite";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE "+ VBEntry.TABLE_NAME + " (" +
            VBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + VBEntry.COLUMN_NAME_POSITION +
            " INTEGER," + VBEntry.COLUMN_NAME_CAPTION + " TEXT," + VBEntry.COLUMN_NAME_VALUE +
            " INTEGER," + VBEntry.COLUMN_NAME_OP1 + " TEXT," + VBEntry.COLUMN_NAME_OP2 + " TEXT," +
            VBEntry.COLUMN_NAME_OP3 + " TEXT," + VBEntry.COLUMN_NAME_LOG_LENGTH + " INTEGER,"
            + VBEntry.COLUMN_NAME_LOG + " TEXT)";

    DBHelper(Context context) {
        super(context, VALUE_BUNDLES_FILENAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
