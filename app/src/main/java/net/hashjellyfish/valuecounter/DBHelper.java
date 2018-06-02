package net.hashjellyfish.valuecounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int dbVersion = 1;
    private static final String VALUE_BUNDLES_FILENAME = "valueBundles.sqlite";

    public DBHelper(Context context) {
        super(context, VALUE_BUNDLES_FILENAME, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE Bundles(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "position INTEGER, caption TEXT, value INTEGER, op1Type TEXT, op1Amount INTEGER, " +
                "op2Type TEXT, op2Amount INTEGER, op3Type TEXT, op3Amount INTEGER)";
        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
