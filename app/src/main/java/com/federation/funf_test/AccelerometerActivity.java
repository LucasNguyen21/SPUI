package com.federation.funf_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.federation.funf_test.stroop.StroopResultActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AccelerometerActivity extends Activity implements SensorEventListener {
    TextView accelorometerTextView;
    private SensorManager sensorManager;
    double ax, ay, az;
    double gx, gy, gz;
    private static DecimalFormat df = new DecimalFormat("0.0000"); // Modify it for more accurate

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/sensor/create";
    private static final String TAG_SUCCESS = "success";
    ArrayList params = new ArrayList();

    String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_log);
        accelorometerTextView = (TextView) findViewById(R.id.accelerometerTextView);

        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            if (df.format(ax) != df.format(event.values[0]) && df.format(ay) != df.format(event.values[1]) && df.format(az) != df.format(event.values[2])) {
                ax = event.values[0];
                ay = event.values[1];
                az = event.values[2];


                params.add(new BasicNameValuePair("device_id", androidId));
                params.add(new BasicNameValuePair("x", df.format(ax)));
                params.add(new BasicNameValuePair("y", df.format(ay)));
                params.add(new BasicNameValuePair("z", df.format(az)));
                params.add(new BasicNameValuePair("type", "Accelerometer"));

                new CreateNewResult().execute();
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            if (df.format(gx) != df.format(event.values[0]) && df.format(gy) != df.format(event.values[1]) && df.format(gz) != df.format(event.values[2])) {
                gx = event.values[0];
                gy = event.values[1];
                gz = event.values[2];

                params.add(new BasicNameValuePair("device_id", androidId));
                params.add(new BasicNameValuePair("x", df.format(gx)));
                params.add(new BasicNameValuePair("y", df.format(gy)));
                params.add(new BasicNameValuePair("z", df.format(gz)));
                params.add(new BasicNameValuePair("type", "Gravity"));

                new CreateNewResult().execute();
            }
        }

        accelorometerTextView.setText("Accelerometer Data :\n" + "x:   " + ax + "\n" + "y:   " + ay + "\n" + "z:   " + az + "\n"
                + "Gravity Data :\n" + "x:   " + gx + "\n" + "y:   " + gy + "\n" + "z:   " + gz + "\n" );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
            Log.d("Debug", json.toString());


            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject file_url) {
        }
    }
}
