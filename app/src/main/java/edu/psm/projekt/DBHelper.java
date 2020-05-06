package edu.psm.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static edu.psm.projekt.MainActivity.selectedDate;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DB1";
    public static final String DB_TABLE = "EventCalendar";
    private static final int DB_VERSION = 3;
    public static final String Col0 = "ID";
    public static final String Col1 = "DATE";
    public static final String Col2 = "HOUR";
    public static final String Col3 = "MINUTE";
    public static final String Col4 = "NAME";
    public static final String Col5 = "DESCRIPTION";

    private static final String Create_table =  "CREATE TABLE " + DB_TABLE +
            "(" + Col0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + Col1 +
            " TEXT," +Col2 + " TEXT, " + Col3 + " TEXT," + Col4 + " TEXT," + Col5+ " TEXT" + ")";

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Create_table);
        }catch (SQLException ex){
            Log.i("SQLite error", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }catch (SQLException ex){
            Log.i("SQLite error", ex.getMessage());
        }
    }
}

