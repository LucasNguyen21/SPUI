package com.federation.funf_test.UI.TabView.DataCollectionView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.federation.funf_test.AccelerometerActivity;
import com.federation.funf_test.AppUsageActivity;
import com.federation.funf_test.BatteryActivity;
import com.federation.funf_test.CallLogActivity;
import com.federation.funf_test.LocationActivity;
import com.federation.funf_test.R;
import com.federation.funf_test.SMSActivity.SmsLogActivity;

public class DataCollectionFragment extends Fragment {
    private DataCollectionViewModel dataCollectionViewModel;
    private Button appUsageBtn;
    private Button batteryBtn;
    private Button callLogBtn;
    private Button smsLogBtn;
    private Button accelerometerBtn;
    private Button gpsBtn;
    private Button browserBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dataCollectionViewModel = ViewModelProviders.of(this).get(DataCollectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_datacollection, container, false);


        appUsageBtn = (Button) root.findViewById(R.id.app_usage_btn);

        appUsageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(getActivity(), AppUsageActivity.class);
                startActivity(appUsageIntent);
            }
        });

        batteryBtn = (Button) root.findViewById(R.id.batteryBtn);

        batteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(getActivity(), BatteryActivity.class);
                startActivity(appUsageIntent);
            }
        });

        smsLogBtn = (Button) root.findViewById(R.id.sms_log_btn);

        smsLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(getActivity(), SmsLogActivity.class);
                startActivity(appUsageIntent);
            }
        });

        callLogBtn = (Button) root.findViewById(R.id.call_log_btn);

        callLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(getActivity(), CallLogActivity.class);
                startActivity(appUsageIntent);
            }
        });

        accelerometerBtn = (Button) root.findViewById(R.id.accelerometer_btn);

        accelerometerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appUsageIntent = new Intent(getActivity(), AccelerometerActivity.class);
                startActivity(appUsageIntent);
            }
        });

        gpsBtn = (Button) root.findViewById(R.id.gpsBtn);

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationIntent = new Intent(getActivity(), LocationActivity.class);
                startActivity(locationIntent);
            }
        });



//        final TextView textView = root.findViewById(R.id.text_datacollection);
////        dataCollectionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(s);
////            }
////        });
        return root;
    }
}
