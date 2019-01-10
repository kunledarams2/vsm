package com.kunledarams.alejoversionspackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kunledarams.alejoversionspackage.Model.BasicRegistration;
import com.kunledarams.alejoversionspackage.Model.Preschedule;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PreBookRegBasic extends AppCompatActivity {
    private EditText name, phone, address, email, company,gender;
    private TextView vName, vPhone,vGender,vEmail,vCompany;
    private Button SearchButton, PreBookButton;
    private MobileServiceClient mClient;
    private MobileServiceTable<BasicRegistration>REGTable;
    private MobileServiceTable<Preschedule>PRETable;
    private ProgressDialog dialog;
    private ArrayList<String>VisitorInfo=new ArrayList<>();
    private ArrayList<String>VisitorInfroPre= new ArrayList<>();
    private ArrayList<String>PassList=new ArrayList<>();
    private Spinner spinner;
    private LinearLayout firstLinearLayout, secondLinearLayout, ThirdLinearLayout,nameLinearLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_book_reg_basic);

        PreBookButton=(Button)findViewById(R.id.PreBookRegistration) ;
        SearchButton=(Button)findViewById(R.id.search_Butt1);
        SearchButton.setVisibility(View.GONE);
        firstLinearLayout=(LinearLayout)findViewById(R.id.layoutBasic1);
        secondLinearLayout=(LinearLayout)findViewById(R.id.layoutBasic2);
        ThirdLinearLayout=(LinearLayout)findViewById(R.id.registrationView);
        nameLinearLayout=(LinearLayout)findViewById(R.id.nameLayout);
        phone= (EditText)findViewById(R.id.Phone);
        address=(EditText)findViewById(R.id.Address);
        email=(EditText)findViewById(R.id.Email);
        company=(EditText)findViewById(R.id.Company);
        spinner=(Spinner)findViewById(R.id.GenderUpdate);

        vName=(TextView)findViewById(R.id.vistName);
        vPhone=(TextView)findViewById(R.id.vistMobile);
        vCompany=(TextView)findViewById(R.id.vistCompany);
        vEmail=(TextView)findViewById(R.id.visitEmail);
        vGender=(TextView)findViewById(R.id.gender);
        imageView=(ImageView)findViewById(R.id.circleImageView2);


        name =(EditText)findViewById(R.id.SearchName);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                boolean isReady=name.getText().toString().length()>0;
                SearchButton.setVisibility(View.VISIBLE);
                SearchButton.setEnabled(isReady);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        try {
            mClient=new MobileServiceClient("https://emalejo.azurewebsites.net",this);
            REGTable=mClient.getTable(BasicRegistration.class);
            PRETable=mClient.getTable(Preschedule.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        dialog=new ProgressDialog(this);
        GenderUpdate();

    }


    public void SearchVisitor(View view) {

        final String VistName= name.getText().toString().trim();

        final String[]getName= new String[11];

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog=ProgressDialog.show(PreBookRegBasic.this,"Search","Please Wait",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    MobileServiceList<BasicRegistration>BR=REGTable.where().field("visitorName").eq(VistName).execute().get();
                    for(BasicRegistration registration:BR ){
                        VisitorInfo.add(registration.getVistorGender());//0
                        VisitorInfo.add("Mr/Mrs");//1
                        VisitorInfo.add(registration.getVisitorName()); //2
                        VisitorInfo.add(registration.getVisitorPhone()); //3
                        VisitorInfo.add(registration.getVisitorEmail()); //4
                        VisitorInfo.add(registration.getVisitorAddress());//5
                        VisitorInfo.add(registration.getVisitorCompany());//6
                        VisitorInfo.add(registration.getVistorImage());//7


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
                if(VisitorInfo.size()==0){
                    Toast.makeText(PreBookRegBasic.this,"Please Wait",Toast.LENGTH_LONG).show();
                    PresTable(VistName);

                }
                else {
                    vName.setText(VisitorInfo.get(2));
                    vPhone.setText(VisitorInfo.get(3));
                    vCompany.setText(VisitorInfo.get(6));
                    vGender.setText(VisitorInfo.get(0));
                    Glide.with(PreBookRegBasic.this)
                            .load(VisitorInfo.get(7))
                            .placeholder(R.drawable.ic_person_black_24dp)
                            .into(imageView);
                    nameLinearLayout.setVisibility(View.GONE);
                    SearchButton.setVisibility(View.GONE);
                    ThirdLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(PreBookRegBasic.this,"You Exit",Toast.LENGTH_LONG).show();
                }

            }
        }.execute();
    }


    public void PresTable(final String searchName){
        final String[]getPreVist=new String[10];

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog=ProgressDialog.show(PreBookRegBasic.this,"Please Wait","........",false,false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    MobileServiceList<Preschedule>presList=PRETable.where().field("visitorname").eq(searchName).execute().get();
                    for(Preschedule preschedule:presList){

                        VisitorInfroPre.add(preschedule.getVisitorName());//0
                        VisitorInfroPre.add(preschedule.getVisitorPhone());//1
                        VisitorInfroPre.add(preschedule.getVisitorEmail());//2
                        VisitorInfroPre.add(preschedule.getVisitorAdress());//3
                        VisitorInfroPre.add(preschedule.getVisitorCompay());//4

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
                if(VisitorInfroPre.size()==0){
                    Intent goToNewVisitor=new Intent(PreBookRegBasic.this,BasicInfro.class);
                    startActivity(goToNewVisitor);
                    Toast.makeText(PreBookRegBasic.this,"Please Register",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                    firstLinearLayout.setVisibility(View.VISIBLE);
                    secondLinearLayout.setVisibility(View.VISIBLE);
                    SearchButton.setVisibility(View.GONE);
                    PreBookButton.setVisibility(View.VISIBLE);
                    phone.setText(VisitorInfroPre.get(1));
                    email.setText(VisitorInfroPre.get(2));
                    address.setText(VisitorInfroPre.get(3));
                    company.setText(VisitorInfroPre.get(4));
                    /** Setting the view of basic infromation about the prebooked visitor for n
                     * normal registration**/
                    Toast.makeText(PreBookRegBasic.this,"Welcome Back",Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    public void nextRegistration(View view) {

        //generate access <code>
        // jjjlkll;;</code>
        VisitorInfo.add(Service.AccessGeneretor(7));
        Intent intent= new Intent(this,ValidateCode.class);
        intent.putStringArrayListExtra("newArryList",(ArrayList<String>)VisitorInfo);
        startActivity(intent);
        finish();
    }

    public void PreBookRegistration(View view) {

        PassList.add(VisitorInfroPre.get(0));
        PassList.add(VisitorInfroPre.get(1));
        PassList.add(VisitorInfroPre.get(2));
        PassList.add(VisitorInfroPre.get(3));
        PassList.add(VisitorInfroPre.get(4));
//        PassList.add(VisitorInfroPre.get(5));
        Intent GoToCamera= new Intent(this, CaptureUmage.class);
        GoToCamera.putStringArrayListExtra("basicList",(ArrayList<String>)PassList);
        Toast.makeText(this,PassList.get(0),Toast.LENGTH_LONG).show();
        startActivity(GoToCamera);
        finish();
    }

    public void GenderUpdate(){

        List<String>gender= new ArrayList<>();
        gender.add("Select Gender");
        gender.add("Male");
        gender.add("Female");

        ArrayAdapter<String>adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selector = adapterView.getItemAtPosition(i).toString();
                if(selector.equals("select Gender")){
                }
                if(selector.equals("Male")){

                    PassList.add("Male");
                    PassList.add("Mr");
                }
                if(selector.equals("Female")){
                    PassList.add("Female");
                    PassList.add("Mrs");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
