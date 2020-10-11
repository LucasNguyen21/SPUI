package com.federation.funf_test;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.federation.funf_test.CallActivity.CallIntentService;
import com.federation.funf_test.SMSActivity.SmsIntentService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

import Broadcaster.AppUsageBroadcaster;
import Broadcaster.LocationBroadcaster;
import Broadcaster.NotificationBroadcaster;
import Broadcaster.BatteryBroadcaster;
import Broadcaster.NotificationHelper;
import Broadcaster.OnBootBroadcaster;
import Broadcaster.SensorService;

public class MainActivity extends AppCompatActivity {
    NotificationHelper notificationHelper;
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private PackageManager mPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintabview);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        notificationHelper = new NotificationHelper(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.BROADCAST_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1000);
        } else {
            setQuestionNotification();

            setBatteryNotification();

            setLocationBackground();

            setCallBroadcast();

            setSmsBroadcast();

            getAppUsageBackground();
        }


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.navigation_test, R.id.navigation_datacollection)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        Intent sensorIntent = new Intent(this, SensorService.class);
//        startService(sensorIntent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000 && grantResults.length != 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                //Set notification for keystroke tracking
                setQuestionNotification();

                //Set background running for battery log
                setBatteryNotification();

                setLocationBackground();

                setCallBroadcast();

                setSmsBroadcast();

                getAppUsageBackground();


            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar cal = Calendar.getInstance();

        final List<UsageStats> queryUsageStats=mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, cal.getTimeInMillis(), System.currentTimeMillis());

        boolean isEmpty = queryUsageStats.isEmpty();
        if (isEmpty) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setQuestionNotification(){
        int hourOfDay = 20;
        int minute = 00;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcaster.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 12 * 60 * 60 * 1000, pendingIntent);
    }

    BatteryBroadcaster batteryBroadcaster = new BatteryBroadcaster();
    private void setBatteryNotification(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(batteryBroadcaster, intentFilter);
    }

    CallIntentService callIntentService = new CallIntentService();
    private void setCallBroadcast(){
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(callIntentService, intentFilter);
    }

    SmsIntentService smsIntentService = new SmsIntentService();
    private void setSmsBroadcast(){
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsIntentService, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setLocationBackground(){
        Calendar calendar = Calendar.getInstance();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, LocationBroadcaster.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 1000, pendingIntent);
    }

    private void getAppUsageBackground(){
        Calendar calendar = Calendar.getInstance();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AppUsageBroadcaster.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 1000, pendingIntent);
    }
}
