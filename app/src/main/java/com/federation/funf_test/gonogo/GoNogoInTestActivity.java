package com.federation.funf_test.gonogo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import com.federation.funf_test.R;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

//1: Go
//0: No go

//result: true/false

public class GoNogoInTestActivity extends Activity {
    Button intestBtn;

    //Test variables
    int maxQuestion = 15;
    int currentCount = 0;
    double startTime;
    double finishTime;
    int randomStatus;
    ArrayList<Question> questionList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gonogo_intest);
        intestBtn = (Button) findViewById(R.id.gonogoInTest_btn);

        currentCount = getCount();
        if(currentCount < 0){
            currentCount = 0;
        }
        Log.d("RESULTTT", "onCreate: " + currentCount);
        if(currentCount < maxQuestion) {
            randomStatus = ThreadLocalRandom.current().nextInt(0,2);
            setQuestionStatus(intestBtn, randomStatus);

            startTime = System.currentTimeMillis();



            final CountDownTimer timer = new CountDownTimer(2000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if(randomStatus == 1) {
                        saveQuestion(currentCount, new Question(randomStatus, 2, false));
                    } else {
                        saveQuestion(currentCount, new Question(randomStatus, 2, true));
                    }

                    currentCount++;
                    saveCount(currentCount);

                    Intent appUsageIntent = new Intent(GoNogoInTestActivity.this, GoNogoInTestActivity.class);
                    startActivity(appUsageIntent);
                }
            };

            timer.start();

            intestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTime = System.currentTimeMillis();
                    if (randomStatus == 1) {
                        saveQuestion(currentCount, new Question(randomStatus, (finishTime-startTime)/1000, true));
                    } else {
                        saveQuestion(currentCount, new Question(randomStatus, (finishTime-startTime)/1000, false));
                    }

                    startTime = System.currentTimeMillis();


                    timer.cancel();


                    Intent nextQuestionIntent = new Intent(GoNogoInTestActivity.this, GoNogoInTestActivity.class);
                    startActivity(nextQuestionIntent);

                    currentCount++;
                    saveCount(currentCount);

                }
            });
        } else {
            Log.d("STOP TEST", "STOP TEST");
            for (int i = 0; i < maxQuestion; i++){
                Question question = getQuestion(i);
                Log.d("RESULTTTT", "onCreate: " + question.toString());
                questionList.add(question);
            }
            clearPref();
            Intent toResultIntent = new Intent(GoNogoInTestActivity.this, GoNogoResultActivity.class);
            toResultIntent.putExtra("result", questionList);
            startActivity(toResultIntent);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void setQuestionStatus(Button button, int randomStatus){
        Log.d("randomStatus", "onClick: " + randomStatus);
        if (randomStatus == 1) {
            button.setText("GO");
            button.setBackgroundColor(Color.parseColor("#8BC34A"));
        } else {
            button.setText("No Go");
            button.setBackgroundColor(Color.parseColor("#FA5549"));
        }

    }

    private void saveCount(int count){
        SharedPreferences.Editor editor = getSharedPreferences("count", MODE_PRIVATE).edit();
        editor.putInt("count", count);
        editor.apply();
    }

    private int getCount(){
        SharedPreferences prefs = getSharedPreferences("count", MODE_PRIVATE);
        int count = prefs.getInt("count", -1);

        return count;
    }

    private void saveQuestion(int count, Question question){
        SharedPreferences.Editor editor = getSharedPreferences(setQuestionCountName(count), MODE_PRIVATE).edit();
        editor.putInt("status", question.status);
        editor.putFloat("ansTime", (float) question.ansTime);
        editor.putBoolean("result", question.result);
        editor.apply();
    }

    private Question getQuestion(int index){
        SharedPreferences prefs = getSharedPreferences(setQuestionCountName(index), MODE_PRIVATE);
        int status = prefs.getInt("status", -1);
        double ansTime = prefs.getFloat("ansTime", -1);
        boolean result = prefs.getBoolean("result", false);

        return new Question(status, ansTime, result);
    }

    private String setQuestionCountName(int count) {
        return "QuestionCount" + count;
    }

    private void clearPref(){
        for(int i = 0; i < maxQuestion; i++){
            SharedPreferences prefs = getSharedPreferences(setQuestionCountName(i), MODE_PRIVATE);
            prefs.edit().clear().commit();
        }
        SharedPreferences.Editor editor = getSharedPreferences("count", MODE_PRIVATE).edit();
        editor.clear().commit();
    }
}

class Question implements Parcelable {
    int status; //0: No Go, 1: Go
    double ansTime; //How long user takes to answer the question
    boolean result; // true, false

    public Question(int status, double ansTime, boolean result) {
        this.status = status;
        this.ansTime = ansTime;
        this.result = result;
    }

    protected Question(Parcel in) {
        status = in.readInt();
        ansTime = in.readDouble();
        result = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public double getAnsTime() {
        return ansTime;
    }

    public String getResult() {
        if(result = true) {
            return "Correct";
        } else {
            return "Incorrect";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAnsTime(double ansTime) {
        this.ansTime = ansTime;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "status=" + status +
                ", ansTime=" + ansTime +
                ", result=" + result +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeDouble(ansTime);
        dest.writeByte((byte) (result ? 1 : 0));
    }
}
