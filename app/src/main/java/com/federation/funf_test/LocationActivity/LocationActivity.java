package com.federation.funf_test.LocationActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.R;
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

public class LocationActivity extends Activity {
    TextView locationTextView;
    FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList params = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/location/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_log);
        locationTextView = (TextView) findViewById(R.id.location_TextView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(LocationActivity.this, "Permission denied to read your Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        locationTextView.setText("");
                        locationTextView.append("Latitude: " + addresses.get(0).getLatitude() + "\nLongitude: " + addresses.get(0).getLongitude() + "\nAddress: " + addresses.get(0).getAddressLine(0));

                        String androidId = Settings.Secure.getString(getContentResolver(),
                                Settings.Secure.ANDROID_ID);

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
