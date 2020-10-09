package Broadcaster;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;

import com.federation.funf_test.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccelerometerBroadcaster extends BroadcastReceiver {
    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/sensor/create";
    private static final String TAG_SUCCESS = "success";

    private UsageStatsManager mUsageStatsManager;
    private PackageManager mPm;

    ArrayList params = new ArrayList();
    ArrayList appUsageList = new ArrayList();

    @Override
    public void onReceive(Context context, Intent intent) {
        getAppUsage(context);
    }

    @SuppressLint("MissingPermission")
    public void getAppUsage(final Context context){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);

        mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        mPm = context.getPackageManager();

        final List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, cal.getTimeInMillis(), System.currentTimeMillis());

        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        final int statCount = queryUsageStats.size();
        for (int i = 0; i < statCount; i++) {
            final UsageStats pkgStats = queryUsageStats.get(i);
            JSONObject appObject = new JSONObject();

            // load application labels for each application
            try {
                ApplicationInfo appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0);
                String label = appInfo.loadLabel(mPm).toString();

                appObject.put("device_id", androidId);
                appObject.put("app_name", label);
                appObject.put("last_used", pkgStats.getLastTimeUsed());
                appObject.put("usage_time", pkgStats.getTotalTimeInForeground());

                appUsageList.add(appObject);

            } catch (PackageManager.NameNotFoundException | JSONException e) {
                // This package may be gone.
            }
        }

        params.add(new BasicNameValuePair("device_id", androidId));
        params.add(new BasicNameValuePair("app_list", appUsageList.toString()));
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
