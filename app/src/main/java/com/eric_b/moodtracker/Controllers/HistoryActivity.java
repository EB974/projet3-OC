package com.eric_b.moodtracker.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eric_b.moodtracker.Modeles.MoodObject;
import com.eric_b.moodtracker.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity{ //shows the last 7 moods recorded

    // declaration of moods variables
    SharedPreferences moodPref;
    public static final String PREF_MEM_MOOD = "NAME_MEM_MOOD";
    public static final String DATE_MEM_MOOD = "DATE_MEM_MOOD";
    public static final String MOOD_MEM_MOOD = "MOOD_MEM_MOOD";
    public static final String NOTE_MEM_MOOD = "NOTE_MEM_MOOD";
    ArrayList<Long> dateRec = new ArrayList<>();
    ArrayList<Integer> moodRec = new ArrayList<>();
    ArrayList<String> noteRec = new ArrayList<>();
    ImageButton currentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        moodPref = getSharedPreferences(PREF_MEM_MOOD, MODE_PRIVATE);

        ImageButton noteButton1Day = findViewById(R.id.day1ImageButton);
        ImageButton noteButton2Day = findViewById(R.id.day2ImageButton);
        ImageButton noteButton3Day = findViewById(R.id.day3ImageButton);
        ImageButton noteButton4Day = findViewById(R.id.day4ImageButton);
        ImageButton noteButton5Day = findViewById(R.id.day5ImageButton);
        ImageButton noteButton6Day = findViewById(R.id.day6ImageButton);
        ImageButton noteButton7Day = findViewById(R.id.day7ImageButton);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics); //recovering the width of the screen

        int orientation = getResources().getConfiguration().orientation;
        int sizeScreen,coef;
        int viewPosition = 0;
        int rectifiedPosition;
        if (orientation==1) {
            sizeScreen= metrics.widthPixels;
            coef = 25;
        }
        else  {
            sizeScreen = metrics.widthPixels;
            coef = 20;
        }
        int min;
        recoverMemMood();
        int sizeListMood = dateRec.size()-1;
        if (sizeListMood-6 >=0){
            min = sizeListMood-6;
            rectifiedPosition = 0;
        }else{
            min = 0;
            rectifiedPosition = 6-sizeListMood;
        }
        int [] txtViewName = {R.id.day1Text,R.id.day2Text,R.id.day3Text,R.id.day4Text,R.id.day5Text,R.id.day6Text,R.id.day7Text};
        int [] buttonViewName = {R.id.day1ImageButton,R.id.day2ImageButton,R.id.day3ImageButton,R.id.day4ImageButton,R.id.day5ImageButton,R.id.day6ImageButton,R.id.day7ImageButton};
        MoodObject bckColor = new MoodObject();
        Calendar dateDay =  Calendar.getInstance();
        Calendar dateMem =  Calendar.getInstance();

        for(int i=min; i <=sizeListMood ; i++){ //displays moods
            dateMem.setTimeInMillis(dateRec.get(i));
            int diffDate = dateDay.get(Calendar.DAY_OF_YEAR)-dateMem.get(Calendar.DAY_OF_YEAR);
            System.out.println("date01 : "+dateDay.get(Calendar.DAY_OF_YEAR)+"   date2 : "+dateMem.get(Calendar.DAY_OF_YEAR));
            String txt;
            switch (diffDate){
                case (1) : txt = "Hier";
                break;
                case (2) : txt = "Avant hier";
                break;
                case (7) : txt= "Il y a une semaine";
                break;
                default : txt= "Il y a "+Integer.toString(diffDate)+" jours";
            }
            if (diffDate > 7) txt = "Il y a plus d'une semaine";
            TextView currentTxtView = findViewById(txtViewName[rectifiedPosition+viewPosition]);
            currentTxtView.setText(txt);
            currentTxtView.setWidth(Math.round(sizeScreen/100*coef*(moodRec.get(i)+1)));
            bckColor.SetMood(moodRec.get(i));
            currentTxtView.setBackgroundResource(bckColor.GetBkground());

            if (noteRec.get(i)!=null){
                currentButton = findViewById(buttonViewName[rectifiedPosition+viewPosition]);
                currentButton.setVisibility(View.VISIBLE);
            }
            viewPosition++;

        }

        // Display button comment
        noteButton1Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-7), Toast.LENGTH_LONG).show();
            }
        });

        noteButton2Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-6), Toast.LENGTH_LONG).show();
            }
        });

        noteButton3Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-5), Toast.LENGTH_SHORT).show();
            }
        });

        noteButton4Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-4), Toast.LENGTH_LONG).show();
            }
        });

        noteButton5Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-3), Toast.LENGTH_LONG).show();
            }
        });

        noteButton6Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-2), Toast.LENGTH_LONG).show();
            }
        });

        noteButton7Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Toast.makeText(cont, noteRec.get(noteRec.size()-1), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void recoverMemMood(){ //Recover the moods memory

        // Recover Arraylist of Mood memory
        if (moodPref.getString(DATE_MEM_MOOD, null) != null) {
            dateRec = recupMoodDate();
            moodRec = recupMoodMood();
            noteRec = recupMoodNote();

        }


    }


    private ArrayList<Long> recupMoodDate(){  //Recover moods date recorded with Gson lib

        Gson dateGson = new Gson();
        ArrayList list= new ArrayList<>();
        ArrayList<Long> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(DATE_MEM_MOOD, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            long nb = Math.round((double)aList);
            listreturn.add(nb);
        }
        return listreturn;}

    private ArrayList<Integer> recupMoodMood() {  //Recover moods recorded with Gson lib

        Gson dateGson = new Gson();
        ArrayList list = new ArrayList<>();
        ArrayList<Integer> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MOOD_MEM_MOOD, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            int nb = (int)((double)aList);
            listreturn.add(nb);
        }
        return listreturn;
    }

    private ArrayList<String> recupMoodNote() {  //Recover moods comment recorded with Gson lib
        //SharedPreferences moodPref = getPreferences(MODE_PRIVATE);
        Gson dateGson = new Gson();
        ArrayList list = new ArrayList<>();
        ArrayList<String> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(NOTE_MEM_MOOD, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            listreturn.add((String) aList);
        }
        return listreturn;
    }

}
