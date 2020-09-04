package edu.psm.projekt;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

/**
 * Klasa odpowiedzialna za przygotowanie powiadomienia
 */
public class Notification extends BroadcastReceiver {

    /**
     * Referencja do klasy
     * @see DBoptions
     * oraz referencje do zmiennych Cursora oraz obecnej daty, godziny oraz minuty
     */
    private static DBoptions dbOptions;
    private Cursor nData;
    private String nEvent;
    private String dEvent;
    public static String currentDay;
    public static String hour;
    public static String minute;

    /**
     * Kontruktor pobierający obecną date i czas
     */
    public Notification(){
        currentDay = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) +
                        Integer.toString(Calendar.getInstance().get(Calendar.MONTH))+
                        Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        System.out.println(hour);
        System.out.println(minute);
    }

    /**
     * Metoda onReceive służąca do zbudowania przypyomnienia i uruchomienia go z poziomu wyłączonej aplikacji
     * @param context Obecny context
     * @param intent Intencja w tym wypadku "wypchanie" powiadomienia
     * Pobiera dane z bazy a następnie buduje powiadomienie
     */
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