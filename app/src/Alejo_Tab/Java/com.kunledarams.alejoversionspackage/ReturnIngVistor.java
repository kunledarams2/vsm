package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ReturnIngVistor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_ing_vistor);
    }

    public void nextButton(View view) {
        Intent next= new Intent(this,Purpose.class);
        startActivity(next);
        finish();
    }


}
