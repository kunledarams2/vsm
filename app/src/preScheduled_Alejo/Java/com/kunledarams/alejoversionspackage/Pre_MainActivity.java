package com.kunledarams.alejoversionspackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Fragment.History;
import com.kunledarams.alejoversionspackage.Fragment.Staff_directory;
import com.kunledarams.alejoversionspackage.Model.HistoryM;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Utili.PresAdapter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class Pre_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;
    private String key ;
    private TextView EmpoyeeeId,EmployeeName;
    private ImageView imageView;
    private MobileServiceClient client;
    private MobileServiceTable<RegistrationForm>RegTable;
    private MobileServiceTable<Preschedule>PreVisitor;
    private MobileServiceTable<HistoryM>HTable;
    private ArrayList<String> userId= new ArrayList<>();
    LinearLayout  loadVisitorLa;
    private HistoryM history;
    private PresAdapter presAdapter;
    private RecyclerView recyclerView;
    private TextView Vstatues, employeeTname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Vstatues=(TextView)findViewById(R.id.viewStatus);
        employeeTname=(TextView)findViewById(R.id.userName);
        recyclerView=(RecyclerView)findViewById(R.id.recySchedule);

        LinearLayoutManager llm= new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);


        View nhM= navigationView.getHeaderView(0);
        EmpoyeeeId=(TextView)nhM.findViewById(R.id.EmployeeNo);
        EmployeeName=(TextView)nhM.findViewById(R.id.employeeName);
        imageView=(ImageView)nhM.findViewById(R.id.imageUser);

          userId=getIntent().getStringArrayListExtra("userId");
          loadVisitorLa=(LinearLayout)findViewById(R.id.VisitorLoading);
          history= new HistoryM();


      try{
            client = new MobileServiceClient("https://veemo.azurewebsites.net",this);

            RegTable = client.getTable(RegistrationForm.class);
            PreVisitor=client.getTable(Preschedule.class);
            HTable=client.getTable(HistoryM.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        EmpoyeeeId.setText(userId.get(0));
       EmployeeName.setText(userId.get(1));
       employeeTname.setText(userId.get(1));

        populate();


    }
    public  Bundle getMyResult(){
        Bundle bundle = new Bundle();
        bundle.putString("MyId",key);
        return bundle;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.nav_addSchedule:
                Intent add_visitor= new Intent(this, Pre_schedule.class);
                add_visitor.putStringArrayListExtra("userId",userId);
                startActivity(add_visitor);
                break;
            case  R.id.staffdirectory:
                Intent openStaffDirectory= new Intent(this,Staff_directory.class);
                openStaffDirectory.putStringArrayListExtra("userId",userId);
                startActivity(openStaffDirectory);
                break;

            case R.id.nav_history:
                Intent openHistory = new Intent(this, History.class);
                openHistory.putStringArrayListExtra("userId",userId);
                startActivity(openHistory);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void AddVisitor(View view) {

        Intent addvisitor= new Intent(this, Pre_schedule.class);
        //addvisitor.putExtra("userId",userId);
        addvisitor.putStringArrayListExtra("userId",userId);
        Toast.makeText(this,userId.get(0),Toast.LENGTH_LONG).show();
        startActivity(addvisitor);
    }


    @SuppressLint("StaticFieldLeak")
    public void populate()  {

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

                    MobileServiceList<Preschedule> preschedules= PreVisitor.where().field("employeeid").eq(userId.get(0)).execute().get();
                    for(Preschedule list:preschedules){
                        if(list.getValidateaccess().equals("Sign_Out")){

                            history.setVisitorName(list.getVisitorName());
                            history.setVisitorPhone(list.getVisitorPhone());
                            history.setVisitorCompany(list.getVisitorCompay());
                            history.setVisitorPurpose(list.getVisitorPurpose());
                            history.setEmployeeid(list.getEmployeeid());
                            history.setPreScheduleDate(list.getPreScheduleDate());
                            history.setStatus(list.getValidateaccess());
                            history.setVisitorEmail(list.getVisitorEmail());
                            history.setVisitorAddress(list.getVisitorAdress());
                            HTable.insert(history).get();
                            counter[0] += 1;

                            PreVisitor.delete(list.getVisitorid());
                        }
                        presAdapter = new PresAdapter(Pre_MainActivity.this,preschedules);


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

}
