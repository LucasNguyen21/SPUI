package Broadcaster;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.hardware.SensorEventListener;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import static Broadcaster.NotificationHelper.channelName;

public class SensorService extends Service implements SensorEventListener {
    ArrayList<JSONObject> sensorList;
    float xAccel, yAccel, zAccel;
    float xLastAccel, yLastAccel, zLastAccel;

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/sensor/create";
    private static final String TAG_SUCCESS = "success";
    ArrayList params = new ArrayList();

    boolean firstUpdate = true;
    boolean shakeInitiated = false;
    float shakeThreshold = 12.5f;

    Sensor accelerometer;
    SensorManager sm;

    public SensorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sm = (SensorManager) getSystemService((SENSOR_SERVICE));
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorList = new ArrayList();

        String NOTIFICATION_CHANNEL_ID = "com.example.spui";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            chan.enableLights(true);
            chan.enableVibration(true);
            chan.setLightColor(R.color.colorPrimary);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            manager.createNotificationChannel(chan);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("SPUI App")
                .setContentText("Sensor runing...")
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateAccelParameters(event.values[0], event.values[1], event.values[2]);
        JSONObject sensor =  new JSONObject();

        try {
            sensor.put("x", event.values[0]);
            sensor.put("y", event.values[1]);
            sensor.put("z", event.values[2]);
            sensor.put("synced_time", System.currentTimeMillis());
            sensorList.add(sensor);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!shakeInitiated && isAccelerationChanged()) {
            shakeInitiated = true;
        } else if (shakeInitiated && isAccelerationChanged()) {
            executeShakeAction();
        } else if (shakeInitiated && !isAccelerationChanged()) {
            shakeInitiated = false;
        }

        if (sensorList.size() == 1000) {
            Log.d("Sensor List", sensorList.toString());
            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            params.add(new BasicNameValuePair("device_id", androidId));
            params.add(new BasicNameValuePair("sensor_list", sensorList.toString()));

            new CreateNewResult().execute();
            sensorList = new ArrayList();
        }
    }

    private void executeShakeAction() {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isAccelerationChanged() {
        float deltaX = Math.abs(xLastAccel - xAccel);
        float deltaY = Math.abs(yLastAccel - yAccel);
        float deltaZ = Math.abs(zLastAccel - zAccel);

        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
            || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
            || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel, float zNewAccel) {
        if (firstUpdate) {
            xLastAccel = xNewAccel;
            yLastAccel = yNewAccel;
            zLastAccel = zNewAccel;

            firstUpdate = false;
        } else {
            xLastAccel = xAccel;
            yLastAccel = yAccel;
            zLastAccel = zAccel;
        }
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
