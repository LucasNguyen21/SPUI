package com.federation.funf_test.UI.TabView.TestView;

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
import com.federation.funf_test.KeystokeLogger.KeystrokeLoggerActivity;
import com.federation.funf_test.Log.LogActivity;
import com.federation.funf_test.R;
import com.federation.funf_test.gonogo.GoNogoTestActivity;
import com.federation.funf_test.stroop.StroopTestActivity;

public class TestFragment extends Fragment {
    private TestViewModel testViewModel;
    Button gonogoBtn;
    Button stroopBtn;
    Button keystrokeLoggerBtn;
    Button logBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_test, container, false);

        gonogoBtn = (Button) root.findViewById(R.id.gonogo_btn);
        stroopBtn = (Button) root.findViewById(R.id.stroop_btn);
        keystrokeLoggerBtn = (Button) root.findViewById(R.id.keystrokeLog_btn);
        logBtn = (Button) root.findViewById(R.id.logMainBtn);


        gonogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gonogoIntent = new Intent(getActivity(), GoNogoTestActivity.class);
                startActivity(gonogoIntent);
            }
        });

        stroopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stroopIntent = new Intent(getActivity(), StroopTestActivity.class);
                startActivity(stroopIntent);
            }
        });

        keystrokeLoggerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keystrokeLoggerIntent = new Intent(getActivity(), KeystrokeLoggerActivity.class);
                startActivity(keystrokeLoggerIntent);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logIntent = new Intent(getActivity(), LogActivity.class);
                startActivity(logIntent);
            }
        });

        return root;
    }

}
