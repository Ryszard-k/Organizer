package edu.psm.projekt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Klasa aktywności odpowiedzialna za stworzenie nowego wydarzenia
 */
public class ViewDialog extends Activity {

    /**
     * Referencje do klas
     * @see MainActivity
     * @see DBoptions
     */
    private MainActivity mainActivity;
    private DBoptions dbOptions;

    /**
     * Metoda onCreate podpinająca layout i kontrolki z niego
     * @param saveInstanceState
     */
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.dialog_layout);

        final EditText NameEvent = findViewById(R.id.NameEvent);
        final EditText Description = findViewById(R.id.Describe_text);
        ImageButton SaveButton = findViewById(R.id.buttonSave);
        final TimePicker picker = findViewById(R.id.TimerPicker);
        ImageButton CancelButton = findViewById(R.id.CancelButton);

        /**
         * Interfejs nasłuchujący przycisk, sprawdzająca poprawność wprowadzonych danych, zaktualizowanie danego wydarzenia
         * oraz obliczenie czasu i uruchomienia intencji alarmManagera
         */
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NameEvent.getText().toString().equals("")){
                    Toast.makeText(ViewDialog.this, "Incorrectly data, try again", Toast.LENGTH_SHORT).show();}
                else {
                    mainActivity.dbOptions.InsertDB(mainActivity.selectedDate, picker.getCurrentHour().toString(),
                            picker.getCurrentMinute().toString(), NameEvent.getText().toString(), Description.getText().toString());
                    Toast.makeText(ViewDialog.this, "Event added", Toast.LENGTH_SHORT).show();

                    int Intent_id = (int) System.currentTimeMillis();

                    Intent notificationIntent = new Intent(ViewDialog.this, Notification.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ViewDialog.this, Intent_id, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

                    long time = 0;
                    long currentPicker = picker.getCurrentHour() *60 * 60 * 1000 + picker.getCurrentMinute()* 60 * 1000;
                    long currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * (60 * 60 * 1000) +
                            Calendar.getInstance().get(Calendar.MINUTE) * ( 60 * 1000) +
                            Calendar.getInstance().get(Calendar.SECOND) * 1000;
                    long currentDate = Calendar.getInstance().get(Calendar.MONTH) +
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +
                            Calendar.getInstance().get(Calendar.YEAR);
                    long liczba_dni = (mainActivity.thatDay.get(mainActivity.thatDay.MONTH) +
                            mainActivity.thatDay.get(mainActivity.thatDay.DAY_OF_MONTH) +
                            mainActivity.thatDay.get(mainActivity.thatDay.YEAR))- currentDate;
                    time = 24 * 60 * 60 * 1000 * liczba_dni + (currentPicker - currentHour);

                    AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
                    finish();
                }
            }
        });

        /**
         * Interfejs nasłuchujący przycisku odpowiedzialny za anulowanie operacji i zamknięcie okna
         */
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }
}
