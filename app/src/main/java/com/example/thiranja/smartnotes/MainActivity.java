package com.example.thiranja.smartnotes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializing the mobile add service from addmob

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");

        // Setting the text view

        final TextView loadingtxt = (TextView) findViewById(R.id.loadingtxt);

        loadingtxt.setText("Loading");

        final Thread loading = new Thread() {
            public void run() {
                try {
                    sleep(500);
                    Intent home = new Intent("android.intent.action.HOME");
                    startActivity(home);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };

        loading.start();

    }
}
