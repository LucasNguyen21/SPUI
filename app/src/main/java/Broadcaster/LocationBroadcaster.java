package Broadcaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.LocationActivity.LocationActivity;
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

public class LocationBroadcaster extends BroadcastReceiver {

    FusedLocationProviderClient fusedLocationProviderClient;
    ArrayList params = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/location/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onReceive(Context context, Intent intent) {
        getLocation(context);
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

//                        locationTextView.setText("");
//                        locationTextView.append("Latitude: " + addresses.get(0).getLatitude() + "\nLongitude: " + addresses.get(0).getLongitude() + "\nAddress: " + addresses.get(0).getAddressLine(0));
//
//                        Double latitude

                        Log.d("LOCATION_CHECK", "Location broadcast: " + addresses.get(0).getAddressLine(0));

                        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        params.add(new BasicNameValuePair("device_id", androidId));

                        params.add(new BasicNameValuePair("latitude", Double.toString(addresses.get(0).getLatitude())));
                        params.add(new BasicNameValuePair("longitude", Double.toString(addresses.get(0).getLongitude())));
                        params.add(new BasicNameValuePair("address", addresses.get(0).getAddressLine(0)));
                        new CreateNewResult().execute();

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
