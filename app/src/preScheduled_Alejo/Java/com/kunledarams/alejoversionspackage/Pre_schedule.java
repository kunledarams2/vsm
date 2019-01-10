package com.kunledarams.alejoversionspackage;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.kunledarams.alejoversionspackage.Utili.EmailPassword;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Pre_schedule extends AppCompatActivity {

    private Spinner PurposeSpinner, EmployeeSpinner ;
    private LinearLayout fromWhereLayout, companyLayout, itemLayout, toWhomLayout, eventlayout,
            WhomToseeLayout,WhomToseeSpinner,layoutDate,layouttime;
    private EditText visitorName, visitorPhone,visitorEmail, visitorAddress, employeeId, EditDate, visitorCompany;
    private TextView meetDate,evetDate,PickDate;
    private Preschedule visitorInfro;
    private MobileServiceClient client;
    private MobileServiceTable<Preschedule>tablePre_schedule;
    private MobileServiceTable<RegistrationForm>serviceTable;
    private ProgressDialog dialog;
    private Session session = null;
    private ArrayList<String>  userId= new ArrayList<>();
    private static SecureRandom secureRandom = new SecureRandom();
    private Toolbar toolbar;
   // private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private LinkedList<String> EmployeeInfro= new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_schedule);

        //casting visitor information
        visitorName = (EditText)findViewById(R.id.visitorName);
        visitorEmail = (EditText)findViewById(R.id.visitorEmail);
        visitorPhone = (EditText)findViewById(R.id.visitorPhone);
        visitorAddress = (EditText)findViewById(R.id.visitorAddress);
        visitorCompany= (EditText)findViewById(R.id.userCompany);
        employeeId = (EditText)findViewById(R.id.EmployeeId);
        employeeId.setVisibility(View.GONE);

        meetDate=(TextView)findViewById(R.id.MeetingDate);
        meetDate.setVisibility(View.GONE);

        PurposeSpinner=(Spinner)findViewById(R.id.spinnerpurpose);

        fromWhereLayout = (LinearLayout)findViewById(R.id.layoutFromwhere);
        fromWhereLayout.setVisibility(View.GONE);

        companyLayout = (LinearLayout)findViewById(R.id.layoutcompanyName);
        companyLayout.setVisibility(View.GONE);

        itemLayout = (LinearLayout)findViewById(R.id.layoutItem);
        itemLayout.setVisibility(View.GONE);


        eventlayout= (LinearLayout)findViewById(R.id.LayoutEventView);
        eventlayout.setVisibility(View.GONE);

        layoutDate=(LinearLayout)findViewById(R.id.layoutdate);
        layoutDate.setVisibility(View.GONE);

        layouttime=(LinearLayout)findViewById(R.id.layoutTime);
        layouttime.setVisibility(View.GONE);

        EditDate=(EditText)findViewById(R.id.dateid);
        EditDate.setInputType(InputType.TYPE_NULL);


        secureRandom= new SecureRandom();

        visitorInfro = new Preschedule();

        toolbar=(Toolbar)findViewById(R.id.toolbarNewv);
        setSupportActionBar(toolbar);



        Purpose();

        try {
            client= new MobileServiceClient("https://veemo.azurewebsites.net",this);

            client.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            serviceTable = client.getTable(RegistrationForm.class);
            tablePre_schedule=client.getTable(Preschedule.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //getting inputId
        Intent getUseId= getIntent();
        userId= getUseId.getStringArrayListExtra("userId");
        employeeId.setText(userId.get(0));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Pre_schedule.this,Pre_MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        visitorInfro.setEmployeeTosee(userId.get(1));

    }


    public void Purpose(){

        List <String>purpose = new ArrayList<>();
        purpose.add("Select_Purpose");
        purpose.add("Meeting");
        purpose.add("Pick-up Or Delivery");
        purpose.add("Service And Maintenance");

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,purpose);
         arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        PurposeSpinner.setAdapter(arrayAdapter);
        PurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String itemSelector = adapterView.getItemAtPosition(position).toString();

                if(itemSelector.equals("Select_Purpose")){
                    companyLayout.setVisibility(View.GONE);


                    fromWhereLayout.setVisibility(View.GONE);
                    itemLayout.setVisibility(View.GONE);

                    meetDate.setVisibility(View.GONE);
                    layoutDate.setVisibility(View.GONE);

                }
                if(itemSelector.equals("Meeting")){
                    companyLayout.setVisibility(View.VISIBLE);
                    fromWhereLayout.setVisibility(View.GONE);
                    itemLayout.setVisibility(View.GONE);
                    meetDate.setVisibility(View.VISIBLE);
                    layoutDate.setVisibility(View.VISIBLE);
                    visitorInfro.setVisitorPurpose("Meeting");

                }

                if(itemSelector.equals("Pick-up Or Delivery")){
                    fromWhereLayout.setVisibility(View.VISIBLE);
                    itemLayout.setVisibility(View.VISIBLE);

                    companyLayout.setVisibility(View.GONE);

                    meetDate.setVisibility(View.GONE);
                    layoutDate.setVisibility(View.VISIBLE);
                    visitorInfro.setVisitorPurpose("Pick-up or Delivery");
                }
                if(itemSelector.equals("Service And Maintenance")){
                    companyLayout.setVisibility(View.VISIBLE);
                    fromWhereLayout.setVisibility(View.GONE);
                    itemLayout.setVisibility(View.GONE);
                    meetDate.setVisibility(View.VISIBLE);
                    layoutDate.setVisibility(View.VISIBLE);
                    visitorInfro.setVisitorPurpose("Service And Maintenance");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void EployeeName (){


        EmployeeInfro.addFirst("Select Employee Name");


        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, EmployeeInfro);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        EmployeeSpinner.setAdapter(adapter);

        EmployeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                final String itemSelector = adapterView.getItemAtPosition(i).toString();

               if(!itemSelector.equals("Select Employee Name")){
                   visitorInfro.setEmployeeTosee(itemSelector);
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void savedata(View view){

        final String enterName= visitorName.getText().toString().trim();
        final String enterPhone = visitorPhone.getText().toString().trim();
        final String enterEmail = visitorEmail.getText().toString().trim();
        final String enterAddress = visitorAddress.getText().toString().trim();
        final String enterVisitorCompany= visitorCompany.getText().toString().trim();

        final String enterEmployeeId = userId.get(0).trim();
        visitorInfro.setVisitorName(enterName);
        visitorInfro.setVisitorEmail(enterEmail);
        visitorInfro.setVisitorPhone(enterPhone);
        visitorInfro.setVisitorAdress(enterAddress);
        visitorInfro.setVisitorCompay(enterVisitorCompany);

        // visitorInfro.setVisitorid(visitorId);
        visitorInfro.setEmployeeid(enterEmployeeId);



        // check all condition (network and if all the infromation is complete enter)
        if(!TextUtils.isEmpty(enterName)&&!TextUtils.isEmpty(enterEmail)&& !TextUtils.isEmpty(enterPhone) &&!TextUtils.isEmpty(enterEmployeeId)){
            final  String accessPoint = randomAlphaNumeric(7);
            final  String accessCode = secureRandom.toString();

            visitorInfro.setAccesscode(accessPoint);
            visitorInfro.setValidateaccess("Not Yet");
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void,Void>task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {

                dialog = ProgressDialog.show(Pre_schedule.this, "Pre_schedule","Process....", false,false);
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
                        return new PasswordAuthentication(EmailPassword.Email, EmailPassword.password);
                    }
                });
                try {
                    // condition to send message if ()
                    // sending emails
                if(!visitorInfro.getVisitorPurpose().isEmpty()) {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(EmailPassword.Email)); // email of particular employee scheduling appointment
                        message.setSubject("Alejo Notification");
                        message.setText("Dear  " + enterName + "\n"
                                + "You have been scheduled for " + visitorInfro.getVisitorPurpose() + "\n" + "Location: Lotus Beta Analytics Lekki Phase 1 Lagos" + "\n" + "Purpose:" + visitorInfro.getVisitorPurpose()
                                + "\n" + "Date:" + visitorInfro.getPreScheduleDate() + "\n" + "Access Code:" + accessPoint );
                        message.setRecipient(Message.RecipientType.TO, new InternetAddress(enterEmail));
                    Transport.send(message);

                    }

                    // sending sms to the visitor phone

                    tablePre_schedule.insert(visitorInfro).get();

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
                if(!TextUtils.isEmpty(enterPhone)){
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(enterPhone, null, "Dear" + enterName + "\n"
                                + "You have been scheduled for " + visitorInfro.getVisitorPurpose() + "\n" + "Location: " + "\n" + "Purpose:" + visitorInfro.getVisitorPurpose()
                                + "\n" + "Date:"+visitorInfro.getPreScheduleDate()  + "\n" + "Access Code:" + accessPoint, null, null);

                    }catch (Exception e){
                        Toast.makeText(getApplication(), " SMS Error " + e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                }


                dialog.dismiss();
                Intent backTo = new Intent(Pre_schedule.this,Pre_MainActivity.class);
                backTo.putExtra("userId", userId);
                startActivity(backTo);
                finish();
            }
        };
        runAsyncTask(task);

        }else {
            Intent backTo = new Intent(Pre_schedule.this,Pre_MainActivity.class);
            backTo.putExtra("userId", userId);
            startActivity(backTo);

        }
    }

    private AsyncTask<Void,Void,Void> initLocalValue(){
        AsyncTask<Void,Void,Void>sync= new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {


                return null;
            }
        };
        return null;
    }


    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public void PickDate(View view) {
      //  GregorianCalendar calendar= new GregorianCalendar();
        final Calendar calendar= Calendar.getInstance();
        int Dayofyear= calendar.get(Calendar.DAY_OF_MONTH); // current day
        int mMonth = calendar.get(Calendar.MONTH);// current month
        int mYear = calendar.get(Calendar.YEAR);// current year

        datePickerDialog= new DatePickerDialog(Pre_schedule.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int dayDate, int Month, int Year) {

                EditDate.setText(Year + "-" + (Month +1)+ "-" + dayDate);
                visitorInfro.setPreScheduleDate((Year + "-" + (Month +1)+ "-" + dayDate));// preschedule date
            }

        },Dayofyear,mMonth,mYear

        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    @SuppressLint("StaticFieldLeak")
    public void GetEmployee(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    MobileServiceList<RegistrationForm> FormList=serviceTable.where().field("DELETED").eq("false").execute().get();
                    for(RegistrationForm form:FormList){
                        EmployeeInfro.add(form.getEmployeeName());//8
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
                EployeeName ();
            }
        }.execute();
    }

    public void PickTime(View view) {

        final Calendar calendar= Calendar.getInstance();
        int hour= calendar.get(Calendar.HOUR_OF_DAY);
        int mins=calendar.get(Calendar.MINUTE);

        timePickerDialog= new TimePickerDialog(Pre_schedule.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {


            }
        },hour,mins,true);
        timePickerDialog.show();

    }
}
