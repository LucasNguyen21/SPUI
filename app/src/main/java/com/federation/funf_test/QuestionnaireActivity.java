package com.federation.funf_test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.federation.funf_test.ListAdapter.Question;
import com.federation.funf_test.ListAdapter.QuestionnaireListAdapter;

import java.util.ArrayList;

public class QuestionnaireActivity extends Activity {

    ArrayList<Question> questions = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_list);

        listView = (ListView) findViewById(R.id.questionnaireListView);

        setQuestions();

        QuestionnaireListAdapter questionnaireListAdapter = new QuestionnaireListAdapter(this, questions);
        listView.setAdapter(questionnaireListAdapter);

        
    }

    public void setQuestions() {
        questions.add(new Question("SUQ-G1. How often do you have your cellphone on your person?", -1));
        questions.add(new Question("SUQ-G2. How frequently do you send and receive text messages or emails?", -1));
        questions.add(new Question("SUQ-G3. To what extent do you have push notifications enabled on your phone?", -1));
        questions.add(new Question("SUQ-G4. How often do you find yourself checking your phone for new events such as text mes-\n" +
                "sages or emails?", -1));
        questions.add(new Question("SUQ-G5. How often do you use the phone for reading the news or browsing the web?", -1));
        questions.add(new Question("SUQ-G6. How often do you use sound notifications on your phone?", -1));
        questions.add(new Question("SUQ-G7. When you get a notification on your phone, how often do you check it immediately?", -1));
        questions.add(new Question("SUQ-G8. How often do you use the calendar (or similar productivity apps?)", -1));
        questions.add(new Question("SUQ-G9. How often do you check social media apps such as snapchat, facebook, or twitter?", -1));
        questions.add(new Question("SUQ-G10. How often do you use your phone for entertainment purposes (i.e. apps and games)?", -1));


        questions.add(new Question("SUQ-A1. How often do you open your phone to do one thing and wind up doing something else\n" +
                "without realizing it?", -1));
        questions.add(new Question("SUQ-A2. How often do you check your phone while interacting with other people (i.e. during\n" +
                "conversation)?", -1));
        questions.add(new Question("SUQ-A3. How often do you find yourself checking your phone “for no good reason”?", -1));
        questions.add(new Question("SUQ-A4. How often do you automatically check your phone without a purpose?", -1));
        questions.add(new Question("SUQ-A5. How often do you check your phone out of habit?", -1));
        questions.add(new Question("SUQ-A6. How often do you find yourself checking your phone without realizing why you did it?", -1));
        questions.add(new Question("SUQ-A7. How often have you realized you checked your phone only after you have already been\n" +
                "using it?", -1));
        questions.add(new Question("SUQ-A8. How often do you find yourself using your phone absent-mindedly?", -1));
        questions.add(new Question("SUQ-A9. How often do you wind up using your phone for longer than you intended to?", -1));
        questions.add(new Question("SUQ-A10. How often do you lose track of time while using your phone?", -1));
    }
}
