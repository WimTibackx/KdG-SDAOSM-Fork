package be.kdg.groepa.android.helper;

import android.text.Layout;
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

    public UpcomingTrajectsViewAdapter(LayoutInflater inflater) {
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
        holder.fill(this.items.get(position));
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

        public void fill(UpcomingTrajectDto traject) {
            this.traject = traject;
            this.nextDate.setText(this.traject.getNextOccurrence());
            this.pickupPlace.setText(this.traject.getPickupPlace().getName());
            this.pickupTime.setText(this.traject.getPickupTime());
            this.dropoffPlace.setText(this.traject.getDropoffPlace().getName());
            this.dropoffTime.setText(this.traject.getDropoffTime());
            this.driver.setText(this.traject.getChauffeurName());
        }
    }
}
