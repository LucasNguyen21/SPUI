package com.federation.funf_test.stroop;

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

public class StroopInTestActivity extends Activity {
    Button intestBtn;

    Button redBtn;
    Button yellowBtn;
    Button greenBtn;
    Button blueBtn;

    //Test variables
    int maxQuestion = 30;
    int currentCount = 0;
    double startTime;
    double finishTime;
    int randomColor;
    int randomName;
    ArrayList<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroop_intest);
        intestBtn = (Button) findViewById(R.id.stroop_test_btn);

        redBtn = (Button) findViewById(R.id.stroop_red_btn);
        yellowBtn = (Button) findViewById(R.id.stroop_yellow_btn);
        greenBtn = (Button) findViewById(R.id.stroop_green_btn);
        blueBtn = (Button) findViewById(R.id.stroop_blue_btn);


        currentCount = getCount();
        if(currentCount < 0){
            currentCount = 0;
        }

        if(currentCount < maxQuestion) {
            randomColor = ThreadLocalRandom.current().nextInt(0,4);
            randomName = ThreadLocalRandom.current().nextInt(0,4);
            setQuestionStatus(intestBtn, randomColor, randomName);

            startTime = System.currentTimeMillis();

            final CountDownTimer timer = new CountDownTimer(2000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    saveQuestion(currentCount, new Question(randomColor, randomName, 2, false));

                    currentCount++;
                    saveCount(currentCount);

                    Intent appUsageIntent = new Intent(StroopInTestActivity.this, StroopInTestActivity.class);
                    startActivity(appUsageIntent);
                }
            };

            timer.start();

            redBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTime = System.currentTimeMillis();

                    if (randomColor == 0) {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, true));
                    } else {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, false));
                    }

                    startTime = System.currentTimeMillis();

                    timer.cancel();

                    Intent nextQuestionIntent = new Intent(StroopInTestActivity.this, StroopInTestActivity.class);
                    startActivity(nextQuestionIntent);

                    currentCount++;
                    saveCount(currentCount);
                }
            });

            yellowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTime = System.currentTimeMillis();

                    if (randomColor == 1) {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, true));
                    } else {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, false));
                    }

                    startTime = System.currentTimeMillis();

                    timer.cancel();

                    Intent nextQuestionIntent = new Intent(StroopInTestActivity.this, StroopInTestActivity.class);
                    startActivity(nextQuestionIntent);

                    currentCount++;
                    saveCount(currentCount);
                }
            });

            greenBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTime = System.currentTimeMillis();

                    if (randomColor == 2) {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, true));
                    } else {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, false));
                    }

                    startTime = System.currentTimeMillis();

                    timer.cancel();

                    Intent nextQuestionIntent = new Intent(StroopInTestActivity.this, StroopInTestActivity.class);
                    startActivity(nextQuestionIntent);

                    currentCount++;
                    saveCount(currentCount);
                }
            });

            blueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishTime = System.currentTimeMillis();

                    if (randomColor == 3) {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, true));
                    } else {
                        saveQuestion(currentCount, new Question(randomColor, randomName, (finishTime-startTime)/1000, false));
                    }

                    startTime = System.currentTimeMillis();

                    timer.cancel();

                    Intent nextQuestionIntent = new Intent(StroopInTestActivity.this, StroopInTestActivity.class);
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
            Intent toResultIntent = new Intent(StroopInTestActivity.this, StroopResultActivity.class);
            toResultIntent.putExtra("result", questionList);
            startActivity(toResultIntent);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void setQuestionStatus(Button button, int randomColor, int randomName){
        Log.d("randomStatus", "onClick: " + randomColor);
        if (randomColor == 0) { //RED
            button.setTextColor(Color.parseColor("#F44336"));
        } else if (randomColor == 1) { //YELLOW
            button.setTextColor(Color.parseColor("#FFC107"));
        } else if (randomColor == 2) { //GREEN
            button.setTextColor(Color.parseColor("#4CAF50"));
        } else if (randomColor == 3) { //BLUE
            button.setTextColor(Color.parseColor("#03A9F4"));
        }

        if (randomName == 0) {
            button.setText("RED");
        } else if (randomName == 1) {
            button.setText("YELLOW");
        } else if (randomName == 2) {
            button.setText("GREEN");
        } else if (randomName == 3) {
            button.setText("BLUE");
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
        editor.putInt("name", question.name);
        editor.putInt("color", question.color);
        editor.putFloat("ansTime", (float) question.ansTime);
        editor.putBoolean("result", question.result);
        editor.apply();
    }

    private Question getQuestion(int index){
        SharedPreferences prefs = getSharedPreferences(setQuestionCountName(index), MODE_PRIVATE);
        int color = prefs.getInt("color", -1);
        int name = prefs.getInt("name", -1);
        double ansTime = prefs.getFloat("ansTime", -1);
        boolean result = prefs.getBoolean("result", false);

        return new Question(color, name, ansTime, result);
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
    int color;
    int name;
    double ansTime;
    boolean result;

    public Question(int color, int name, double ansTime, boolean result) {
        this.color = color;
        this.name = name;
        this.ansTime = ansTime;
        this.result = result;
    }

    protected Question(Parcel in) {
        color = in.readInt();
        name = in.readInt();
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

    public int getName() {
        return name;
    }

    public int getColor() {
        return color;
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

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(int name) {
        this.name = name;
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
                "color=" + color +
                ", text=" + name +
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
        dest.writeInt(color);
        dest.writeInt(name);
        dest.writeDouble(ansTime);
        dest.writeByte((byte) (result ? 1 : 0));
    }
}
