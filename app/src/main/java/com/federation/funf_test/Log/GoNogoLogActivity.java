package com.federation.funf_test.Log;

import android.app.Activity;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.federation.funf_test.Log.Adapter.Test;
import com.federation.funf_test.Log.Adapter.TestListAdapter;
import com.federation.funf_test.R;
import com.federation.funf_test.SQLite.DatabaseHelper;

import java.util.ArrayList;

public class GoNogoLogActivity extends Activity {
    DatabaseHelper databaseHelper;
    ArrayList arrayList;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_gonogo);
        textView = (TextView) findViewById(R.id.logGoNogotextView);
        databaseHelper = new DatabaseHelper(GoNogoLogActivity.this);
        textView.setText("");
        showData();
    }

    private void showData(){
        Cursor data = databaseHelper.getData();
        ArrayList<Test> listData = new ArrayList<>();

        while (data.moveToNext()){
            String question = data.getString(1);
            String anwTime = data.getString(2);
            String result = data.getString(3);
            String date = data.getString(4);
            listData.add(new Test(question, anwTime, result, date));
        }

        for(int i = 0; i < listData.size(); i++){
            textView.append(listData.get(i).getStatus() + "  " + listData.get(i).getAnsTime()+ "  " + listData.get(i).getResult()+ "  " + listData.get(i).getDate());
            textView.append("\n");
        }
    }
}
