package com.federation.funf_test.Log.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.federation.funf_test.R;

import java.util.ArrayList;

public class TestListAdapter extends ArrayAdapter<Test> {

    private Context context;
    int res;

    public TestListAdapter(Context context, int resources, ArrayList<Test> testArrayList) {
        super(context, resources, testArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String status = getItem(position).getStatus();
        String anwTime = getItem(position).getAnsTime();
        String result = getItem(position).getResult();
        String date = getItem(position).getDate();

        Test test = new Test(status, anwTime, result, date);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(res, parent, false);

        TextView staTV = (TextView) convertView.findViewById(R.id.logQuestionTextView);
        TextView anwTV = (TextView) convertView.findViewById(R.id.logAnsTimeTextView);
        TextView resTV = (TextView) convertView.findViewById(R.id.logResultTextView);
        TextView dateTV = (TextView) convertView.findViewById(R.id.logDateTextView);

        staTV.setText(status);
        anwTV.setText(anwTime);
        resTV.setText(result);
        dateTV.setText(date);



        return super.getView(position, convertView, parent);
    }
}
