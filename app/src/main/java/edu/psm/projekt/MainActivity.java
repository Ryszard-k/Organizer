package edu.psm.projekt;

/**
 * @author Piotr Wójcik
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

import java.util.HashMap;

/**
 * Klasa głównej aktywności odpowiadająca za pierwszy widok okna aplikacji i jego obsługę
 */
public class MainActivity extends AppCompatActivity implements RVAdapter.IItemClickListener{

    /**
     * Referencje do klasy
     * @see RVAdapter
     * @see DBoptions
     * oraz referencje do widgetu Calendar oraz Recycler View.
     * Deklaracja referencji do wybieranej daty, usuwanej daty oraz obecnej daty
     */
    private CalendarView calendar;
    public static String selectedDate;
    public static RVAdapter adapter;
    protected RecyclerView recyclerView;
    public static DBoptions dbOptions;
    public static String deleteId;
    public static Calendar thatDay;


    /**
     * Metoda onCreate podpinająca layout głównego okna aplikacji, referencje do kontrolek layoutu oraz widgetów,
     * tworzenie obiektu bazy danych oraz otwarcie jej, nadanie wartości wybieranej dacie i pobranie obecnej daty
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.RecyclerView);
        ImageButton addEvent = findViewById(R.id.buttonPlus);
        Button multiEvent = findViewById(R.id.multiInsert);
        createNotificationChannel();

        dbOptions = new DBoptions(this);
        dbOptions.open();

        thatDay = Calendar.getInstance();

        if(selectedDate == null){
            selectedDate = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) +
                    Integer.toString(Calendar.getInstance().get(Calendar.MONTH)) +
                    Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }

        /**
         * Interfejs odpowiedzialny za nasłuchanie zmiany daty na widgecie Calendar oraz przypisanie wartości zmienionej daty
         * @param selectedDate
         * oraz przypisanie obecnej daty zmiennej
         * @param thatDay
         * wyświetlenie danych odczytanych z bazy danych oraz uruchomienie animacji
         */
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
                thatDay.set(Calendar.YEAR, year);
                thatDay.set(Calendar.MONTH, month);
                thatDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                adapter.swapCursor(dbOptions.getData());
                runLayoutAnimation();

            }
        });

        /**
         * Interfejs nasłuchujący przycisk odpowiedzialny za uruchomienie aktywności klasy
         * @see ViewDialog
         */
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewDialog.class);
                startActivity(intent);
            }
        });

        /**
         * Interfejs nasłuchujący przycisk odpowiedzialny za uruchomienie aktywności klasy
         * @see MultiAdds
         */
        multiEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MultiAdds.class);
                startActivity(intent);
            }
        });

        /**
         * Utworzenie obiektu adaptera, nasłuchanie kliknięcia na jego obiekty oraz ustawienia widgetu Recycler View
         * odnośnie wielkości, managera layoutu i podpięcie adaptera.
         * Przypisanie layoutu animacji a następnie stworzenie obiektu jej i podpięcie do widgetu Recycler View
         */
        adapter = new RVAdapter(this, dbOptions.getData());
        adapter.setClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int resId = R.anim.layout_animation;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);

        adapter.swapCursor(dbOptions.getData());
        runLayoutAnimation();
    }

    /**
     * Metoda odpowiedzialna za przebieg animacji
     */
    public void runLayoutAnimation(){
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Interfejs nasłuchania kliknięcia na danych obiekcie Recycler View
     * @param view Bierzący widok
     * @param position Pozycja klikniętego obiektu Recycler View
     * Następnie pobranie danych z bazy danych użytych w klikniętym obiekcie Recycler View, przypisanie id któy ma posłużyć do usunięcia
     *           danego wiersza, stworzenie Alert Dialogu w którym możemy albo usunąć dane wydarzenie, uruchomić okno edycji wydarzenia
     * @see Update
     * lub wyłączenia okna alertowego
     */
    @Override
    public void onItemCLick(View view, int position) {
        HashMap<String, String> item = adapter.getItem(position);
        deleteId = item.get("ID");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to update?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dbOptions.deleteData(deleteId);
                        Toast.makeText(MainActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                        adapter.swapCursor(dbOptions.getData());
                    }
                });

        alertDialogBuilder.setNegativeButton("Update",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, Update.class);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    /**
     * Prywatna metoda odpowiedzialna za stworzenie kanału do wyświetlania powiadomień
     */

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_id";
            String description = "remainder channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
