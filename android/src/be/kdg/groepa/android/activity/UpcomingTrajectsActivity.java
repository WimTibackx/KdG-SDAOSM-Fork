package be.kdg.groepa.android.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcomingtrajects);
        this.adapter = new UpcomingTrajectsViewAdapter((LayoutInflater)super.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        super.setListAdapter(this.adapter);
        new UpcomingTrajectsCall(getApplicationContext(), this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof UpcomingTrajectsCall) {
            this.trajects = ((UpcomingTrajectsCall)observable).getResultDtos();
            this.adapter.addItems(this.trajects);
        }
    }
}