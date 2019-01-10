package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrorPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_page);
    }

    public void REfresh(View view) {
        Intent intent= new Intent(this, WelcomeSplash.class);
        startActivity(intent);

    }
}
