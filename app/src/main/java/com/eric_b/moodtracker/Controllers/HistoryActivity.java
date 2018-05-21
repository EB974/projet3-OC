package com.eric_b.moodtracker.Controllers;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eric_b.moodtracker.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    // declaration of mood variables
    SharedPreferences moodPref;
    public static final String BUNDLE_CURRENT_MOOD = "MOOD";
    public static final String DATE_CURRENT_MOOD = "DATE_CURRENT_MOOD";
    public static final String MOOD_CURRENT_MOOD = "MOOD_CURRENT_MOOD";
    public static final String NOTE_CURRENT_MOOD = "NOTE_CURRENT_MOOD";
    public static final String DATE_MEM_MOOD = "DATE_MEM_MOOD";
    public static final String MOOD_MEM_MOOD = "MOOD_MEM_MOOD";
    public static final String NOTE_MEM_MOOD = "NOTE_MEM_MOOD";
    ArrayList<Long> dateRec = new ArrayList<>();
    ArrayList<Integer> moodRec = new ArrayList<>();
    ArrayList<String> noteRec = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView Txt1Day = findViewById(R.id.day1Text);
        TextView Txt2Day = findViewById(R.id.day2Text);
        TextView Txt3Day = findViewById(R.id.day3Text);
        TextView Txt4Day = findViewById(R.id.day4Text);
        TextView Txt5Day = findViewById(R.id.day5Text);
        TextView Txt6Day = findViewById(R.id.day6Text);
        TextView Txt7Day = findViewById(R.id.day7Text);

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
        int mood;
        recoverMemMood();
        int sizeListMood = dateRec.size();
        System.out.println("taille liste "+sizeListMood);
        int intName = 7;
        for (int i = sizeListMood ; i<=0 ; i--){
            String nameTxtView="Txt"+Integer.toString(intName)+"Day";
            //System.out.println(nameTxtView);
            intName--;
        }
        //int widthText = Math.round(widthScreen/100*(20*mood));
        Txt7Day.setBackgroundResource(R.color.faded_red);
        Txt7Day.setText("Il y a une semaine");
        //Txt7Day.setWidth(widthText);
    }

    private void recoverMemMood(){
        moodPref = getPreferences(MODE_PRIVATE);
        SharedPreferences moodPref = getPreferences(MODE_PRIVATE);

        // Recover Arraylist of Mood memory
        if (moodPref.getString(DATE_MEM_MOOD, null) != null) {
            dateRec = recupMoodDate(DATE_MEM_MOOD);
            moodRec = recupMoodMood(MOOD_MEM_MOOD);
            noteRec = recupMoodNote(NOTE_MEM_MOOD);
        }
    }

    protected ArrayList<Long> recupMoodDate(String MEM_MOOD_INFO){
        SharedPreferences moodPref = getPreferences(MODE_PRIVATE);
        Gson dateGson = new Gson();
        ArrayList list= new ArrayList<>();
        ArrayList<Long> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MEM_MOOD_INFO, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            long nb = Math.round((double)aList);
            System.out.println(aList+"  "+nb);
            listreturn.add(nb);
        }
        return listreturn;}

    protected ArrayList<Integer> recupMoodMood(String MEM_MOOD_INFO) {
        SharedPreferences moodPref = getPreferences(MODE_PRIVATE);
        Gson dateGson = new Gson();
        ArrayList list = new ArrayList<>();
        ArrayList<Integer> listreturn= new ArrayList<>();
        String getGson;
        getGson = moodPref.getString(MEM_MOOD_INFO, null);
        list = dateGson.fromJson(getGson, list.getClass());
        for (Object aList : list) {
            int nb = (int)((double)aList);
            System.out.println(aList+"  "+nb);
            listreturn.add(nb);
        }
        return listreturn;
    }

    protected ArrayList<String> recupMoodNote(String MEM_MOOD_INFO) {
        SharedPreferences moodPref = getPreferences(MODE_PRIVATE);
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
