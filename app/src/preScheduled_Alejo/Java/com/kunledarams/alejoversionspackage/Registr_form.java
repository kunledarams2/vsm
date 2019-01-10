package com.kunledarams.alejoversionspackage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Utili.Service;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Registr_form extends AppCompatActivity {

    EditText enterEmail, enterName, enterPhone, enterDepartMent, enterCompanyId;
    private RegistrationForm registrationForm;
    private Preschedule preschedule;
    private static final int Gallery_Request = 1000;
    private Uri imageUri =null;
    private ImageView EmployeeImage;
    private FirebaseAuth mAthm;
    private DatabaseReference userDB;
    private ProgressDialog dialog;
    private Toolbar toolbar;
    public static final int GOOGLE_LOGIN_REQUEST_CODE = 1;

    private MobileServiceClient mclient;
   // private MobileServiceTable<RegistrationForm>mobileServiceTable;
    private MobileServiceTable<RegistrationForm>RegTable;
    private  MobileServiceTable<Preschedule>PreTable;
    Service service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registr_form);
        toolbar=(Toolbar)findViewById(R.id.toolbarsignup);

        service= new Service(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Registr_form.this, Login_Form.class);
                startActivity(intent);
            }
        });
        enterInfrom();

        /** using firebase for the authication of user**/
        mAthm= FirebaseAuth.getInstance();
        userDB= FirebaseDatabase.getInstance().getReference().child("EmployeeList");

        dialog = new ProgressDialog(this);
        registrationForm = new RegistrationForm();
        preschedule = new Preschedule();

        try {
            mclient = new MobileServiceClient("https://veemo.azurewebsites.net",this);
            mclient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {

                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;

                }
            });
            RegTable= mclient.getTable(RegistrationForm.class);
            PreTable= mclient.getTable(Preschedule.class);
        }catch ( Exception e){
            e.printStackTrace();
        }




    }
    public void mAuth(){
        mclient.login(MobileServiceAuthenticationProvider.MicrosoftAccount ,new UserAuthenticationCallback() {
            @Override
            public void onCompleted(MobileServiceUser user, Exception exception, ServiceFilterResponse response) {

                if(exception==null){

                    InsertInfro();

                }else{
                    Toast.makeText(getApplication(), exception.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When request completes

        if (requestCode == Gallery_Request) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
                EmployeeImage.setImageURI(imageUri);

            }

        }
    }

    public void enterInfrom(){

        enterName = (EditText)findViewById(R.id.editUsername);
        enterEmail = (EditText)findViewById(R.id.editEmail);
        enterPhone = (EditText)findViewById(R.id.editPhone);
        enterDepartMent = (EditText)findViewById(R.id.editDepartment);
        enterCompanyId = (EditText)findViewById(R.id.employeeId);
        EmployeeImage = (ImageView)findViewById(R.id.circleImageView);

    }

     @SuppressLint("StaticFieldLeak")
     public void InsertInfro(){

        final String fillName = enterName.getText().toString().trim();
        final String fillEmail = enterEmail.getText().toString().trim();
        final String fillPhone = enterPhone.getText().toString().trim();
        final String fillDepartment = enterDepartMent.getText().toString().trim();
        final String fillCompanyid = enterCompanyId.getText().toString().trim();

        registrationForm.setEmployeeName(fillName);
        registrationForm.setEmployeeDepartment(fillDepartment);
        registrationForm.setEmployeeEmail(fillEmail);
        registrationForm.setEmployeePhone(fillPhone);
        registrationForm.setEmployeeId(fillCompanyid);

        // insert inform into the table or the particular userId

        //reg_table.insertValues(registrationForm);
        if(!TextUtils.isEmpty(fillName)&&!TextUtils.isEmpty(fillEmail)&&!TextUtils.isEmpty(fillCompanyid)){

            if(service.isOnline()){


                   new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {

                        dialog =ProgressDialog.show(Registr_form.this,"Registration ", "",false,false);
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            //reg_table.insertValues(registrationForm); // Local insert please remove dis before deployment
                            RegTable.insert(registrationForm).get();
                           // PreTable.insert(preschedule).get();

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
                       // reg_table.insertValues(registrationForm);
                        Intent link_to_Login = new Intent(Registr_form.this, Login_Form.class);
                        startActivity(link_to_Login);
                        finish();
                    }
                }.execute();


            }
            else {
                Toast.makeText(this, "Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this,"Please enter your details",Toast.LENGTH_LONG).show();
        }

    }

    public void UserImage (View view){

        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImage,Gallery_Request);
    }

    public  void SubmitBut(View view){

        if(service.isOnline()){

           // mAuth();
            InsertInfro();
        }
        else Toast.makeText(this,"Please check your network",Toast.LENGTH_LONG).show();

    }

}
