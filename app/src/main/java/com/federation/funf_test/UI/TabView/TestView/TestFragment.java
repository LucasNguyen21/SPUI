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
import com.federation.funf_test.R;
import com.federation.funf_test.gonogo.GoNogoTestActivity;

public class TestFragment extends Fragment {
    private TestViewModel testViewModel;
    Button gonogoBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_test, container, false);

        gonogoBtn = (Button) root.findViewById(R.id.gonogo_btn);

        gonogoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gonogoIntent = new Intent(getActivity(), GoNogoTestActivity.class);
                startActivity(gonogoIntent);
            }
        });

        return root;
    }

}
