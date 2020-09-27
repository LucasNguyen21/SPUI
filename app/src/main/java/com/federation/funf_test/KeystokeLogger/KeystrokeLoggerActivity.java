package com.federation.funf_test.KeystokeLogger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.federation.funf_test.R;


public class KeystrokeLoggerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keystrokelogger);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d("key press", "onKeyUp: " + keyCode);
        return super.onKeyUp(keyCode, event);
    }
}
