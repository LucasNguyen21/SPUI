package com.federation.funf_test.Log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.federation.funf_test.R;

public class LogActivity extends Activity {
    Button goNoGoBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_main);

        goNoGoBtn = (Button) findViewById(R.id.gonogoLogBtn);

        goNoGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGonogoLog = new Intent(LogActivity.this, GoNogoLogActivity.class);
                startActivity(toGonogoLog);
            }
        });
    }
}
