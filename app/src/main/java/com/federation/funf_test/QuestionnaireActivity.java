package com.federation.funf_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.federation.funf_test.KeystokeLogger.KeystrokeLoggerActivity;
import com.federation.funf_test.ListAdapter.Question;
import com.federation.funf_test.ListAdapter.QuestionnaireListAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionnaireActivity extends Activity {

    ArrayList<Question> questions = new ArrayList<>();
    ListView listView;
    Button nextButton;
    ArrayList<Boolean> answerList = new ArrayList<>();
    ArrayList params = new ArrayList();
    ArrayList<JSONObject> questionArrayList = new ArrayList<>();

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url_create = "https://pos.pentacle.tech/api/questionaire/create";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_list);
        nextButton = (Button) findViewById(R.id.questionnaireNextButton);
        listView = (ListView) findViewById(R.id.questionnaireListView);
        setQuestions();

        QuestionnaireListAdapter questionnaireListAdapter = new QuestionnaireListAdapter(this, questions);
        listView.setAdapter(questionnaireListAdapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int canSave = 0;
                answerList = new ArrayList<>();
                for(int i = 0; i < questions.size(); i++){
                    Log.d("QUESTIONNAIRE", "onClick: " + questions.get(i).getQuestion());
                    Log.d("QUESTIONNAIRE", "onClick: " + questions.get(i).getAnswerId());

                    //Check if user answered all question?
                    if(questions.get(i).getAnswerId() == -1) {
                        answerList.add(false);
                    } else {
                        answerList.add(true);
                    }
                }

                for (int i = 0; i < answerList.size(); i++) {
                    if(answerList.get(i) == true){
                        canSave += 1;
                    } else {
                        //Do nothing
                    }
                }

                if(canSave == answerList.size()){
                    //SAVE DATA and Navigate to keyboard tracker
                    String androidId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    ArrayList question_list = new ArrayList();
                    ArrayList answer_list = new ArrayList();

                    JSONObject newQuestion = new JSONObject();

                    for(int i = 0; i < questions.size(); i++){
                        question_list.add('\"' + questions.get(i).getQuestion() + '\"');
                        answer_list.add(questions.get(i).getAnswerId());
                    }

                    params.add(new BasicNameValuePair("device_id", androidId));
                    params.add(new BasicNameValuePair("question_list", question_list.toString()));
                    params.add(new BasicNameValuePair("answer_list", answer_list.toString()));
                    new CreateNewResult().execute();


                } else {
                    Toast.makeText(getApplicationContext(), "You need to answer all question", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setQuestions() {
        questions.add(new Question("SUQ-G1. How often do you have your cellphone on your person?", -1));
        questions.add(new Question("SUQ-G2. How frequently do you send and receive text messages or emails?", -1));
        questions.add(new Question("SUQ-G3. To what extent do you have push notifications enabled on your phone?", -1));
        questions.add(new Question("SUQ-G4. How often do you find yourself checking your phone for new events such as text messages or emails?", -1));
        questions.add(new Question("SUQ-G5. How often do you use the phone for reading the news or browsing the web?", -1));
        questions.add(new Question("SUQ-G6. How often do you use sound notifications on your phone?", -1));
        questions.add(new Question("SUQ-G7. When you get a notification on your phone, how often do you check it immediately?", -1));
        questions.add(new Question("SUQ-G8. How often do you use the calendar (or similar productivity apps?)", -1));
        questions.add(new Question("SUQ-G9. How often do you check social media apps such as snapchat, facebook, or twitter?", -1));
        questions.add(new Question("SUQ-G10. How often do you use your phone for entertainment purposes (i.e. apps and games)?", -1));

        questions.add(new Question("SUQ-A1. How often do you open your phone to do one thing and wind up doing something else without realizing it?", -1));
        questions.add(new Question("SUQ-A2. How often do you check your phone while interacting with other people (i.e. during conversation)?", -1));
        questions.add(new Question("SUQ-A3. How often do you find yourself checking your phone “for no good reason”?", -1));
        questions.add(new Question("SUQ-A4. How often do you automatically check your phone without a purpose?", -1));
        questions.add(new Question("SUQ-A5. How often do you check your phone out of habit?", -1));
        questions.add(new Question("SUQ-A6. How often do you find yourself checking your phone without realizing why you did it?", -1));
        questions.add(new Question("SUQ-A7. How often have you realized you checked your phone only after you have already been using it?", -1));
        questions.add(new Question("SUQ-A8. How often do you find yourself using your phone absentmindedly?", -1));
        questions.add(new Question("SUQ-A9. How often do you wind up using your phone for longer than you intended to?", -1));
        questions.add(new Question("SUQ-A10. How often do you lose track of time while using your phone?", -1));
    }

    class CreateNewResult extends AsyncTask<String, String, JSONObject> {
        /**
         * Before starting background thread Show Progress Dialog
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QuestionnaireActivity.this);
            pDialog.setMessage("Uploading Result..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected JSONObject doInBackground(String... args) {
            // getting JSON Object
            // Note that create product url accepts POST methodN
            JSONObject json = jsonParser.makeHttpRequest(url_create,
                    "POST", params);
            // check log cat fro response
            //Log.d("Debug", json.toString());

            return json;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(JSONObject file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Intent toKeystrokeLoggerIntent = new Intent(QuestionnaireActivity.this, KeystrokeLoggerActivity.class);
            startActivity(toKeystrokeLoggerIntent);
        }
    }
}
