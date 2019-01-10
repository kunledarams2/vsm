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
import android.widget.Toast;

import com.kunledarams.alejoversionspackage.Model.ActivityTable;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class CheckOut extends AppCompatActivity {
    private EditText accessCode;
    private MobileServiceClient mClient;
    private MobileServiceTable<Preschedule>PTable;
    private MobileServiceTable<ActivityTable>AcTable;
    private Button button;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        accessCode=(EditText)findViewById(R.id.editAccesscode);
        button=(Button)findViewById(R.id.checkOutBut);
        button.setVisibility(View.GONE);
        accessCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                boolean isReady=accessCode.getText().toString().length()>0;
                button.setVisibility(View.VISIBLE);
                button.setEnabled(isReady);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        try {
            mClient= new MobileServiceClient("https://emalejo.azurewebsites.net",this);

            PTable= mClient.getTable(Preschedule.class);
            AcTable=mClient.getTable(ActivityTable.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        dialog=new ProgressDialog(this);
    }


    public void Check_out(View view) {

      final String getAccessCode= accessCode.getText().toString().trim();
        final String []getStatus= new String[3];

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog= ProgressDialog.show(CheckOut.this,"Checking Out","Please wait checking you out....",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    MobileServiceList<Preschedule>PList=PTable.where().field("accesscode").eq(getAccessCode).execute().get();
                    for(Preschedule preschedule:PList){
                        getStatus[0]=preschedule.getValidateaccess();

                        if(getStatus[0].equals("Sign_In")){
                            preschedule.setValidateaccess("Sign_Out");
                            PTable.update(preschedule).get();
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
                if(getStatus[0]==null){
                    ValidateActivity(getAccessCode);
                }
                else {
                    if(getStatus[0].equals("Not Yet")){

                        Toast.makeText(CheckOut.this," Sign In ",Toast.LENGTH_LONG).show();
                        Intent Gointent= new Intent(CheckOut.this,SignInOut.class);
                        startActivity(Gointent);
                        finish();
                    }
                    else if(getStatus[0].equals("Sign_Out")){
                        Toast.makeText(CheckOut.this, "Used Access Code",Toast.LENGTH_LONG).show();
                        Intent Outintent = new Intent( CheckOut.this, SignInOut.class);
                        startActivity(Outintent);
                        finish();
                    }
                    else
                        Toast.makeText(getApplication(), "Thanks For Coming",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( CheckOut.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.execute();
    }

    public  void ValidateActivity( final String getAccCode){

        final String[]getStatus= new String[3];
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {

                dialog= ProgressDialog.show(CheckOut.this,"Checking Out","Please wait checking you out....",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    MobileServiceList<ActivityTable>AList=AcTable.where().field("accesscode").eq(getAccCode).execute().get();
                    for(ActivityTable tableList:AList){
                        getStatus[0]=tableList.getStatus();

                        if(getStatus[0].equals("Sign_In")){
                           tableList.setStatus("Sign_out");
                            AcTable.update(tableList).get();
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
                if(getStatus[0]==null){

                    Toast.makeText(CheckOut.this, "Invalided Access Code",Toast.LENGTH_LONG).show();

                }
                else {
                   if(getStatus[0].equals("Sign_Out")){
                        Toast.makeText(CheckOut.this, "Used Access Code",Toast.LENGTH_LONG).show();
                        Intent Outintent = new Intent( CheckOut.this, SignInOut.class);
                        startActivity(Outintent);
                        finish();
                    }
                    else
                        Toast.makeText(getApplication(), "Thanks For Coming",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( CheckOut.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }.execute();

    }
}
