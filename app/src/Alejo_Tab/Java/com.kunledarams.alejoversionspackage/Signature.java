package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.kunledarams.alejoversionspackage.Model.BasicRegistration;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;



public class Signature extends AppCompatActivity {

    private SignaturePad  signaturePad;
    private ArrayList<String>newListId;
    private BasicRegistration basicRegistration;
    private MobileServiceClient mClient;
    private MobileServiceTable<BasicRegistration>RGTable;
    private ProgressDialog dialog;
    private Service service;
    java.security.Signature signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        service=new Service();

        newListId=(ArrayList<String>)getIntent().getSerializableExtra("basicInfro");
        basicRegistration= new BasicRegistration();

        try {
            mClient= new MobileServiceClient("https://emalejo.azurewebsites.net",this);

           RGTable=mClient.getTable(BasicRegistration.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        signaturePad=(SignaturePad)findViewById(R.id.signturePad);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

            }
        });
    }

    public void AcceptButton(View view) {

        String accessCode= service.AccessGeneretor(7);
        // insert values into the table
        if(newListId.size()>0){
            newListId.add(accessCode);//8
        }
        Intent acceptSignature= new Intent(getApplicationContext(), ValidateCode.class);
        acceptSignature.putStringArrayListExtra("newArryList",(ArrayList<String>)newListId);
        Toast.makeText(getApplicationContext(),accessCode,Toast.LENGTH_LONG).show();
        startActivity(acceptSignature);
        finish();


    }

}
