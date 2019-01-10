package com.kunledarams.alejoversionspackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCenter.start(getApplication(),"{5c462855-bcc5-40f3-a8a9-500ff0681648}",Analytics.class, Crashes.class);

        if(Contant.type == Contant.Type.ALEJO_TAB){

        }else {

        }

    }


}
