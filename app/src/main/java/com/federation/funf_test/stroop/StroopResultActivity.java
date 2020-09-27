package com.federation.funf_test.stroop;

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

public class StroopResultActivity extends Activity {
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
            int color = resultList.get(i).color;
            int name = resultList.get(i).name;
            String colorCon = "";
            String nameCon = "";

            if (color == 1) {
                colorCon = "Red ";
            } if (color == 2) {
                colorCon = "Yellow ";
            } if (color == 3) {
                colorCon = "Green ";
            } else {
                colorCon = "Blue ";
            }

            if (name == 1) {
                nameCon = "Red ";
            } if (name == 2) {
                nameCon = "Yellow ";
            } if (name == 3) {
                nameCon = "Green ";
            } else {
                nameCon = "Blue ";
            }

            double ansTime = resultList.get(i).ansTime;
            DecimalFormat df = new DecimalFormat("0.00");
            String ansTimeFormatted = df.format(ansTime);

            String resultCon = "";
            if (colorCon == nameCon){
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
                Intent toHomeIntent = new Intent(StroopResultActivity.this, MainActivity.class);
                startActivity(toHomeIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
