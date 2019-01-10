package com.kunledarams.alejoversionspackage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeSplash extends AppCompatActivity {
    private ImageView getImage;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash);
        getImage= (ImageView)findViewById(R.id.imageView);
         Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
         getImage.setAnimation(animation);
         progressBar=(LinearLayout) findViewById(R.id.linProgress);


        Thread timer= new Thread(){

            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(isOnline()){

                        Intent signalTab = new Intent(getApplicationContext(),Login_Form.class);
                        startActivity(signalTab);
                    }else {

                     Intent refreshPage= new Intent(WelcomeSplash.this, ErrorPage.class);
                     startActivity(refreshPage);

                    }

                }

            }

        };

        timer.start();

    }



    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
