package com.federation.funf_test.ListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.federation.funf_test.R;

import java.util.ArrayList;


public class QuestionnaireListAdapter extends ArrayAdapter<Question> {
    Activity context;
    ArrayList<Question> question;

    public QuestionnaireListAdapter(Activity context, ArrayList<Question> questions) {
        super(context, R.layout.questionnaire_list_items, questions);

        this.context = context;
        this.question = questions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.questionnaire_list_items, null, true);

        TextView questionTextView = (TextView) rowView.findViewById(R.id.questionTextView);
        RadioButton radioButton0 = (RadioButton) rowView.findViewById(R.id.answer0radioButton);
        RadioButton radioButton1 = (RadioButton) rowView.findViewById(R.id.answer1radioButton);
        RadioButton radioButton2 = (RadioButton) rowView.findViewById(R.id.answer2radioButton);
        RadioButton radioButton3 = (RadioButton) rowView.findViewById(R.id.answer3radioButton);
        RadioButton radioButton4 = (RadioButton) rowView.findViewById(R.id.answer4radioButton);
        RadioButton radioButton5 = (RadioButton) rowView.findViewById(R.id.answer5radioButton);
        RadioButton radioButton6 = (RadioButton) rowView.findViewById(R.id.answer6radioButton);

        questionTextView.setText(question.get(position).question);
        return rowView;
    }
}

