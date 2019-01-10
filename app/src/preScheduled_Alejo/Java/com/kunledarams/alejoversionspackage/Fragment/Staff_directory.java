package com.kunledarams.alejoversionspackage.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Pre_MainActivity;
import com.kunledarams.alejoversionspackage.R;
import com.kunledarams.alejoversionspackage.Utili.StaffAdapter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Staff_directory extends AppCompatActivity {

    private View view;
    private LinearLayout linearLayout;
    private ArrayList<RegistrationForm>forms;
    private MobileServiceTable<RegistrationForm>staffDire;
    private MobileServiceClient mClient;
    private StaffAdapter staffAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ArrayList<String> getuserId =new ArrayList<>();





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_staff_directory);
        forms= new ArrayList<>();

        getuserId=getIntent().getStringArrayListExtra("userId");

        toolbar=(Toolbar)findViewById(R.id.staffTbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Staff_directory.this, Pre_MainActivity.class);
                intent.putStringArrayListExtra("userId",getuserId);
                startActivity(intent);
                finish();
            }
        });

        try {
            mClient= new MobileServiceClient("https://veemo.azurewebsites.net",this);

            staffDire= mClient.getTable(RegistrationForm.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        recyclerView=(RecyclerView)findViewById(R.id.recyStaff);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        populate();
    }



    public  void  populate(){

        @SuppressLint("StaticFieldLeak") AsyncTask<Void,Void,Void>task= new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    forms=staffDire.execute().get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (MobileServiceException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if(forms.size()>0){
                    staffAdapter= new StaffAdapter(Staff_directory.this,forms);
                    recyclerView.setAdapter(staffAdapter);

                }

            }
        };
        runAsyncTask(task);

    }

    private AsyncTask<Void,Void,Void> runAsyncTask(AsyncTask<Void, Void,Void>task){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else
            return task.execute();
    }

}
