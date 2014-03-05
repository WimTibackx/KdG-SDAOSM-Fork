package be.kdg.groepa.android.helper;

import android.content.Context;
import android.text.Layout;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import be.kdg.groepa.android.R;
import be.kdg.groepa.android.dto.UpcomingTrajectDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delltvgateway on 3/4/14.
 */
public class UpcomingTrajectsViewAdapter extends BaseAdapter {

    private List<UpcomingTrajectDto> items;
    private LayoutInflater inflater;
    private Context context;

    public UpcomingTrajectsViewAdapter(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.items = new ArrayList<>();
    }

    public void addItems(List<UpcomingTrajectDto> items) {
        this.items = items;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UpcomingTrajectCardView holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.upcoming_traject_card, null);
            holder = new UpcomingTrajectCardView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UpcomingTrajectCardView)convertView.getTag();
        }
        holder.fill(this.items.get(position), this.context);
        return convertView;
    }

    public static class UpcomingTrajectCardView {
        private UpcomingTrajectDto traject;
        private TextView nextDate;
        private TextView pickupPlace;
        private TextView pickupTime;
        private TextView dropoffPlace;
        private TextView dropoffTime;
        private TextView driver;

        public UpcomingTrajectCardView(View v) {
            this.nextDate = (TextView)v.findViewById(R.id.uptDate);
            this.pickupPlace = (TextView)v.findViewById(R.id.uptPickupPlace);
            this.pickupTime = (TextView)v.findViewById(R.id.uptPickupTime);
            this.dropoffPlace = (TextView)v.findViewById(R.id.uptDropoffPlace);
            this.dropoffTime = (TextView)v.findViewById(R.id.uptDropoffTime);
            this.driver = (TextView)v.findViewById(R.id.uptDriver);
        }

        public void fill(UpcomingTrajectDto traject, Context context) {
            this.traject = traject;
            this.nextDate.setText(DateUtils.formatDateTime(context, this.traject.getNextOccurrence().getTimeInMillis(),DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_NO_YEAR));
            this.pickupPlace.setText(this.traject.getPickupPlace().getName());
            this.pickupTime.setText(this.traject.getPickupTime());
            this.dropoffPlace.setText(this.traject.getDropoffPlace().getName());
            this.dropoffTime.setText(this.traject.getDropoffTime());
            this.driver.setText(this.traject.getChauffeurName());
        }
    }
}
