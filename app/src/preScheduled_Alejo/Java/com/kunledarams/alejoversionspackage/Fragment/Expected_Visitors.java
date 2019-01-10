package com.kunledarams.alejoversionspackage.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Login_Form;
import com.kunledarams.alejoversionspackage.Model.HistoryM;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Pre_MainActivity;
import com.kunledarams.alejoversionspackage.Pre_schedule;
import com.kunledarams.alejoversionspackage.R;
import com.kunledarams.alejoversionspackage.Utili.PresAdapter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Expected_Visitors extends Fragment {

   View rootView;
    private FloatingActionButton fab;
    LinearLayout linearLayout;
    private MobileServiceClient client;
    private MobileServiceTable<Preschedule>serviceTable;
    private MobileServiceTable<RegistrationForm>Regtable;
    private MobileServiceTable<HistoryM>HTable;
    private RecyclerView recyclerView;
    private ArrayList<Preschedule>preschedules;
    private PresAdapter presAdapter;
    private Pre_schedule pre_schedule;
    private  Preschedule preschedule;
    private HistoryM history;
    private RegistrationForm registrationForm;
    private Login_Form login_form;
    private ProgressDialog dialog;
    private String getInfro ;
    private TextView textView;


    LinearLayout loadVisitorLa;
    public Expected_Visitors() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pre_schedule =new Pre_schedule();
        registrationForm= new RegistrationForm();
        login_form= new Login_Form();
        dialog= new ProgressDialog(getContext());
        history=new HistoryM();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_expected__visitors, container, false);


        loadVisitorLa= (LinearLayout)rootView.findViewById(R.id.VisitorLoading);
        loadVisitorLa.setVisibility(View.GONE);
        textView=(TextView)rootView.findViewById(R.id.viewStatus);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recySchedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        preschedules = new ArrayList<>();
        // Inflate the layout for this fragment


        try{
            client = new MobileServiceClient("https://emalejo.azurewebsites.net",getActivity());
            serviceTable= client.getTable(Preschedule.class);
            Regtable=client.getTable(RegistrationForm.class);
            HTable= client.getTable(HistoryM.class);

        }catch (Exception e){
            e.printStackTrace();
        }
        Pre_MainActivity preMainActivity = (Pre_MainActivity)getActivity();

        Bundle userId = preMainActivity.getMyResult();
        getInfro= userId.getString("MyId");

       // Toast.makeText(getActivity(),getInfro,Toast.LENGTH_LONG).show();

        fab = (FloatingActionButton)rootView.findViewById(R.id.fab_Pre_schedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent link_PreSchedule = new Intent(getContext(), Pre_schedule.class);
                link_PreSchedule.putExtra("userId",getInfro);
                startActivity(link_PreSchedule);
            }
        });


        populate();

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    public void populate()  {


        final String[] getItem = new String[4];
        final ArrayList<String> historyArrayList = new ArrayList<>() ;
        final int[] counter = {0};

       new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                loadVisitorLa.setVisibility(View.VISIBLE);
                // Toast.makeText(getContext(),dateChecker,Toast.LENGTH_LONG).show();

            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {

                    MobileServiceList<Preschedule> preschedules=serviceTable.where().field("employeeid").eq(getInfro).execute().get();
                    for(Preschedule list:preschedules){
                        if(list.getValidateaccess().equals("Sign_Out")){


                            history.setVisitorName(list.getVisitorName());
                            history.setVisitorPhone(list.getVisitorPhone());
                            history.setVisitorCompany(list.getVisitorCompay());
                            history.setVisitorPurpose(list.getVisitorPurpose());
                            history.setEmployeeid(list.getEmployeeid());
                            history.setPreScheduleDate(list.getPreScheduleDate());
                            history.setStatus(list.getValidateaccess());
                            HTable.insert(history).get();
                            counter[0] += 1;

                            serviceTable.delete(list.getVisitorid());
                        }
                        presAdapter = new PresAdapter(getActivity(),preschedules);


                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadVisitorLa.setVisibility(View.GONE);
                recyclerView.setAdapter(presAdapter);

            }

        }.execute();


    }

    @SuppressLint("StaticFieldLeak")
    public void ViewtoUI(final ArrayList<String>employeeId){
        int counter = 0;

       if(employeeId.size()>0){
           for(String getId:employeeId){
              if(getId.equals(getInfro))

                  Toast.makeText(getContext(),getId,Toast.LENGTH_LONG).show();

                  new AsyncTask<Void, Void, Void>() {
                      @Override
                      protected void onPreExecute() {
                          loadVisitorLa.setVisibility(View.VISIBLE);
                      }

                      @Override
                      protected Void doInBackground(Void... voids) {
                          try {
                              MobileServiceList<Preschedule> newlist=serviceTable.where().field("employeeid").eq(getInfro).execute().get();

                              presAdapter = new PresAdapter(getActivity(),newlist);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          } catch (ExecutionException e) {
                              e.printStackTrace();
                          }
                          return null;
                      }

                      @Override
                      protected void onPostExecute(Void aVoid) {
                          super.onPostExecute(aVoid);
                          loadVisitorLa.setVisibility(View.GONE);
                          recyclerView.setAdapter(presAdapter);
                      }
                  }.execute();

               break;
           }
       }

    }

}
