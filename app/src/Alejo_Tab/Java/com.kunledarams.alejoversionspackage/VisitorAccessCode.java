package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.Preschedule;
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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VisitorAccessCode extends AppCompatActivity {

    private EditText enterAccessCode;
    private Button Check;
    private LinearLayout linearLayout, linearlayout2;
    private MobileServiceClient mclient;
    private MobileServiceTable<Preschedule>PTable;
    private MobileServiceTable<RegistrationForm>EmTable;
    private TextView name, address, company, purpose,employeeTosee, email, phone;
    private String AccessCode;
    private ProgressDialog dialog;
    private Session session=null;
    public VisitorAccessCode(String accessCode) {
        AccessCode = accessCode;
    }

    public String getAccessCode() {
        return AccessCode;
    }

    public void setAccessCode(String accessCode) {
        AccessCode = accessCode;
    }

    public VisitorAccessCode() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_access_code);

        enterAccessCode=(EditText)findViewById(R.id.editCode);
        Check=(Button)findViewById(R.id.buttCheck);
        Check.setVisibility(View.GONE);

        linearLayout=(LinearLayout)findViewById(R.id.visitorLayout);
        linearLayout.setVisibility(View.GONE);
        linearlayout2=(LinearLayout)findViewById(R.id.layoutEnterCode);

        name= (TextView)findViewById(R.id.visitorName);
        address=(TextView)findViewById(R.id.visitorAddress);
        phone=(TextView)findViewById(R.id.visitorPhone);
        email=(TextView)findViewById(R.id.visitorEmail);
        company=(TextView)findViewById(R.id.visitorCompany);
        purpose=(TextView)findViewById(R.id.visitorpurpose);
        employeeTosee=(TextView)findViewById(R.id.EmpolyeeTosee);

        enterAccessCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isReady= enterAccessCode.getText().toString().length()>0;
                Check.setEnabled(isReady);
                Check.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        try {
            mclient=new MobileServiceClient("https://emalejo.azurewebsites.net",this);
            PTable=mclient.getTable(Preschedule.class);
            EmTable=mclient.getTable(RegistrationForm.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void CheckAcc(View view) {

       final String getAccessCode= enterAccessCode.getText().toString().trim();
        setAccessCode(getAccessCode);
        final ArrayList<String>getVisitorInfro=new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog=ProgressDialog.show(VisitorAccessCode.this,"Search","Please Wait...",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    MobileServiceList<Preschedule>Plist=PTable.where().field("accesscode").eq(getAccessCode).execute().get();

                    for (Preschedule preschedule:Plist){

                        if(preschedule.getValidateaccess().equals("Not Yet")){
                            getVisitorInfro.add(preschedule.getValidateaccess());
                            getVisitorInfro.add(preschedule.getVisitorName());
                            getVisitorInfro.add(preschedule.getVisitorEmail());
                            getVisitorInfro.add(preschedule.getVisitorPhone());
                            getVisitorInfro.add(preschedule.getVisitorAdress());
                            getVisitorInfro.add(preschedule.getVisitorPurpose());
                            getVisitorInfro.add(preschedule.getVisitorCompay());
                            getVisitorInfro.add(preschedule.getEmployeeTosee());
                        }
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
                if(getVisitorInfro.get(0)==null){
                    Toast.makeText(VisitorAccessCode.this,"Invalided Access Code",Toast.LENGTH_LONG).show();
                }
                else {
                    if(getVisitorInfro.get(0).equals("Not Yet")){
                        linearLayout.setVisibility(View.VISIBLE);
                        linearlayout2.setVisibility(View.GONE);
                        name.setText(getVisitorInfro.get(1));
                        email.setText(getVisitorInfro.get(2));
                        phone.setText(getVisitorInfro.get(3));
                        address.setText(getVisitorInfro.get(4));
                        purpose.setText(getVisitorInfro.get(5));
                        company.setText(getVisitorInfro.get(6));
                        employeeTosee.setText(getVisitorInfro.get(7));
                    }

                    if(getVisitorInfro.get(0).equals("Sign_In")){

                        Toast.makeText(VisitorAccessCode.this, "Please  Sign Out ",Toast.LENGTH_LONG).show();
                        Intent backToMain= new Intent(VisitorAccessCode.this, SignInOut.class);
                        startActivity(backToMain);
                        finish();
                    }
                    else if (getVisitorInfro.get(0).equals("Sign_Out")){

                        Toast.makeText(VisitorAccessCode.this, "Used Access Code",Toast.LENGTH_LONG).show();
                        Intent backToMain= new Intent(VisitorAccessCode.this,WelcomePage.class);
                        startActivity(backToMain);
                        finish();
                    }
                }
            }
        }.execute();
    }

    public void CheckIn(View view) {

       final String AccessCode= getAccessCode();
        final  String []getStatus=new String[3];
        final String signin="Sign_In";
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    MobileServiceList<Preschedule>Plist=PTable.where().field("accesscode").eq(AccessCode).execute().get();
                    for (Preschedule preschedule:Plist){
                        getStatus[0]=preschedule.getEmployeeTosee();
                        getStatus[1]=preschedule.getVisitorName();
                        preschedule.setValidateaccess("Sign_In");
                        PTable.update(preschedule).get();
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
                ContactHost(getStatus[0],getStatus[1]);


            }
        }.execute();
    }

    public void ContactHost(final String employeeName, final String VisitorName){
        final String []getEmployeeEmail= new String[2];
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog=ProgressDialog.show(VisitorAccessCode.this,"Contacting Your Host",employeeName,false,false);
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
                    MobileServiceList<RegistrationForm>EList=EmTable.where().field("employeeName").eq(employeeName).execute().get();
                    for (RegistrationForm form:EList){
                        getEmployeeEmail[0]=form.getEmployeeEmail();
                        getEmployeeEmail[1]=form.getEmployeeName();
                    }

                    Message message= new MimeMessage(session);
                    message.setFrom(new InternetAddress(MailPassEmail.emailAddress));
                    message.setSubject("Visitor Notification");
                    message.setText("Dear  " + getEmployeeEmail[1] + "\n" +  VisitorName   + "  is at the reception waiting for you");
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(getEmployeeEmail[0]));
                    Transport.send(message);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dialog.dismiss();

                Intent intent= new Intent(VisitorAccessCode.this,WelcomePage.class);
                startActivity(intent);
                Toast.makeText(VisitorAccessCode.this,"Welcome ",Toast.LENGTH_LONG).show();
                finish();
            }
        }.execute();

    }


}
