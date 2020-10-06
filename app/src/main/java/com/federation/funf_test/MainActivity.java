package com.federation.funf_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import Broadcaster.NotificationBroadcaster;
import Broadcaster.BatteryBroadcaster;
import Broadcaster.NotificationHelper;
import Broadcaster.OnBootBroadcaster;

public class MainActivity extends AppCompatActivity {
    NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintabview);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        notificationHelper = new NotificationHelper(this);

        //Set notification for keystroke tracking
        setQuestionNotification();

        //Set background running for battery log
        setBatteryNotification();

        setOnBoot();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                 R.id.navigation_test, R.id.navigation_datacollection)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    private void setQuestionNotification(){
        int hourOfDay = 22;
        int minute = 50;
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

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
    }

    BatteryBroadcaster batteryBroadcaster = new BatteryBroadcaster();
    private void setBatteryNotification(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryBroadcaster, intentFilter);
    }

    OnBootBroadcaster onBootBroadcaster = new OnBootBroadcaster();
    private void setOnBoot(){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_LOCKED_BOOT_COMPLETED);
        registerReceiver(onBootBroadcaster, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryBroadcaster);
    }
}
