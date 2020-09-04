package edu.psm.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Klasa zawierająca metody do obsługi stworzonej bazy danych
 */

public class DBoptions {

    /**
     * Referencje do klas
     * @see DBHelper
     * @see MainActivity
     * @see Notification
     * oraz referencje do bazy danych SQLiteDatabase
     */

    private DBHelper dbHelper;
    private static SQLiteDatabase database;
    private static MainActivity mainActivity;
    private static Notification notification;

    /**
     * Konstruktor inicjalizujący wartości w tym wypadku obiekt bazy danych
     * @param context Zmienna zawierająca contex klasy
     */
    public DBoptions(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Metoda pozwalająca na otwarcie i nadpisanie bazy danych
     * @throws SQLException Wyjątek zwracany w przypadku niemożności nadpisania bazy danych
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Metoda zamykająca baze danych
     */
    public void close(){
        database.close();
    }

    /**
     * Metoda pozwalająca na wstawienie wartości do stworzonej bazy danych
     * @param Date Zmienna odpowiadająca za dane wprowadzane do kolumny z datami
     * @param Hour Zmienna odpowiadająca za dane wprowadzane do kolumny z godzinami
     * @param Minute Zmienna odpowiadająca za dane wprowadzane do kolumny z minutami
     * @param Name Zmienna odpowiadająca za dane wprowadzane do kolumny z nazwami wydarzeń
     * @param Description Zmienna odpowiadająca za dane wprowadzane do kolumny z opisami wydarzeń
     */
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

    /**
     * Metoda pozwalająca na zaktualizowanie danych w bazie danych
     * @param Date Zmienna odpowiadająca za dane aktualizowane w kolumnie z datami
     * @param Hour Zmienna odpowiadająca za dane aktualizowane w kolumnie z godzinami
     * @param Minute Zmienna odpowiadająca za dane aktualizowane w kolumnie z minutami
     * @param Name Zmienna odpowiadająca za dane aktualizowane w kolumnie z nazwami wydarzeń
     * @param Description Zmienna odpowiadająca za dane aktualizowane w kolumnie z opisami wydarzeń
     */

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

    /**
     * Metoda pozwalająca na usunięcie danego wiersza tabeli bazy danych
     * @param position Wartość opisująca parametr id po którym jest wyszukiwana pozycja usuwanego wiersza
     */

    public void deleteData(String position){
        try {
        database.delete(DBHelper.DB_TABLE, DBHelper.Col0 + "=" + position, null);
        }catch (SQLException e){
            Log.i("DB delete error", e.getMessage());
        }
    }

    /**
     * Metoda typu Cursor pozwalająca na pobranie danych z bazy danych za pomocą Cursora. Dane wyszukiwane są tutaj
     * poprzez porównanie daty wybranej na kalendarzu a dacie wprowadzonego wydarzenia w bazie danych
     * @return Zwraca wartość typu Cursor
     */
    public static Cursor getData() {
        String where = DBHelper.Col1+"=?";
        String[] arg = {mainActivity.selectedDate};
        return database.query(DBHelper.DB_TABLE, null, where, arg, null,
                null, DBHelper.Col2);
    }

    /**
     * Metoda typu Cursor pozwalająca na pobranie danych z bazy danych za pomocą Cursora. Dane wyszukiwane są tutaj
     * poprzez porównanie obecnej daty z datą wydarzenia, obecnej godziny oraz minuty z czasem wydarzenia w bazie danych
     * @return Zwraca wartość typu Cursor
     */
    public static Cursor getDataToNotification() {
        String where = DBHelper.Col1+"=?" + " AND "+ DBHelper.Col2+"=?" + " AND " + DBHelper.Col3 + "=?";
        String[] arg = {notification.currentDay, notification.hour, notification.minute};
        return database.query(DBHelper.DB_TABLE, null, where, arg, null,
                null, DBHelper.Col2);
    }
}
