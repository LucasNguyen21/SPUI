package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.MainActivity;
import com.federation.funf_test.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GoNogoResultActivity extends Activity {
    TextView resultText;
    Button returnHomeBtn;

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
