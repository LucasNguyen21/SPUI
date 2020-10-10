package Broadcaster;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.hardware.SensorEventListener;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class SensorService extends Service implements SensorEventListener {
    ArrayList<JSONObject> sensorList;
    float xAccel, yAccel, zAccel;
    float xLastAccel, yLastAccel, zLastAccel;

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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateAccelParameters(event.values[0], event.values[1], event.values[2]);
        Log.d("Sensor", "x: " + event.values[0] + "y: " + event.values[1] + "z: " + event.values[2]);
        JSONObject sensor =  new JSONObject();

        try {
            sensor.put("x", event.values[0]);
            sensor.put("y", event.values[1]);
            sensor.put("z", event.values[2]);
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
}
