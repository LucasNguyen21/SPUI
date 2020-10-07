package Broadcaster;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.provider.Settings;
import android.util.Log;

import com.federation.funf_test.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class BatteryBroadcaster extends BroadcastReceiver {

    ArrayList params = new ArrayList();
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/battery/create";
    private static final String TAG_SUCCESS = "success";
    public static final String TAG = "BATTERY LOG";
    float latestBatteryPct = 0;
    String latestChargingStatus;

    @Override
    public void onReceive(Context context, Intent intent) {

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;

        String chargingStatus;

        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        params.add(new BasicNameValuePair("device_id", androidId));
        params.add(new BasicNameValuePair("current_percentage",  Float.toString(batteryPct)));

        if(isCharging == true) {
            Log.d(TAG, "Charging");
            chargingStatus = "Charging";

            if(usbCharge == true){
                //PUSH DATE TIME +batteryPct % + charging status TO SERVER
                Log.d(TAG, "USB Charge");
                chargingStatus = "USB Charge";
            }

            if(acCharge == true){
                //PUSH DATE TIME + batteryPct% + charging status TO SERVER
                Log.d(TAG, "AC Charge");
                chargingStatus = "AC Charge";
            }
        } else {
            Log.d(TAG, "Not Charging");
            chargingStatus = "Not Charging";
        }

        //CONDITION TO PUT TO SERVER
        if(batteryPct < 10) {
            //PUSH DATE TIME + batteryPct% + isCharging status TO SERVER
        }

        if(batteryPct > 90) {
            //PUSH DATE TIME + batteryPct% + isCharging status TO SERVER
        }

        params.add(new BasicNameValuePair("charging_status", chargingStatus));

        //CONDITION TO PUT TO SERVER
        if (latestBatteryPct != batteryPct || latestChargingStatus != chargingStatus)
            new CreateNewResult().execute();

        latestBatteryPct = batteryPct;
        latestChargingStatus = chargingStatus;
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
