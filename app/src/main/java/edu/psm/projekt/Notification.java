package edu.psm.projekt;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Notification extends BroadcastReceiver {

    private static DBoptions dbOptions;
    private Cursor nData;
    private String nEvent;
    private String dEvent;
    public static String currentDay;
    public static String hour;
    public static String minute;

    public Notification(){
        currentDay = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) +
                        Integer.toString(Calendar.getInstance().get(Calendar.MONTH))+
                        Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        System.out.println(hour);
        System.out.println(minute);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        nData = dbOptions.getDataToNotification();

            if (nData.getCount() > 0) {
                nData.moveToFirst();
                nEvent = nData.getString(nData.getColumnIndex("NAME"));
                dEvent = nData.getString(nData.getColumnIndex("DESCRIPTION"));

                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                        .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                        .setContentTitle(nEvent)
                        .setContentText(dEvent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManagerC = NotificationManagerCompat.from(context);
                notificationManagerC.notify(100, builder.build());
                nData.close();
            }else{
                nData.close();
            }
    }
}