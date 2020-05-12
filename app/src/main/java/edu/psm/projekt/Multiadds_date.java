package edu.psm.projekt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class Multiadds_date extends Activity/* implements MultiAdds.getUpdateListener*/{

    private CalendarView calendar2;
    private String insertingDates;
    private MainActivity mainActivity;
    private MultiAdds multiAdds;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.date);

        calendar2 = findViewById(R.id.calendarView2);
        ImageButton finish = findViewById(R.id.finishButton);



        insertingDates = String.valueOf(calendar2.getDate());

        calendar2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                insertingDates = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                mainActivity.dbOptions.InsertDB(insertingDates, multiAdds.hour, multiAdds.minute, multiAdds.name, multiAdds.description);
                Toast.makeText(Multiadds_date.this, "Event added", Toast.LENGTH_SHORT).show();

                Intent notificationIntent = new Intent(Multiadds_date.this, Notification.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Multiadds_date.this, 0, notificationIntent, 0);

                long time = 0;
                long currentPicker = multiAdds.Godzin *60 * 60 * 1000 + multiAdds.Minuta* 60 * 1000;
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

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

}
