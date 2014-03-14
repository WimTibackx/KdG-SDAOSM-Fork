package be.kdg.groepa.android.task.apicall;

import android.content.Context;
import be.kdg.groepa.android.AsyncResponse;
import be.kdg.groepa.android.dto.UpcomingTrajectDto;
import be.kdg.groepa.android.task.HttpTask;
import be.kdg.groepa.android.task.HttpTaskUser;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class UpcomingTrajectsCall extends Observable implements HttpTaskUser {
    private List<UpcomingTrajectDto> resultDtos;

    public UpcomingTrajectsCall(Context context, Observer observer) {
        super();
        this.resultDtos = new ArrayList<>();
        super.addObserver(observer);
        System.out.println("UPTRJCALL: ADDED OBSERVER, CREATING HTTPTASK");
        HttpTask task = new HttpTask(context, true, "/authorized/traject/myupcoming", this);
        System.out.println("TASK CREATED, EXECUTING");
        task.execute();
    }

    @Override
    public void responseSuccess(String result) {
        try {
            JSONArray dataList = new JSONArray(result);
            for (int i=0; i<dataList.length(); i++) {
                resultDtos.add(new UpcomingTrajectDto(dataList.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(resultDtos);
        super.setChanged();
        super.notifyObservers();
    }

    public List<UpcomingTrajectDto> getResultDtos() {
        return resultDtos;
    }
}
