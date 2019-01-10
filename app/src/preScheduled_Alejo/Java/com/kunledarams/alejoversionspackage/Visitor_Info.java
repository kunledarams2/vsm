package com.kunledarams.alejoversionspackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Visitor_Info extends AppCompatActivity {
    String visitorId;
    MobileServiceClient client;
    MobileServiceTable<Preschedule>PTable;
    TextView visitorName,vistorPhone,visitorEmail,visitorCompany, purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visitor_info);

        visitorId= getIntent().getExtras().getString("visitorId");

        vistorPhone=(TextView)findViewById(R.id.visitorPhone);
        visitorEmail=(TextView)findViewById(R.id.vistEmail);
        purpose=(TextView)findViewById(R.id.vistorPurpose);

        try {
            client= new MobileServiceClient("https://emalejo.azurewebsites.net",this);
            client.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {

                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;

                }
            });
            PTable= client.getTable(Preschedule.class);
        }catch ( Exception e){
            e.printStackTrace();
        }

        setView();

    }
    public void setView(){


        final  String [] getItem = new String[4];
        if(isOnline()){

            @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void>task= new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        List<Preschedule> visitorInfro= PTable.where().field("visitorname").eq(visitorId).execute().get();
                        for (Preschedule item: visitorInfro){
                            getItem[0]=item.getVisitorName();
                            getItem[1]=item.getVisitorEmail();
                            getItem[2]=item.getVisitorPhone();
                            getItem[3]=item.getVisitorPurpose();
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
                    visitorName.setText(getItem[0]);
                    visitorEmail.setText(getItem[1]);
                    vistorPhone.setText(getItem[2]);
                    purpose.setText(getItem[3]);
                }
            };

            runAsyncTask(task);


        }
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
