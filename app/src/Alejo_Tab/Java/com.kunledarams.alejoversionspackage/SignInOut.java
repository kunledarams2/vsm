package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignInOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_out);
    }

    public void Sign_In(View view) {

        Intent sign_in= new Intent(this, WelcomePage.class);
        startActivity(sign_in);

    }

    public void Sign_out(View view) {
        Intent intent= new Intent(this, CheckOut.class);
        startActivity(intent);
    }
}
