package com.federation.funf_test;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class AccelerometerActivity extends Activity implements SensorEventListener {
    TextView accelorometerTextView;
    private SensorManager sensorManager;
    double ax, ay, az;
    double gx, gy, gz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_log);
        accelorometerTextView = (TextView) findViewById(R.id.accelerometerTextView);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            ax=event.values[0];
            ay=event.values[1];
            az=event.values[2];
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gx = event.values[0];
            gy = event.values[1];
            gz = event.values[2];
        }
        accelorometerTextView.setText("Accelerometer Data :\n" + "x:   " + ax + "\n" + "y:   " + ay + "\n" + "z:   " + az + "\n"
                + "Gravity Data :\n" + "x:   " + gx + "\n" + "y:   " + gy + "\n" + "z:   " + gz + "\n" );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
