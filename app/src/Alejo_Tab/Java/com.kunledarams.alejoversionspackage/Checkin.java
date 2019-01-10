package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Checkin extends AppCompatActivity {

    private TextView name,phone,company,email, accesscode;
    private ArrayList<String>myList;
    private ProgressDialog dialog;
    private Session session=null;
    private  String employeeEmail;
    private MobileServiceClient mClient;
    private MobileServiceTable<RegistrationForm>EmployeeTable;
    private Toolbar toolbar;
    private String EmployeEmail;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);


        name=(TextView)findViewById(R.id.vistName);
        phone=(TextView)findViewById(R.id.vistMobile);
        company=(TextView)findViewById(R.id.vistCompany);
        email=(TextView)findViewById(R.id.vistEmail);
        accesscode=(TextView)findViewById(R.id.vistAccessCode);
        toolbar=(Toolbar)findViewById(R.id.toolbarche);
        imageView=(ImageView)findViewById(R.id.circleImageView);
        getSupportActionBar();


        myList=(ArrayList<String>)getIntent().getSerializableExtra("BasicList");

        name.setText(myList.get(2));
        phone.setText(myList.get(3));
        company.setText(myList.get(6));
        email.setText(myList.get(4));
        accesscode.setText(myList.get(8));
        toolbar.setTitle("Host   "  +   myList.get(9));
        EmployeEmail=myList.get(9);
        //employeeEmail=myList.get(7);
        dialog=new ProgressDialog(this);

        try {
            mClient= new MobileServiceClient("https://emalejo.azurewebsites.net",this);

            EmployeeTable=mClient.getTable(RegistrationForm.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try{
            Glide.with(this)
                    .load(myList.get(7))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }

       getEmployeeEmail();
    }


    private void getEmployeeEmail(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    /** Query the Employee List to get the
                     * selected employee Email for notification**/
                    MobileServiceList<RegistrationForm>EmployeeList=EmployeeTable.where().field("employeeName").eq(EmployeEmail).execute().get();
                    for(RegistrationForm form:EmployeeList){
                        employeeEmail=form.getEmployeeEmail();
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
                Toast.makeText(Checkin.this,employeeEmail,Toast.LENGTH_LONG).show();
            }
        }.execute();

    }

    public void Sign_In(View view) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {

                dialog=ProgressDialog.show(Checkin.this,"Contacting  Host","Please wait.....",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                Properties props = new Properties();

                props.put("mail.smtp.host", "smtp.office365.com");
                props.put("mail.smtp.socketFactory.port","25");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.socketFactory.port", "465");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");

                session= Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MailPassEmail.emailAddress,MailPassEmail.paswordd);
                    }
                });

                try {
                    Message message= new MimeMessage(session);
                    message.setFrom(new InternetAddress(MailPassEmail.emailAddress));
                    message.setSubject("Visitor Notification");
                    message.setText("Dear  " +   myList.get(9)+ "\n" +  myList.get(1)    +    myList.get(2)   + "  is at the reception waiting for you");
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(employeeEmail));
                    Transport.send(message);

                }catch (MessagingException e){
                    e.printStackTrace();

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                Intent checkmeIn= new Intent(getApplicationContext(),ConnectVisitor.class);
                startActivity(checkmeIn);
                finish();
            }
        }.execute();


    }

}
