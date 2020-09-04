package edu.psm.projekt;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Klasa iinicjująca i opisująca bazę danych
 */

public class DBHelper extends SQLiteOpenHelper {

    /**
     * Zmienne opisujące
     * @param DB_NAME  Nazwa bazy danych
     * @param DB_TABLE Nazwa tabeli
     * @param DB_VERSION Wersja bazy danych
     * @param Col0 Nazwa kolumny zawierająca id
     * @param Col1 Nazwa  kolumny zawierająca date
     * @param Col2 Nazwa kolumny zawierająca godzine
     * @param Col3 Nazwa kolumny zawierająca minuty
     * @param Col4 Nazwa kolumny zawierająca nazwę wydarzenia
     * @param Col5 Nazwa kolumny zawierająca opis wydarzenia
     * @param Create_table Zmienna zawierająca polecenie SQL razem ze zmiennymi potrzebnymi do zainicjalizowania bazy
     */
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

    /**
     * Kontruktor inicjalizujący wersję bazy danych
     * @param context Zmienna zawierająca contex klasy
     */

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Metoda nadpisana onCreate inicjalizująca bazę danych
     * @param db Zmienna wykorzystywana do zainicjalizowania bazy danych
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Create_table);
        }catch (SQLException ex){
            Log.i("SQLite error", ex.getMessage());
        }
    }

    /**
     * Metoda nadpisana pozwalająca na stworzenie nowej wersji bazy danych
     * @param db Zmienna wykorzystywana do zainicjalizowania bazy danych
     * @param oldVersion Zmienna reprezentująca id starej wersji bazy danych
     * @param newVersion Zmienna reprezentująca id nowej wersji bazy danych
     */
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

