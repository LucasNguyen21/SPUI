package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;

import com.federation.funf_test.JSONParser;
import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;
import com.federation.funf_test.SQLite.DatabaseHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GoNogoResultActivity extends Activity {
    TextView resultText;
    Button returnHomeBtn;
    DatabaseHelper databaseHelper;
    ArrayList<Question> resultList;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "https://pos.pentacle.tech/api/gonogo/create";
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
            int status = resultList.get(i).status;
            String statusCon = "";
            if (status == 1) {
                statusCon = "Go   ";
            } else {
                statusCon = "No go";
            }

            double ansTime = resultList.get(i).ansTime;
            DecimalFormat df = new DecimalFormat("0.00");
            String ansTimeFormatted = df.format(ansTime);

            boolean result = resultList.get(i).result;
            String resultCon = "";
            if (result == true) {
                resultCon = "Correct";
            } else {
                resultCon = "Incorrect";
            }

            resultText.append(statusCon + "|" + "   Answer Time: " + ansTimeFormatted + " s   |" + "   Result: " + resultCon);
            resultText.append("\n");

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String timeStamp = String.valueOf(formatter.format(date.getTime()));


            databaseHelper = new DatabaseHelper(GoNogoResultActivity.this);
            boolean isSuccess = databaseHelper.addData(statusCon, String.valueOf(ansTimeFormatted), String.valueOf(resultCon), timeStamp);

            if (isSuccess) {
                Toast.makeText(this, "Save Result Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Save Result Unsuccessfully", Toast.LENGTH_SHORT).show();
            }
        }

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            new CreateNewResult().execute();

            Intent toHomeIntent = new Intent(GoNogoResultActivity.this, MainActivity.class);
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
            pDialog = new ProgressDialog(GoNogoResultActivity.this);
            pDialog.setMessage("Creating Product..");
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
            ArrayList action_list = new ArrayList();
            ArrayList duration_list = new ArrayList();
            ArrayList result_list = new ArrayList();

            for (int i = 0; i < resultList.size(); i++) {
                int status = resultList.get(i).status;

                action_list.add(status);

                double ansTime = resultList.get(i).ansTime;
                DecimalFormat df = new DecimalFormat("0.00");
                String ansTimeFormatted = df.format(ansTime);

                duration_list.add(ansTimeFormatted);

                boolean result = resultList.get(i).result;
                result_list.add(result);
            }

            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            params.add(new BasicNameValuePair("device_id", androidId));
            params.add(new BasicNameValuePair("action_list", action_list.toString()));
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
