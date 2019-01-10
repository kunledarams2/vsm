package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ConnectVisitor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_visitor);
    }

    public void Home(View view) {

        Intent backHome= new Intent(getApplicationContext(),WelcomePage.class);
        startActivity(backHome);
        finish();
    }
}
