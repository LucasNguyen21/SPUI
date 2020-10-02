package com.federation.funf_test.stroop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;
import com.federation.funf_test.gonogo.GoNogoResultActivity;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class StroopResultActivity extends Activity {
    TextView resultText;
    Button returnHomeBtn;

    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    ArrayList<Question> resultList;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/stroop/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonogo_result);
        resultText = (TextView) findViewById(R.id.gonogo_result_textView);
        returnHomeBtn = (Button) findViewById(R.id.gonogo_backtohome_btn);

        resultList = getIntent().getExtras().getParcelableArrayList("result");
        resultText.setText("");

        for (int i = 0; i < resultList.size(); i++) {
            int color = resultList.get(i).color;
            int name = resultList.get(i).name;
            String colorCon = "";
            String nameCon = "";

            if (color == 0) {
                colorCon = "Red ";
            } else if (color == 1) {
                colorCon = "Yellow ";
            } else if (color == 2) {
                colorCon = "Green ";
            } else if (color == 3) {
                colorCon = "Blue ";
            }

            if (name == 0) {
                nameCon = "Red ";
            } else if (name == 1) {
                nameCon = "Yellow ";
            } else if (name == 2) {
                nameCon = "Green ";
            } else if (name == 3) {
                nameCon = "Blue ";
            }

            double ansTime = resultList.get(i).ansTime;
            DecimalFormat df = new DecimalFormat("0.00");
            String ansTimeFormatted = df.format(ansTime);

            boolean result = resultList.get(i).result;
            String resultCon = "";
            if (result == true){
                resultCon = "Correct";
            } else {
                resultCon = "Incorrect";
            }

            resultText.append(colorCon + "| " + nameCon + "|"  +"   Answer Time: " + ansTimeFormatted + " s   |" + "   Result: " + resultCon);
            resultText.append("\n");
        }

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new CreateNewResult().execute();
            Intent toHomeIntent = new Intent(StroopResultActivity.this, MainActivity.class);
            startActivity(toHomeIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    class CreateNewResult extends AsyncTask<String, String, JSONObject> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StroopResultActivity.this);
            pDialog.setMessage("Uploading Result..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected JSONObject doInBackground(String... args) {
            // Building Parameters
            ArrayList params = new ArrayList();
            ArrayList color_list = new ArrayList();
            ArrayList name_list = new ArrayList();
            ArrayList duration_list = new ArrayList();
            ArrayList result_list = new ArrayList();

            for (int i = 0; i < resultList.size(); i++) {
                int color = resultList.get(i).color;
                int name = resultList.get(i).name;

                if (color == 0) {
                    color_list.add("Red");
                } else if (color == 1) {
                    color_list.add("Yellow");
                } else if (color == 2) {
                    color_list.add("Green");
                } else if (color == 3) {
                    color_list.add("Blue");
                }

                if (name == 0) {
                    name_list.add("Red");
                } else if (color == 1) {
                    name_list.add("Yellow");
                } else if (color == 2) {
                    name_list.add("Green");
                } else if (color == 3) {
                    name_list.add("Blue");
                }

                double ansTime = resultList.get(i).ansTime;
                DecimalFormat df = new DecimalFormat("0.00");
                String ansTimeFormatted = df.format(ansTime);

                duration_list.add(ansTimeFormatted);

                boolean result = resultList.get(i).result;
                if (result == true){
                    result_list.add("Correct");
                } else {
                    result_list.add("Incorrect");
                }
            }

            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            params.add(new BasicNameValuePair("device_id", androidId));
            params.add(new BasicNameValuePair("color_list", color_list.toString()));
            params.add(new BasicNameValuePair("name_list", name_list.toString()));
            params.add(new BasicNameValuePair("duration_list", duration_list.toString()));
            params.add(new BasicNameValuePair("result_list", result_list.toString()));

            // getting JSON Object
            // Note that create product url accepts POST methodN
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);
            // check log cat fro response
            Log.d("Debug", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // successfully created product
                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}
