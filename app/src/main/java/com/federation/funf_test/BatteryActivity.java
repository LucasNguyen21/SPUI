package com.federation.funf_test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class BatteryActivity extends Activity {

    TextView batterySttText;
    BroadcastReceiver batteryBroadcaster;
    int level;
    int scale;
    int status;
    boolean isCharging;
    float batteryPct;
    int chargePlug;
    boolean usbCharge;
    boolean acCharge;
    int batteryTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_stat);
        batterySttText = (TextView) findViewById(R.id.batterySttText);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(this.mBatInfoReceiver, ifilter);

    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryPct = level * 100 / (float)scale;

            chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            batteryTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            float batteryTemperature = batteryTemp /10;

            float batteryVol = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) * 0.001);

            Date currentTime = Calendar.getInstance().getTime();

            int batTech = intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, -1);

            batterySttText.setText("Current Percentage: " + batteryPct + " %"  + "\n" + "Is Charging: " + isCharging + "\n" + "USB Charge?: " + usbCharge + "\n" + "AC Charge?: " + acCharge + "\n" + "Battery Temperature: " + batteryTemperature + " °C" + "\n" + "Current Voltage: " + batteryVol + " Vol" + "\n" + "Record Time: " + currentTime + "\n"
                    + "Technology: " + batTech + "\n");

            Log.d("huhuhuhu", "Current Percentage: " + batteryPct + " %"  + "\n" + "Is Charging: " + isCharging + "\n" + "USB Charge?: " + usbCharge + "\n" + "AC Charge?: " + acCharge + "\n" + "Battery Temperature: " + batteryTemperature + " °C" + "\n" + "Current Voltage: " + batteryVol + " Vol" + "\n" + "Record Time: " + currentTime + "\n"
                    + "Technology: " + batTech + "\n");

        }
    };

    //Where to unregister Receiver


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
