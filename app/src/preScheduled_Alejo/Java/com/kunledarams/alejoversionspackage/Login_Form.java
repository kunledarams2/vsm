package com.kunledarams.alejoversionspackage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Utili.Service;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.MobileServiceSyncTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Login_Form extends AppCompatActivity {



    private EditText email;
    private EditText password;

    public String Email;
    public   String Password;

    private ProgressDialog dialog;
    private MobileServiceClient client;

    LinearLayout progressBar;

    private MobileServiceTable<RegistrationForm>serviceTable; // for online
    private MobileServiceSyncTable<RegistrationForm>offLineTable;
    Service service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);

        email = (EditText)findViewById(R.id.editEmail);
        password = (EditText)findViewById(R.id.userCompanyId);
        Email= email.getText().toString();
        Password= password.getText().toString();

        service= new Service(this);

        try{
            client = new MobileServiceClient("https://veemo.azurewebsites.net",this);

            serviceTable = client.getTable(RegistrationForm.class);

            offLineTable=client.getSyncTable(RegistrationForm.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }



    public void RegForm (View view){

        Intent LinK_Regis = new Intent(this, Registr_form.class);
        startActivity(LinK_Regis);
    }
    @SuppressLint("StaticFieldLeak")
    public void SubmitBut(View view){

        final String fillName = email.getText().toString().trim();
        final  String fillpassword = password.getText().toString().trim();
        final ArrayList<String> getEmployee= new ArrayList();


        if(service.isOnline()){


            if(!TextUtils.isEmpty(fillName)&& !TextUtils.isEmpty(fillpassword)) {

               new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        dialog = ProgressDialog.show(Login_Form.this, "Logging In", "Please Wait.....", false);
                       // progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            List<RegistrationForm>formList1 =serviceTable.where().field("employeeId").eq(fillpassword).execute().get();

                            for (RegistrationForm item : formList1) {
                                getEmployee.add(item.getEmployeeId());
                                getEmployee.add(item.getEmployeeName());
                                getEmployee.add(item.getEmployeeEmail());




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

                        dialog.dismiss();
                        if (getEmployee.size()==0) {

                            Toast.makeText(Login_Form.this, "Please Register!!!", Toast.LENGTH_LONG).show();
                        } else if(getEmployee.get(0).equals(fillpassword) && getEmployee.get(2).equals(fillName)) {

                            Toast.makeText(Login_Form.this, " Welcome  " +  getEmployee.get(1), Toast.LENGTH_LONG).show();

                           Intent Link_Home = new Intent(Login_Form.this,Pre_MainActivity.class);
                           Link_Home.putStringArrayListExtra("userId", getEmployee);
                            startActivity(Link_Home);
                            finish();
                        }


                    }

                }.execute();

            }
            else {
                Toast.makeText(this, "Please check your Email or EmployeeID",Toast.LENGTH_SHORT).show();
            }
        }else Toast.makeText(getApplication(), "No Internet ",Toast.LENGTH_LONG).show();

    }



}
