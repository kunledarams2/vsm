package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.BasicRegistration;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BasicInfro extends AppCompatActivity {

    private Spinner spinnerGender, spinnerPurpose,spinnerEmployeeList;
    private EditText vName, vPhone, vAddress, vCompany,vEmail;
    private AutoCompleteTextView serachView;
    private LinearLayout otherContainer;
    private BasicRegistration BR;
    private MobileServiceClient mClient;
    private MobileServiceTable<Preschedule>PTable;
    private ProgressDialog dialog;
    private Service service;
    private ArrayList<String>basicInfro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_infro);
        InitView();
        BR= new BasicRegistration();
        basicInfro= new ArrayList<>();


        try {
            mClient= new MobileServiceClient("https://emalejo.azurewebsites.net", this);
          PTable= mClient.getTable(Preschedule.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        dialog= new ProgressDialog(this);
        service= new Service();
        SelectGender();



    }



    public void InitView(){

       vName=(EditText) findViewById(R.id.editName);

        vAddress=(EditText)findViewById(R.id.editAddress);
        vCompany=(EditText)findViewById(R.id.editCompany);
        vPhone= (EditText)findViewById(R.id.editPhone);
        vEmail=(EditText)findViewById(R.id.editEmail);
        spinnerGender=(Spinner)findViewById(R.id.Gender);


    }

    private ArrayList<Preschedule>search() throws ExecutionException, InterruptedException {
        final String entername= vName.getText().toString().trim();
        return PTable.where().field(entername).eq("visitorname").execute().get();
    }

    public void SearchPrbook() throws ExecutionException, InterruptedException {

        ArrayList<Preschedule> list=search();
        ArrayAdapter<Preschedule>adapter= new ArrayAdapter<Preschedule>(BasicInfro.this,android.R.layout.simple_list_item_1,list);
    }



  private void CheckProBook(){

      final String enterName=  serachView.getText().toString();
      final String enterPhone = vPhone.getText().toString();
      final String enterAddress= vAddress.getText().toString();
      final String enterEmail= vEmail.getText().toString();

      final String[]getBookInfro=new String[10];

      if(!TextUtils.isEmpty(enterEmail)) {

          AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

              @Override
              protected void onPreExecute() {
                  super.onPreExecute();
                  // dialog= ProgressDialog.show(BasicInfro.this,"Loading","Please wait....",false, false);
              }

              @Override
              protected Void doInBackground(Void... voids) {

                  try {
                      MobileServiceList<Preschedule> getPreBook = PTable.where().field(enterName).eq("visitorname").execute().get();

                      for (Preschedule list : getPreBook) {
                          getBookInfro[0] = list.getVisitorName();
                          getBookInfro[1] = list.getVisitorPhone();
                          getBookInfro[2] = list.getVisitorEmail();
                          getBookInfro[3] = list.getVisitorAdress();
                          getBookInfro[4] = list.getVisitorCompay();
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

                  if (getBookInfro[0] != ("")) {
                      serachView.setText(getBookInfro[0]);
                      vPhone.setText(getBookInfro[1]);
                      vEmail.setText(getBookInfro[2]);
                      vAddress.setText(getBookInfro[3]);
                      vCompany.setText(getBookInfro[4]);
                  }


                  // dialog.dismiss();

              }
          };

          service.runAsyncTask(task);


      }
      }



  public void SelectGender(){

      List<String>gender= new ArrayList<>();
      gender.add("Select Gender");
      gender.add("Male");
      gender.add("Female");

      ArrayAdapter<String>adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
              ,gender);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
      spinnerGender.setAdapter(adapter);

      spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String itemSelector= adapterView.getItemAtPosition(i).toString();

              if(itemSelector.equals("Male")){
                 basicInfro.add("Male");//0
                  basicInfro.add("Mr");//1
              }
              if(itemSelector.equals("Female")){
                  basicInfro.add("Female");//0
                  basicInfro.add("Mrs");//1
              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });


  }
    public void GonextPage(final View v) {

        final String enterName= vName.getText().toString();
        final String enterPhone = vPhone.getText().toString();
        final String enterAddress= vAddress.getText().toString();
        final String enterEmail= vEmail.getText().toString();
        final String enterCompany= vCompany.getText().toString();



        basicInfro.add(enterName); //2
        basicInfro.add(enterPhone);//3
        basicInfro.add(enterEmail);//4
        basicInfro.add(enterAddress);//5
        basicInfro.add(enterCompany);//6
        if(basicInfro.size()>=6){
            Intent nextpage= new Intent(BasicInfro.this, CaptureUmage.class);
            nextpage.putStringArrayListExtra("basicList", (ArrayList<String>) basicInfro);
            Toast.makeText(getApplicationContext(), basicInfro.get(3),Toast.LENGTH_LONG).show();
            startActivity(nextpage);
            finish();
        }
        else {
            Toast.makeText(this,"Please enter all your details",Toast.LENGTH_LONG).show();
        }
        // InsertValues();

    }


}
