package Broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryBroadcaster extends BroadcastReceiver {
    public static final String TAG = "BATTERY LOG";
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

        //CONDITION TO PUT TO SERVER
        if(batteryPct < 10) {
            //PUSH DATE TIME + batteryPct% + isCharging status TO SERVER
        }

        if(batteryPct > 90) {
            //PUSH DATE TIME + batteryPct% + isCharging status TO SERVER
        }

        if(isCharging == true) {
            Log.d(TAG, "Charging");
            if(usbCharge == true){
                //PUSH DATE TIME +batteryPct % + charging status TO SERVER
                Log.d(TAG, "USB Charge");
            }

            if(acCharge == true){
                //PUSH DATE TIME + batteryPct% + charging status TO SERVER
                Log.d(TAG, "AC Charge");
            }
        } else {
            Log.d(TAG, "Not Charging");
        }

        //CONDITION TO PUT TO SERVER
    }
}
