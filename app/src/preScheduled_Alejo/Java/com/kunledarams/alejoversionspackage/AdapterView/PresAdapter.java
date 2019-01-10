package com.kunledarams.alejoversionspackage.Utili;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.R;

import java.util.ArrayList;


/**
 * Created by ok on 8/2/2018.
 */

public class PresAdapter extends RecyclerView.Adapter<PresAdapter.MVholder> {

    private   Activity activity;
    private static ArrayList<Preschedule>preschedules;
    private  Preschedule preschedule;

    public PresAdapter(Activity activity, ArrayList<Preschedule> preschedules) {
        this.activity = activity;
        this.preschedules = preschedules;
    }
    public PresAdapter(ArrayList<Preschedule>osStore){
        preschedules=osStore;
    }

    public class MVholder extends RecyclerView.ViewHolder {
        TextView visitorName, time, vistingPutpose;
        LinearLayout linearLayout;
        Preschedule presched;
        public MVholder(final View itemView) {
            super(itemView);


            visitorName= (TextView)itemView.findViewById(R.id.vis_Name);
            time= (TextView)itemView.findViewById(R.id.time_schedule);
           // vistingPutpose= (TextView)itemView.findViewById(visitorPurpose);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.visitor_info_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                   // builder.setTitle(presched.getVisitorName());
                    final View view1= activity.getLayoutInflater().inflate(R.layout.visitor_info,null);


                    final TextView vphone=(TextView)view1.findViewById(R.id.visitPhone);
                    final TextView vcompany = (TextView)view1.findViewById(R.id.visitorCompany);
                    final TextView vEmail = (TextView)view1.findViewById(R.id.vistEmail);
                    final TextView vpurpose=(TextView)view1.findViewById(R.id.vistorPurpose);
                  final TextView v_name=(TextView)view1.findViewById(R.id.vis_Name);


                    // setting the staff content
                    v_name.setText(presched.getVisitorName());
                    vcompany.setText(presched.getVisitorCompay());
                    vEmail.setText(presched.getVisitorEmail());
                    vpurpose.setText(presched.getVisitorPurpose());
                    vphone.setText(presched.getVisitorPhone());


                    builder.setView(view1);
                    AlertDialog alertDialog= builder.create();

                    alertDialog.show();

                }


            });


        }


    }

    @Override
    public PresAdapter.MVholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view =layoutInflater.inflate(R.layout.expect_recycl,parent,false);

        return new MVholder(view);
    }

    @Override
    public void onBindViewHolder(PresAdapter.MVholder holder, int position) {

        final String visitorId= ((String.valueOf(holder.getItemId())) );
       Preschedule vsitorId = preschedules.get(position);
        holder.visitorName.setText(preschedules.get(position).getVisitorName());
        //holder.vistingPutpose.setText(preschedules.get(position).getVisitorPurpose());
        holder.time.setText(preschedules.get(position).getPreScheduleDate());
        holder.presched=vsitorId;


    }


    @Override
    public int getItemCount() {

        return preschedules.size();
    }



}
