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

public class HistoryActivity extends AppCompatActivity{

    // declaration of mood variables
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
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthScreen = metrics.widthPixels;
        int min;
        recoverMemMood();
        int sizeListMood = dateRec.size()-1;
        if (sizeListMood-7 >=0){
            min = sizeListMood-7;
        }else{
            min = sizeListMood;
        }
        int [] txtViewName = {R.id.day1Text,R.id.day2Text,R.id.day3Text,R.id.day4Text,R.id.day5Text,R.id.day6Text,R.id.day7Text};
        int [] buttonViewName = {R.id.day1ImageButton,R.id.day2ImageButton,R.id.day3ImageButton,R.id.day4ImageButton,R.id.day5ImageButton,R.id.day6ImageButton,R.id.day7ImageButton};
        MoodObject bckColor = new MoodObject();
        Calendar dateDay =  Calendar.getInstance();
        Calendar dateMem =  Calendar.getInstance();
        int viewPosition = 0;
        for(int i=min; i >=0 ; i--){
            dateMem.setTimeInMillis(dateRec.get(i));
            int diffDate = dateDay.get(Calendar.DATE)-dateMem.get(Calendar.DATE);
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

            TextView currentTxtView = findViewById(txtViewName[viewPosition]);
            currentTxtView.setText(txt);
            currentTxtView.setWidth(Math.round(widthScreen/100*25*(moodRec.get(i)+1)));
            bckColor.SetScreen(moodRec.get(i));
            currentTxtView.setBackgroundResource(bckColor.GetBkground());

            if (noteRec.get(i).length()>0){
                currentButton = findViewById(buttonViewName[i]);
                currentButton.setVisibility(View.VISIBLE);
            }
            viewPosition++;

        }
        
    }

    private void recoverMemMood(){

        // Recover Arraylist of Mood memory
        if (moodPref.getString(DATE_MEM_MOOD, null) != null) {
            dateRec = recupMoodDate(DATE_MEM_MOOD);
            moodRec = recupMoodMood(MOOD_MEM_MOOD);
            noteRec = recupMoodNote(NOTE_MEM_MOOD);
        }


    }


    protected ArrayList<Long> recupMoodDate(String MEM_MOOD_INFO){

        Gson dateGson = new Gson();
        ArrayList list= new ArrayList<>();
        ArrayList<Long> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MEM_MOOD_INFO, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            long nb = Math.round((double)aList);
            listreturn.add(nb);
        }
        return listreturn;}

    protected ArrayList<Integer> recupMoodMood(String MEM_MOOD_INFO) {

        Gson dateGson = new Gson();
        ArrayList list = new ArrayList<>();
        ArrayList<Integer> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MEM_MOOD_INFO, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            int nb = (int)((double)aList);
            listreturn.add(nb);
        }
        return listreturn;
    }

    protected ArrayList<String> recupMoodNote(String MEM_MOOD_INFO) {
        //SharedPreferences moodPref = getPreferences(MODE_PRIVATE);
        Gson dateGson = new Gson();
        ArrayList list = new ArrayList<>();
        ArrayList<String> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MEM_MOOD_INFO, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            listreturn.add((String) aList);
        }
        return listreturn;
    }

}
