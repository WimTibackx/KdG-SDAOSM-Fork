package be.kdg.groepa.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.Button;
import android.widget.RadioButton;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.SendMessageActivity;

/**
 * Created by Tim on 27/02/14.
 */
public class SendRideStatusActivity extends Activity implements AsyncResponse {
    private RadioGroup radioGroup;
    private Button submitButton;
    private String[] passengerUsernames;
    private String[] placeNames;
    private NumberPicker nmbrPickerMinutes;
    private NumberPicker nmbrPickerHours;
    private Spinner spnPlaces;
    private final int minMinutes = 5, maxMinutes = 60, stepMinutes = 5, minHours = 0, maxHours = 6, stepHours = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiateComponents(savedInstanceState);

    }

    public void submitMessage() {
        int id = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(id);
        int radioId = radioGroup.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
        String selection = (String) btn.getText();
        System.out.println("CONSOLE -- SELECTION: " + selection);
        switch (selection) {
            case "Time until next point":
                String msg = String.format("I will arrive at %s in %d hours and %d minutes.", spnPlaces.getSelectedItem().toString(), nmbrPickerHours.getValue(), nmbrPickerMinutes.getValue());
                for (String s : this.passengerUsernames) {
                    System.out.println("SENDING MESSAGE TO USER: " + s + " ; MESSAGE: " + msg);
                    messagePassenger(msg, s);
                }
                break;
            default:
                for (String s : this.passengerUsernames) {
                    System.out.println("SENDING MESSAGE TO USER: " + s + " ; MESSAGE: " + selection);
                    messagePassenger(selection, s);
                }
        }
    }

    private void messagePassenger(String msg, String receiver) {
        Intent goToMyActivity = new Intent(this, SendMessageActivity.class);
        Bundle b = new Bundle();
        b.putString("receiverUsername", receiver);
        b.putString("messageHeader", msg);
        b.putString("messageBody", msg);
        goToMyActivity.putExtras(b);
        startActivity(goToMyActivity);
    }

    @Override
    public void processFinish(String output) {
        // startActivity(goToMyActivity);
    }

    private void initiateComponents(Bundle savedInstances) {
        this.passengerUsernames = savedInstances.getStringArray("passengerUsernames");
        this.placeNames = savedInstances.getStringArray("routePlaces");

        setContentView(R.layout.sendridestatus);
        radioGroup = (RadioGroup) findViewById(R.id.rgRideStatus);
        final Button btnRideStatus = (Button) findViewById(R.id.buttonSendRideStatus);

        btnRideStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMessage();
            }
        });
        // Set clickable to false until one of the radiobuttons is clicked
        btnRideStatus.setClickable(false);
        btnRideStatus.getBackground().setAlpha(128);

        // Set clickable to false when one of the radiobuttons is clicked
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                o.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnRideStatus.setClickable(true);
                        btnRideStatus.getBackground().setAlpha(255);
                    }
                });
            }
        }

        // Set a ChangeListener to the RadioGroup so the necessary views get revealed when the appropriate RadioButtons are selected
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int newChecked) {
                RadioButton checked = (RadioButton) radioGroup.findViewById(newChecked);
                if (checked.getText().equals("Time until next point")) {
                    nmbrPickerHours.setVisibility(View.VISIBLE);
                    nmbrPickerMinutes.setVisibility(View.VISIBLE);
                    spnPlaces.setVisibility(View.VISIBLE);
                } else {
                    nmbrPickerHours.setVisibility(View.INVISIBLE);
                    nmbrPickerMinutes.setVisibility(View.INVISIBLE);
                    spnPlaces.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Set custom values for the sliders and set them invisible until the corresponding option is selected
        nmbrPickerMinutes = (NumberPicker) this.findViewById(R.id.nmbrMinutesLeft);
        nmbrPickerMinutes.setMinValue(minMinutes);
        nmbrPickerMinutes.setMaxValue(maxMinutes);
        String[] valuesMinutes = new String[maxMinutes / minMinutes];
        for (int i = minMinutes; i <= maxMinutes; i += stepMinutes) {
            valuesMinutes[(i / stepMinutes) - 1] = String.valueOf(i);
        }
        nmbrPickerMinutes.setDisplayedValues(valuesMinutes);
        nmbrPickerMinutes.setVisibility(View.INVISIBLE);

        nmbrPickerHours = (NumberPicker) this.findViewById(R.id.nmbrHoursLeft);
        nmbrPickerHours.setMinValue(minHours);
        nmbrPickerHours.setMaxValue(maxHours);
        String[] valuesHours = new String[maxHours +1];
        for (int i = minHours; i <= maxHours; i += stepHours) {
            valuesHours[(i / stepHours) - 1] = String.valueOf(i);
        }
        nmbrPickerHours.setDisplayedValues(valuesHours);
        nmbrPickerHours.setVisibility(View.INVISIBLE);

        // Fill the placespinner
        spnPlaces = (Spinner) this.findViewById(R.id.spnPlaces);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, placeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPlaces.setAdapter(adapter);
        spnPlaces.setVisibility(View.INVISIBLE);
    }
}
