package edu.psm.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBoptions {

    private DBHelper dbHelper;
    private static SQLiteDatabase database;
    private static MainActivity mainActivity;
    private static Notification notification;

    public DBoptions(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void InsertDB(String Date, String Hour, String Minute, String Name, String Description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.Col1, Date);
        contentValues.put(dbHelper.Col2, Hour);
        contentValues.put(dbHelper.Col3, Minute);
        contentValues.put(dbHelper.Col4, Name);
        contentValues.put(dbHelper.Col5, Description);
        try {
            database.insert(DBHelper.DB_TABLE, null, contentValues);
        }catch (SQLException e){
            Log.i("DB write error", e.getMessage());
        }
    }

    public void updateDB(String Date, String Hour, String Minute, String Name, String Description, String position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.Col1, Date);
        contentValues.put(dbHelper.Col2, Hour);
        contentValues.put(dbHelper.Col3, Minute);
        contentValues.put(dbHelper.Col4, Name);
        contentValues.put(dbHelper.Col5, Description);
        try {
            database.update(DBHelper.DB_TABLE, contentValues, DBHelper.Col0 + "=" + position, null);
        }catch (SQLException e){
            Log.i("DB update error", e.getMessage());
        }
    }

    public void deleteData(String position){
        try {
        database.delete(DBHelper.DB_TABLE, DBHelper.Col0 + "=" + position, null);
        }catch (SQLException e){
            Log.i("DB delete error", e.getMessage());
        }
    }

    public static Cursor getData() {
        String where = DBHelper.Col1+"=?";
        String[] arg = {mainActivity.selectedDate};
        return database.query(DBHelper.DB_TABLE, null, where, arg, null,
                null, DBHelper.Col2);
    }

    public static Cursor getDataToNotification() {
        String where = DBHelper.Col1+"=?" + " AND "+ DBHelper.Col2+"=?" + " AND " + DBHelper.Col3 + "=?";
        String[] arg = {notification.currentDay, notification.hour, notification.minute};
        return database.query(DBHelper.DB_TABLE, null, where, arg, null,
                null, DBHelper.Col2);
    }
}
