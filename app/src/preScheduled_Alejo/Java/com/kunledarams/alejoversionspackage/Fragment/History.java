package com.kunledarams.alejoversionspackage.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.kunledarams.alejoversionspackage.AdapterView.HistoryAdapter;
import com.kunledarams.alejoversionspackage.Model.HistoryM;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.Pre_MainActivity;
import com.kunledarams.alejoversionspackage.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class History extends AppCompatActivity {

    private MobileServiceClient mSClient;
    private MobileServiceTable<Preschedule> pTable;
    private MobileServiceTable<HistoryM>historyMMST;
    private Preschedule preschedule;
    private ArrayList<String> getInfro= new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private RecyclerView recyclerView;
    private LinearLayout progLoading;
    private Toolbar  toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getInfro=getIntent().getStringArrayListExtra("userId");
      toolbar=(Toolbar)findViewById(R.id.histoolbar);
      setSupportActionBar(toolbar);

      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent= new Intent(History.this, Pre_MainActivity.class);
              intent.putStringArrayListExtra("userId",getInfro);
              startActivity(intent);
              finish();

          }
      });

        try {
            mSClient= new MobileServiceClient("https://veemo.azurewebsites.net",this);
            pTable=mSClient.getTable(Preschedule.class);
            historyMMST=mSClient.getTable(HistoryM.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        recyclerView=(RecyclerView)findViewById(R.id.recyHistory);
        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        progLoading=(LinearLayout)findViewById(R.id.historyloading);
        // Toast.makeText(getActivity(),getInfro, Toast.LENGTH_LONG).show();
        inserHistoryTable();

    }




    @SuppressLint("StaticFieldLeak")
    public void inserHistoryTable(){


         new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
             progLoading.setVisibility(View.VISIBLE);

            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                  final   MobileServiceList<HistoryM>historyMs=historyMMST.where().field("employeeid").eq(getInfro.get(0)).execute().get();
                    historyAdapter= new HistoryAdapter(History.this, historyMs);


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
                progLoading.setVisibility(View.GONE);
                //Toast.makeText(getActivity(),getItem[0] ,Toast.LENGTH_LONG).show();
               recyclerView.setAdapter(historyAdapter);
            }
        }.execute();


    }

}
