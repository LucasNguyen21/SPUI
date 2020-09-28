package com.federation.funf_test.KeystokeLogger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class KeystrokeLoggerActivity extends Activity {
    private TextInputEditText answerInput;
    private TextView answerText;
    private double startTime;
    private double endTime;
    private String textOutput = "";
    private int charCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keystrokelogger);
        answerInput = (TextInputEditText) findViewById(R.id.answer_input);
        answerText = (TextView) findViewById(R.id.answer_text);

        answerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                endTime = System.currentTimeMillis();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence string, int start,
                                      int before, int count) {
                charCount++;

                if (charCount > 1)
                    startTime = System.currentTimeMillis();

                String newResult = charCount + ". Key: (";

                char lastChar = string.charAt(string.length() - 1);

                if (lastChar == ' ') {
                    newResult += "space";
                } else {
                    newResult += string.charAt(string.length() - 1);
                }
                newResult += ") | Duration: " + ((startTime - endTime)/1000) + " second\n";
                textOutput = newResult + textOutput;

                if (string.length() > 0)
                    answerText.setText(textOutput);
            }
        });
    }
}

