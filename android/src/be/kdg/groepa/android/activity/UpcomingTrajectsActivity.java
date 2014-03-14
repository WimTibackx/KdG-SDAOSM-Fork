package be.kdg.groepa.android.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import be.kdg.groepa.android.*;
import be.kdg.groepa.android.dto.UpcomingTrajectDto;
import be.kdg.groepa.android.helper.UpcomingTrajectsViewAdapter;
import be.kdg.groepa.android.task.ConfirmRideTask;
import be.kdg.groepa.android.task.SetMessageReadTask;
import be.kdg.groepa.android.task.apicall.UpcomingTrajectsCall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class UpcomingTrajectsActivity extends ListActivity implements Observer, AsyncResponse {
    private List<UpcomingTrajectDto> trajects;
    private UpcomingTrajectsViewAdapter adapter;
    private View previousView;
    private Button btnSendMessage;
    private Button btnConfirmRide;
    private NumberPicker nmbrPickerMinutes;
    private NumberPicker nmbrPickerHours;
    UpcomingTrajectsViewAdapter.UpcomingTrajectCardView card;
    SharedPreferences privPref;
    private final int minMinutes = 5, maxMinutes = 60, stepMinutes = 5, minHours = 0, maxHours = 6, stepHours = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcomingtrajects);
        this.adapter = new UpcomingTrajectsViewAdapter((LayoutInflater) super.getSystemService(Context.LAYOUT_INFLATER_SERVICE), super.getApplicationContext());
        btnSendMessage = (Button) this.findViewById(R.id.btnMessagePassengers);
        btnConfirmRide = (Button) this.findViewById(R.id.btnConfirmRide);
        btnConfirmRide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirmRide();
            }
        });
        // Only set the confirmRide button to clickable when the user clicks a route that he is the driver of.
        btnConfirmRide.setClickable(false);
        // Half opacity when it's not one of the user's trajects
        btnConfirmRide.getBackground().setAlpha(128);
        // The SendMessage-button will only activate when a route is clicked, and its function will alter based on the user
        btnSendMessage.getBackground().setAlpha(128);
        btnSendMessage.setClickable(false);

        privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences", MODE_PRIVATE);
        super.setListAdapter(this.adapter);
        new UpcomingTrajectsCall(getApplicationContext(), this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof UpcomingTrajectsCall) {
            this.trajects = ((UpcomingTrajectsCall) observable).getResultDtos();
            this.adapter.addItems(this.trajects);
            this.findViewById(R.id.uptLoading).setVisibility(View.GONE);
            if (this.trajects.isEmpty()) {
                this.findViewById(R.id.uptNoneFound).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void processFinish(String response) {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        Intent goToMyActivity = new Intent(this, HomePageActivity.class);
        startActivity(goToMyActivity);
    }

    public void clickUptCard(View view) {
        btnSendMessage.getBackground().setAlpha(255);
        // Reset the previous selected card's background.
        if (previousView != null) {
            previousView.setBackgroundResource(R.color.black);
        }
        view.setBackgroundResource(R.color.blue);
        previousView = view;
        card = (UpcomingTrajectsViewAdapter.UpcomingTrajectCardView) view.getTag();
        // If the logged user is the driver of the traject, allow him to confirm a ride.
        if (card.getTraject().getChauffeurId() != privPref.getInt("UserId", -1)) {
            btnConfirmRide.setClickable(false);
            btnConfirmRide.getBackground().setAlpha(128);
            btnSendMessage.setText("Message driver");
            btnSendMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent goToMyActivity = new Intent(getApplicationContext(), SendMessageActivity.class);
                    Bundle b = new Bundle();
                    b.putString("receiverUsername", card.getTraject().getChauffeurUsername());
                    goToMyActivity.putExtras(b);
                    startActivity(goToMyActivity);
                }
            });

        } else {
            btnConfirmRide.setClickable(true);
            btnConfirmRide.getBackground().setAlpha(255);
            // Disabling messaging passengers because the functionality is not completely implemented.
            // btnSendMessage.setVisibility(View.INVISIBLE);
            btnSendMessage.setText("Message passengers");
            btnSendMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    System.out.println("Going to messagePassengers");
                    messagePassengers();
                }
            });
        }
    }

    public void messagePassengers() {
        JSONArray jsonUsernames = card.getTraject().getPassengerUsernames();
        JSONArray jsonRoutePlaces = card.getTraject().getRoutePlaces();
        Set<String> usernames = new HashSet<>(), places = new HashSet<>();
        // First get the required size of the arrays, then copy the filled data to them.
        try {
            for (int i = 0; i < jsonUsernames.length(); i++) {
                if (!jsonUsernames.getString(i).equals("null")) {
                    usernames.add(jsonUsernames.getString(i));
                }
            }
            for (int i = 0; i < jsonRoutePlaces.length(); i++) {
                if (!jsonRoutePlaces.getString(i).equals("null")) {
                    places.add(jsonRoutePlaces.getString(i));
                }
            }
        } catch (JSONException e) {
            System.out.println("JSONEXCEPTION WHEN SIZING: " + e.getMessage());
        }
        SharedPreferences privPref = getApplicationContext().getSharedPreferences("CarpoolPreferences",MODE_PRIVATE);
        SharedPreferences.Editor privPrefEditor = privPref.edit();
        System.out.println("SET USERNAMES: " + usernames);
        System.out.println("SET PLACES: " + places);
        privPrefEditor.putStringSet("usernames", usernames);
        privPrefEditor.putStringSet("places", places);
        privPrefEditor.commit();

        Intent goToMyActivity = new Intent(this, SendRideStatusActivity.class);
        startActivity(goToMyActivity);

    }

    public void confirmRide() {
        UpcomingTrajectDto traj = card.getTraject();
        System.out.println("PICKUPTIME: " + traj.getPickupTime());
        int year = traj.getRouteYear();
        int month = traj.getRouteMonth();
        int day = traj.getRouteDay();
        int hour = traj.getRouteStartHour();
        int min = traj.getRouteStartMinute();
        JSONObject json = new JSONObject();
        try {
            json.put("year", year);
            json.put("month", month);
            json.put("day", day);
            json.put("hour", hour);
            json.put("minute", min);
        } catch (JSONException e) {
        }
        ConfirmRideTask task = new ConfirmRideTask(traj.getRouteId(), year, month, day, hour, min, getApplicationContext(), this);
        task.execute();
    }
}