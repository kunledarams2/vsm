package com.kunledarams.alejoversionspackage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceActivityResult;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class Authication extends AppCompatActivity implements View.OnClickListener {

    private MobileServiceClient mclient;
    private  String currentUser;
    private Button mAuthButton, RegButton,  LoginButton;
    public static final int GOOGLE_LOGIN_REQUEST_CODE = 1;
    public Dialog createAndShowDialog;
   // public Registr_form registr_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authication);


        mAuthButton =(Button) findViewById(R.id.button);
        mAuthButton.setOnClickListener(this);

        LoginButton= (Button)findViewById(R.id.button3);
        LoginButton.setOnClickListener(this);

       // registr_form= new Registr_form();
        
        createAndShowDialog= new Dialog(this);


        try {
            mclient = new MobileServiceClient("https://ealejo.azurewebsites.net",this);
            mclient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {

                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;

                }
            });

        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    public  void mAuth(){


        mclient.login(MobileServiceAuthenticationProvider.MicrosoftAccount, new UserAuthenticationCallback() {
            @Override
            public void onCompleted(MobileServiceUser user, Exception exception, ServiceFilterResponse response) {
                if(exception== null){

                  currentUser= user.getUserId();
                    Intent intent = new Intent(getApplicationContext(),Pre_MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),exception.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button:
                Intent linlToRegis = new Intent(getApplicationContext(), Registr_form.class);
                startActivity(linlToRegis);
                break;

            case R.id.button3:
                    mAuth();
                    break;


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When request completes

                if (resultCode == RESULT_OK) {
                    // Check the request code matches the one we send in the login request
                    if (requestCode == GOOGLE_LOGIN_REQUEST_CODE) {
                        MobileServiceActivityResult result = mclient.onActivityResult(data);
                        if (result.isLoggedIn()) {
                            // login succeeded
                           // InsertInfro();
                           // RegButton.setVisibility(View.VISIBLE);
                           //createAndShowDialog(String.format("You are now logged in - %1$2s", mclient.getCurrentUser().getUserId()), "Success");
                            Toast.makeText(this,"Successful",Toast.LENGTH_LONG).show();

                        } else {
                            // login failed, check the error message
                            String errorMessage = result.getErrorMessage();
                            // createAndShowDialog(errorMessage, "Error");
                        }
                    }

        }

    }

    private void createAndShowDialog(String format, String success) {
    }
}
