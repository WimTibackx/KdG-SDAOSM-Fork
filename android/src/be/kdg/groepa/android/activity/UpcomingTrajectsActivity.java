package be.kdg.groepa.android.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.dto.UpcomingTrajectDto;
import be.kdg.groepa.android.helper.UpcomingTrajectsViewAdapter;
import be.kdg.groepa.android.task.apicall.UpcomingTrajectsCall;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class UpcomingTrajectsActivity extends ListActivity implements Observer {
    private List<UpcomingTrajectDto> trajects;
    private UpcomingTrajectsViewAdapter adapter;
    private View previousView;
    private Button btnSendMessage;
    private Button btnConfirmRide;
    UpcomingTrajectsViewAdapter.UpcomingTrajectCardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcomingtrajects);
        this.adapter = new UpcomingTrajectsViewAdapter((LayoutInflater)super.getSystemService(Context.LAYOUT_INFLATER_SERVICE), super.getApplicationContext());
        btnSendMessage = (Button) this.findViewById(R.id.login);
        btnConfirmRide = (Button) this.findViewById(R.id.login);
        super.setListAdapter(this.adapter);
        new UpcomingTrajectsCall(getApplicationContext(), this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof UpcomingTrajectsCall) {
            this.trajects = ((UpcomingTrajectsCall)observable).getResultDtos();
            this.adapter.addItems(this.trajects);
            this.findViewById(R.id.uptLoading).setVisibility(View.GONE);
            if (this.trajects.isEmpty()) {
                this.findViewById(R.id.uptNoneFound).setVisibility(View.VISIBLE);
            }
        }
    }

    public void clickUptCard(View view) {
        System.out.println("CONSOLE: CLICKED ROUTE");
        // Reset the previous selected card's background.
        if(previousView != null){
            previousView.setBackgroundResource(R.color.white);
        }
        view.setBackgroundResource(R.color.black);
        previousView = view;
        card = (UpcomingTrajectsViewAdapter.UpcomingTrajectCardView)view.getTag();
        Toast.makeText(super.getApplicationContext(), "Clicked route "+card.getTraject().getId(), Toast.LENGTH_LONG).show();
    }

    public void messagePassengers(){

    }

    public void confirmRide(){

    }
}