package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.ActivityTable;
import com.kunledarams.alejoversionspackage.Model.BasicRegistration;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Purpose extends AppCompatActivity {
    private Spinner Purpose, employeeName;
    private ActivityTable activityTable;
    private ArrayList<String>BazicList;
    private MobileServiceClient mClient;
    private MobileServiceTable<ActivityTable>ATable;
    private MobileServiceTable<BasicRegistration>BR;
    private MobileServiceTable<RegistrationForm>EmployeeTable;
    private ProgressDialog dialog;
    private Service service;
    private LinearLayout ProgreBar;
    private final LinkedList<String> EmployeeInfro = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose);

        Purpose= (Spinner)findViewById(R.id.PSpinner);
        employeeName=(Spinner)findViewById(R.id.staffSpinner);
        ProgreBar=(LinearLayout)findViewById(R.id.purpProgress);

        activityTable = new ActivityTable();
        BazicList= (ArrayList<String>) getIntent().getSerializableExtra("BasicList");



        try {
            mClient= new MobileServiceClient("https://emalejo.azurewebsites.net",this);
            ATable= mClient.getTable(ActivityTable.class);
            BR= mClient.getTable(BasicRegistration.class);
            EmployeeTable= mClient.getTable(RegistrationForm.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        dialog=new ProgressDialog(this);
        service=new Service();

        GetVID();
        PurposeList();

    }

    public  void PurposeList(){
        List<String>vPurpose= new ArrayList<>();
        vPurpose.add("Select Your Purpose");
        vPurpose.add("Meeting");
        vPurpose.add("Event");
        vPurpose.add("Service & Maintenance");
        vPurpose.add("Pickup or Delivery");

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,vPurpose);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        Purpose.setAdapter(adapter);

        Purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String selector = adapterView.getItemAtPosition(i).toString();


                if(selector.equals("Select Your Purpose")){
                    return;
                }else {
                    if(selector.equals("Meeting")){
                        activityTable.setPurpose("Meeting");
                    }
                    if(selector.equals("Event")){
                        activityTable.setPurpose("Event");
                    }
                    if(selector.equals("Service & Maintenance")){
                        activityTable.setPurpose("Service & Maintenance");
                    }
                    if(selector.equals("Pickup or Delivery")){
                        activityTable.setPurpose("Pickup or Delivery");

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void StaffList(){
        //final ArrayList<String>getItem=EmployeeInfro;
        final LinkedList<String>getItem=EmployeeInfro;
        getItem.addFirst("Select Who to see");

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getItem);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        employeeName.setAdapter(adapter);

        employeeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selector= adapterView.getItemAtPosition(i).toString();
                if(!selector.equals("Select Who to see")){
                    //getItem.indexOf(selector);
                    activityTable.setStaffTosee(selector);
                    BazicList.add(selector);//8
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void GetVID(){

        if(BazicList.size()>=6){
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        MobileServiceList<BasicRegistration> BList=BR.where().field("visitorName").eq(BazicList.get(2)).execute().get();
                        for(BasicRegistration RList:BList){
                            activityTable.setvID(RList.getVID());
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
                    super.onPostExecute(aVoid);
                    GetEmployee();
                }
            }.execute();
        }
    }

    /**  Getting the Employee information from azure mobile service
     * Employee Name, Email**/
    private void GetEmployee(){

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    MobileServiceList<RegistrationForm>FormList=EmployeeTable.where().field("DELETED").eq("false").execute().get();
                    for(RegistrationForm form:FormList){
                        EmployeeInfro.add(form.getEmployeeName());//9
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
                StaffList();
                ProgreBar.setVisibility(View.GONE);
            }
        }.execute();


    }

    public void GonextPage(View view) {

        if(BazicList.size()>=7){
            final String getVisitorName=BazicList.get(2);
            activityTable.setAccessCode(BazicList.get(8)); // Access Code
            activityTable.setvPName(getVisitorName);// visitor name
            activityTable.setStatus("Sign_In");

            AsyncTask<Void, Void, Void>task = new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    dialog=ProgressDialog.show(Purpose.this,"Purpose","Please........",false,false);

                }

                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        ATable.insert(activityTable).get();
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
                    dialog.dismiss();
                    Intent intent = new Intent(Purpose.this, Checkin.class);
                    intent.putStringArrayListExtra("BasicList",(ArrayList<String>)BazicList);
                    Toast.makeText(Purpose.this,BazicList.get(8),Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                }
            };
            service.runAsyncTask(task);
        }





    }
}
