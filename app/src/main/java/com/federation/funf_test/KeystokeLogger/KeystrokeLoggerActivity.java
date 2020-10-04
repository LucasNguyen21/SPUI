package com.federation.funf_test.KeystokeLogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;
import com.google.android.material.textfield.TextInputEditText;


public class KeystrokeLoggerActivity extends Activity {
    private TextInputEditText answerInput;
    private double startTime;
    private double endTime;
    private String textOutput = "";
    private int charCount = 0;
    private int lastCount = 0;
    private String textBeforeChanged;
    private String textOnChanged;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keystrokelogger);
        answerInput = (TextInputEditText) findViewById(R.id.answer_input);
        saveButton = (Button) findViewById(R.id.saveButton);

        answerInput.setText("");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE DATA TO SERVER

                //Navigate to main view after finish saving
                Intent toMainActivityIntent = new Intent(KeystrokeLoggerActivity.this, MainActivity.class);
                startActivity(toMainActivityIntent);
            }
        });


        answerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                endTime = System.currentTimeMillis();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                textBeforeChanged = String.valueOf(s);
                Log.d("KeyTest", "\nBefore Text change:\n Start: " + start + "\nAfter: " + after + "\nCount: " + count + "\nString: " + s);
            }

            @Override
            public void onTextChanged(CharSequence string, int start,
                                      int before, int count) {

                Log.d("KeyTest", "Start: " + start + "\nBefore: " + before + "\nCount: " + count + "\nString: " + string);



                if(String.valueOf(string).isEmpty() == false) {
                    textOnChanged = String.valueOf(string);
                    char lastChar = string.charAt(string.length() - 1);

                    charCount++;

                    if (charCount > 1)
                        startTime = System.currentTimeMillis();

                    String newResult = charCount + ". Key: (";

                    if (textOnChanged.length() - textBeforeChanged.length() <= -1) {
                        newResult += "backspace";
                    } else if (lastChar == ' ') {
                        lastCount = 0;
                        newResult += "space";
                    } else if (lastChar == '\n'){
                        newResult += "Enter";
                    } else {
                        lastCount = count;
                        newResult += lastChar;
                    }
                    newResult += ") | Duration: " + ((startTime - endTime) / 1000) + " second\n";
                    textOutput = newResult + textOutput;

//                    if (string.length() > 0)
//                        answerText.setText(textOutput);
                }

                }

        });
    }
}

