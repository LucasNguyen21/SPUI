package com.federation.funf_test;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

import static android.content.ContentValues.TAG;

public class SmsLogActivity extends Activity {

    ListView smsLogListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_log_view);
        smsLogListView = (ListView) findViewById(R.id.sms_log_listview_id);

        ActivityCompat.requestPermissions(SmsLogActivity.this,
                new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS},
                1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showCallLog();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SmsLogActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void showCallLog(){
        ArrayList<String> callArrayList= new ArrayList<>();

        TelephonyProvider telephonyProvider = new TelephonyProvider(this);
        List<Sms> smsList = telephonyProvider.getSms(TelephonyProvider.Filter.ALL).getList();


        for (Sms i : smsList) {
            callArrayList.add("Subject:" + i.subject + "\nBody: " + i.body + "\nAddress: " + i.address + "\n Received Date: " + i.receivedDate + "\nPerson: " + i.person + "\nSent Date: " + i.sentDate  + "\nSeen: " + i.seen + "\nRead: " + i.read + "\nStatus: " + i.status);
            Log.d(TAG, "showCallLog: " + i.body);
        }
        callArrayList.add("Test 1");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, callArrayList);

        smsLogListView.setAdapter(arrayAdapter);
    }
}
