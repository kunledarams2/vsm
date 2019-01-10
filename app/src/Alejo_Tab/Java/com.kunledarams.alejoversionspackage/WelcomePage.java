package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }

    public void NewVisitor(View view) {
        Intent enterNewVisitor= new Intent(this, BasicInfro.class);
        startActivity(enterNewVisitor);
        finish();
    }

    @Override
    public void onClick(View view) {

    }

    public void ReturningVisitor(View view) {

        Intent returnVisitor= new Intent(this,PreBookRegBasic.class);
        startActivity(returnVisitor);
        finish();
    }

    public void AccessCode(View view) {
        Intent VisitorCode= new Intent(this,VisitorAccessCode.class);
        startActivity(VisitorCode);
        finish();
    }
}
