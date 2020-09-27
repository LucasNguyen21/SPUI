package com.federation.funf_test.stroop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.federation.funf_test.R;

public class StroopTestActivity extends Activity {

    Button startBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroop_main);

        startBtn = (Button) findViewById(R.id.startstroop_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTestIntent = new Intent(StroopTestActivity.this, StroopInTestActivity.class);
                startActivity(startTestIntent);
            }
        });
    }
}
