package com.federation.funf_test.LocationActivity;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.federation.funf_test.JSONParser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationIntentService extends BroadcastReceiver {
    ArrayList params = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/location/create";
    private static final String TAG_SUCCESS = "success";

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
            String androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            params.add(new BasicNameValuePair("device_id", androidId));
            getLocation(context);

            Toast.makeText(context, "GPS Changing", Toast.LENGTH_SHORT).show();

            new CreateNewResult().execute();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation(final Context context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        params.add(new BasicNameValuePair("latitude", Double.toString(addresses.get(0).getLatitude())));
                        params.add(new BasicNameValuePair("longitude", Double.toString(addresses.get(0).getLongitude())));
                        params.add(new BasicNameValuePair("address", addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
