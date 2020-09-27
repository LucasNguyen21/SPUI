package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.federation.funf_test.R;

public class GoNogoInTestActivity extends Activity {
    Button intestBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gonogo_intest);

        intestBtn = (Button) findViewById(R.id.gonogoInTest_btn);


    }
}
