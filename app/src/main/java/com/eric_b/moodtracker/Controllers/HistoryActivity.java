package com.eric_b.moodtracker.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eric_b.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    private void calculPeriod(){
        java.util.Date date = new java.util.Date();
        java.util.Date date2 = new java.util.Date(118,4,17);
        long date3 = date2.getTime() -  date.getTime();
        System.out.println(date);
        System.out.println(date2);
        System.out.println(date3 /86400000);

    }
}
