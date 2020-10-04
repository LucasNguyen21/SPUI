package com.federation.funf_test.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.federation.funf_test.R;

import java.util.ArrayList;


public class QuestionnaireListAdapter extends ArrayAdapter<Question> {
    Activity context;
    ArrayList<Question> question;
    ArrayList<Integer> selectedAnswer;

    public QuestionnaireListAdapter(Activity context, ArrayList<Question> questions) {
        super(context, R.layout.questionnaire_list_items, questions);

        this.context = context;
        this.question = questions;
        selectedAnswer = new ArrayList<>();
        for(int i = 0; i < questions.size(); i++){
            selectedAnswer.add(-1);

        }
    }

    @Override
    public int getCount() {
        return question.size();
    }

    @Nullable
    @Override
    public Question getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.questionnaire_list_items, null, true);

        TextView questionTextView = (TextView) rowView.findViewById(R.id.questionTextView);
        questionTextView.setText(question.get(position).question);


        RadioButton radioButton0 = (RadioButton) rowView.findViewById(R.id.answer0radioButton);
        RadioButton radioButton1 = (RadioButton) rowView.findViewById(R.id.answer1radioButton);
        RadioButton radioButton2 = (RadioButton) rowView.findViewById(R.id.answer2radioButton);
        RadioButton radioButton3 = (RadioButton) rowView.findViewById(R.id.answer3radioButton);
        RadioButton radioButton4 = (RadioButton) rowView.findViewById(R.id.answer4radioButton);
        RadioButton radioButton5 = (RadioButton) rowView.findViewById(R.id.answer5radioButton);
        RadioButton radioButton6 = (RadioButton) rowView.findViewById(R.id.answer6radioButton);

        if(question.get(position).answerId == 1){
            radioButton0.setChecked(true);
        } else if(question.get(position).answerId == 2){
            radioButton1.setChecked(true);
        } else if(question.get(position).answerId == 3){
            radioButton2.setChecked(true);
        } else if(question.get(position).answerId == 4){
            radioButton3.setChecked(true);
        } else if(question.get(position).answerId == 5){
            radioButton4.setChecked(true);
        } else if(question.get(position).answerId == 6){
            radioButton5.setChecked(true);
        } else if(question.get(position).answerId == 7){
            radioButton6.setChecked(true);
        } else {

        }

        radioButton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(1);
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(2);
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(3);
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(4);
            }
        });

        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(5);
            }
        });

        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(6);
            }
        });

        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.get(position).setAnswerId(7);
            }
        });

        return rowView;
    }
}

