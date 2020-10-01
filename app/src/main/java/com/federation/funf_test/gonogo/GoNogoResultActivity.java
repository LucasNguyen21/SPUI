package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GoNogoResultActivity extends Activity {
    TextView resultText;
    Button returnHomeBtn;
    DatabaseHelper databaseHelper;
    ArrayList<Question> resultList;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_create = "http://pos.pentacle.tech/api/gonogo/create";
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

    class CreateNewResult extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("device_id", "1"));
            params.add(new BasicNameValuePair("action_list", "2"));
            params.add(new BasicNameValuePair("duration_list", "3"));
            params.add(new BasicNameValuePair("result_list", "4"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);
            // check log cat fro response


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
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
}
