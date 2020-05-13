package edu.psm.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RVAdapter.IItemClickListener{

    private CalendarView calendar;
    public static String selectedDate;
    public static RVAdapter adapter;
    protected RecyclerView recyclerView;
    public static DBoptions dbOptions;
    public static String deleteId;
    public static Calendar thatDay;

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

        selectedDate = String.valueOf(calendar.getDate());
        thatDay = Calendar.getInstance();

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

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewDialog.class);
                startActivity(intent);
            }
        });

        multiEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MultiAdds.class);
                startActivity(intent);
            }
        });

        adapter = new RVAdapter(this, dbOptions.getData());
        adapter.setClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int resId = R.anim.layout_animation;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
    }

    private void runLayoutAnimation(){
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

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
