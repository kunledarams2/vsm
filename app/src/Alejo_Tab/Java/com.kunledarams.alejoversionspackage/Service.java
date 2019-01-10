package com.kunledarams.alejoversionspackage;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by ok on 9/18/2018.
 */

public class Service {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public  static  String AccessGeneretor(int count){
        StringBuilder builder= new StringBuilder();

        while (count--!=0){
            int charater= (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(charater));
        }
        return builder.toString();
    }




}
