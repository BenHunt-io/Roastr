package com.exetxstate.bullytoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();



        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                    Intent goToMain = new Intent("com.exetxstate.MAINACTIVITY");
                    startActivity(goToMain);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };
        timer.start();
    }
}