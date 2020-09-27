package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.federation.funf_test.AccelerometerActivity;
import com.federation.funf_test.R;

public class GoNogoTestActivity extends Activity {

    Button startBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonogo_main);

        startBtn = (Button) findViewById(R.id.startgonogo_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTestIntent = new Intent(GoNogoTestActivity.this, GoNogoInTestActivity.class);
                startActivity(startTestIntent);
            }
        });
    }
}
