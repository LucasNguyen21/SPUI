package com.federation.funf_test.KeystokeLogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class KeystrokeLoggerActivity extends Activity {
    private TextInputEditText answerInput;
    private TextView questionText;
    private double startTime;
    private double endTime;
    private String textOutput = "";
    private int charCount = 0;
    private int lastCount = 0;
    private String textBeforeChanged;
    private String textOnChanged;
    private Button saveButton;

    ArrayList params = new ArrayList();
    ArrayList key_list = new ArrayList();
    ArrayList duration_list = new ArrayList();

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url_create = "https://pos.pentacle.tech/api/keystroke/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keystrokelogger);
        answerInput = (TextInputEditText) findViewById(R.id.answer_input);
        questionText = (TextView) findViewById(R.id.question_text);
        answerInput.setMaxHeight(answerInput.getHeight());

        saveButton = (Button) findViewById(R.id.saveButton);

        answerInput.setText("");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE DATA TO SERVER
                String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                JSONObject newQuestion = new JSONObject();

                params.add(new BasicNameValuePair("device_id", androidId));
                params.add(new BasicNameValuePair("question", questionText.getText().toString()));
                params.add(new BasicNameValuePair("key_list", key_list.toString()));
                params.add(new BasicNameValuePair("duration_list", duration_list.toString()));
                params.add(new BasicNameValuePair("answer", answerInput.getText().toString()));
                new CreateNewResult().execute();

                //Navigate to main view after finish saving

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
                        key_list.add("backspace");
                    } else if (lastChar == ' ') {
                        lastCount = 0;
                        newResult += "space";
                        key_list.add("space");
                    } else if (lastChar == '\n'){
                        newResult += "Enter";
                        key_list.add("Enter");
                    } else {
                        lastCount = count;
                        newResult += lastChar;
                        key_list.add(lastChar);
                    }
                    newResult += ") | Duration: " + ((startTime - endTime) / 1000) + " second\n";
                    duration_list.add((startTime - endTime) / 1000);
                    textOutput = newResult + textOutput;

                }

                }

        });
    }

    class CreateNewResult extends AsyncTask<String, String, JSONObject> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(KeystrokeLoggerActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            builderDialog.setTitle("Uploading...");
            builderDialog.setMessage("Uploading data to server");
            builderDialog.setCancelable(true);
            builderDialog.show();
        }

        /**
         * Creating product
         */
        protected JSONObject doInBackground(String... args) {
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject file_url) {
            // dismiss the dialog once done
            AlertDialog alertDialog = builderDialog.create();
            alertDialog.dismiss();
            Intent toMainActivityIntent = new Intent(KeystrokeLoggerActivity.this, MainActivity.class);
            startActivity(toMainActivityIntent);
        }
    }
}

