package com.kunledarams.alejoversionspackage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by ok on 9/25/2018.
 */

public class ValidateCode extends AppCompatActivity {
    private ArrayList<String>BasicList;
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkcode);
        editText=(EditText)findViewById(R.id.editText4);

        BasicList= (ArrayList<String>) getIntent().getSerializableExtra("newArryList");
        if(BasicList.size()>0){
            editText.setText(BasicList.get(8));
        }

    }

    public void CheclAccess(View view) {

        Intent authenticateUser= new Intent(this, Purpose.class);
        authenticateUser.putStringArrayListExtra("BasicList",(ArrayList<String>)BasicList);
        startActivity(authenticateUser);
        finish();
    }

}
