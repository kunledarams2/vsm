package com.kunledarams.alejoversionspackage.AdapterView;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kunledarams.alejoversionspackage.Model.HistoryM;
import com.kunledarams.alejoversionspackage.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;

import java.util.ArrayList;

import static com.kunledarams.alejoversionspackage.R.id.visitorPurpose;

/**
 * Created by ok on 10/22/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Activity activity;
    private static ArrayList<HistoryM>preschedules;

    public HistoryAdapter(Activity activity, MobileServiceList<HistoryM> preschedules) {
        this.activity = activity;
        this.preschedules= preschedules;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view =layoutInflater.inflate(R.layout.historyrecy,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        holder.visitorName.setText(preschedules.get(position).getVisitorName());
        holder.vistingPutpose.setText(preschedules.get(position).getVisitorPurpose());
        holder.time.setText(preschedules.get(position).getPreScheduleDate());

    }

    @Override
    public int getItemCount() {
        return preschedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView visitorName, time, vistingPutpose;

        public ViewHolder(View itemView) {
            super(itemView);
            visitorName= (TextView)itemView.findViewById(R.id.vis_Name);
            time= (TextView)itemView.findViewById(R.id.time_schedule);
            vistingPutpose= (TextView)itemView.findViewById(visitorPurpose);

        }
    }
}
