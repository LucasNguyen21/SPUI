package com.federation.funf_test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private Button appUsageBtn;
    private Button batteryBtn;
    private Button callLogBtn;
    private Button smsLogBtn;
    private Button accelerometerBtn;
    private Button gpsBtn;
    private Button browserBtn;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        appUsageBtn = (Button) findViewById(R.id.app_usage_btn);

        appUsageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(MainActivity.this, AppUsageActivity.class);
                startActivity(appUsageIntent);
            }
        });

        batteryBtn = (Button) findViewById(R.id.batteryBtn);

        batteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(MainActivity.this, BatteryActivity.class);
                startActivity(appUsageIntent);
            }
        });

        smsLogBtn = (Button) findViewById(R.id.sms_log_btn);

        smsLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(MainActivity.this, SmsLogActivity.class);
                startActivity(appUsageIntent);
            }
        });

        callLogBtn = (Button) findViewById(R.id.call_log_btn);

        callLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(MainActivity.this, CallLogActivity.class);
                startActivity(appUsageIntent);
            }
        });

        accelerometerBtn = (Button) findViewById(R.id.accelerometer_btn);

        accelerometerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(MainActivity.this, AccelerometerActivity.class);
                startActivity(appUsageIntent);
            }
        });

//        gpsBtn = (Button) findViewById(R.id.gpsBtn);
//
//        gpsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent appUsageIntent = new Intent(MainActivity.this, LocationActivity.class);
//                startActivity(appUsageIntent);
//            }
//        });

//        browserBtn = (Button) findViewById(R.id.browserBtn);
//
//        browserBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent appUsageIntent = new Intent(MainActivity.this, BrowserActivity.class);
//                startActivity(appUsageIntent);
//            }
//        });

    }


}
