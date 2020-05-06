package edu.psm.projekt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class Update extends Activity {

    private MainActivity mainActivity;
    private DBoptions dbOptions;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.dialog_layout);

        final EditText NameEvent = findViewById(R.id.NameEvent);
        final EditText Description = findViewById(R.id.Describe_text);
        Button SaveButton = findViewById(R.id.buttonSave);
        final TimePicker picker = findViewById(R.id.TimerPicker);
        Button CancelButton = findViewById(R.id.CancelButton);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NameEvent.getText().toString().equals("")){
                    Toast.makeText(Update.this, "Incorrectly data, try again", Toast.LENGTH_SHORT).show();}
                else {
                    mainActivity.dbOptions.updateDB(mainActivity.selectedDate, picker.getCurrentHour().toString(),
                            picker.getCurrentMinute().toString(), NameEvent.getText().toString(), Description.getText().toString(), mainActivity.deleteId);
                    mainActivity.adapter.swapCursor(dbOptions.getData());
                    Toast.makeText(Update.this, "Event updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
    }
}
