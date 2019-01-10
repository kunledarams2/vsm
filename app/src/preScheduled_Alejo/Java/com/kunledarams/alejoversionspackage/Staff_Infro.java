package com.kunledarams.alejoversionspackage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.kunledarams.alejoversionspackage.Model.RegistrationForm;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class Staff_Infro extends AppCompatActivity {
    private TextView staffName, staffDepartment, staffEmail, staffPhone;
    private MobileServiceClient mClient;
    private MobileServiceTable<RegistrationForm> serviceTable;
    private String staffId;
    private ImageView imageView;
    private ArrayList<String>StaffInfro= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff__infro);

        staffName = (TextView) findViewById(R.id.staffName);
        staffDepartment = (TextView) findViewById(R.id.staffDepartment);
        staffPhone = (TextView) findViewById(R.id.staffPhone);
        staffEmail = (TextView) findViewById(R.id.staffEmail);
        imageView=(ImageView)findViewById(R.id.imageStaff);
        StaffInfro = (ArrayList<String>)getIntent().getSerializableExtra("staffInfro");
       //staffId = getIntent().getExtras().getString("staffId");

        try {
            mClient = new MobileServiceClient("https://emalejo.azurewebsites.net", this);
            serviceTable = mClient.getTable(RegistrationForm.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setView();
    }

    public void setView() {

        staffName.setText(StaffInfro.get(0));
        staffDepartment.setText(StaffInfro.get(1));
        staffPhone.setText(StaffInfro.get(2));
        staffEmail.setText(StaffInfro.get(3));

        ColorGenerator generator= ColorGenerator.MATERIAL;
        String firstLetter= String.valueOf(StaffInfro.get(0).charAt(0));
        TextDrawable drawable=TextDrawable.builder().buildRect(firstLetter,generator.getRandomColor());
        imageView.setImageDrawable(drawable);

        staffPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + StaffInfro.get(2)));

                if (ActivityCompat.checkSelfPermission(Staff_Infro.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);

            }
        });


       /* final String[] getItem = new String[4];
        if (isOnline()) {

            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    try {
                        List<RegistrationForm> staffInfro = serviceTable.where().field("employeeName").eq(staffId).execute().get();
                        for (RegistrationForm item : staffInfro) {
                            getItem[0] = item.getEmployeeName();
                            getItem[1] = item.getEmployeeDepartment();
                            getItem[2] = item.getEmployeePhone();
                            getItem[3] = item.getEmployeeEmail();
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



                }
            };

            runAsyncTask(task);
        }

        final String[] TO = {getItem[3]};
        String[] CC = {""};

        staffEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, TO);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try{
                    startActivity(Intent.createChooser(intent, "Send mail..."));

                }catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Staff_Infro.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

               // finish();
            }

        });*/

        // Image view setting

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
