package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;
import com.federation.funf_test.SQLite.DatabaseHelper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GoNogoResultActivity extends Activity {
    TextView resultText;
    Button returnHomeBtn;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonogo_result);
        resultText = (TextView) findViewById(R.id.gonogo_result_textView);
        returnHomeBtn = (Button) findViewById(R.id.gonogo_backtohome_btn);


        ArrayList<Question> resultList = getIntent().getExtras().getParcelableArrayList("result");
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
            if (result == true){
                resultCon = "Correct";
            } else {
                resultCon = "Incorrect";
            }

            resultText.append(statusCon + "|"  +"   Answer Time: " + ansTimeFormatted + " s   |" + "   Result: " + resultCon);
            resultText.append("\n");

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String timeStamp = String.valueOf(formatter.format(date.getTime()));



            databaseHelper = new DatabaseHelper(GoNogoResultActivity.this);
            boolean isSuccess = databaseHelper.addData(statusCon, String.valueOf(ansTimeFormatted), String.valueOf(resultCon), timeStamp);

            if(isSuccess){
                Toast.makeText(this, "Save Result Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Save Result Unsuccessfully", Toast.LENGTH_SHORT).show();
            }
        }

        returnHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHomeIntent = new Intent(GoNogoResultActivity.this, MainActivity.class);
                startActivity(toHomeIntent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        return;
    }
}
