package com.eric_b.moodtracker.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.eric_b.moodtracker.Modeles.MoodObject;
import com.eric_b.moodtracker.R;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;



public class MainActivity extends AppCompatActivity{


    // declaration of mood variables

    SharedPreferences daylyMoodPref;
    SharedPreferences moodPref;
    public static final String BUNDLE_CURRENT_MOOD = "MOOD";
    public static final String DATE_CURRENT_MOOD = "DATE_CURRENT_MOOD";
    public static final String MOOD_CURRENT_MOOD = "MOOD_CURRENT_MOOD";
    public static final String NOTE_CURRENT_MOOD = "NOTE_CURRENT_MOOD";
    public static final String DATE_MEM_MOOD = "DATE_MEM_MOOD";
    public static final String MOOD_MEM_MOOD = "MOOD_MEM_MOOD";
    public static final String NOTE_MEM_MOOD = "NOTE_MEM_MOOD";
    int mood;
    String dayNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton noteButton = findViewById(R.id.buttonAddNote);
        ImageButton historyButton = findViewById(R.id.buttonHistory);
        daylyMoodPref = getPreferences(MODE_PRIVATE);
        moodPref = getPreferences(MODE_PRIVATE);

        if (savedInstanceState != null) {
            mood = savedInstanceState.getInt(BUNDLE_CURRENT_MOOD);
        } else {
            mood = 2;
        }
        MoodChange(mood);

        final View currentView;
        currentView = findViewById(R.id.moodScreen);
        currentView.setOnTouchListener(
                new View.OnTouchListener() {
                    int Y1, Y2, deltaY;

                    public boolean onTouch(View myView, MotionEvent event) {
                        int action = event.getAction();

                        switch (action) {
                            case (MotionEvent.ACTION_DOWN):
                                Y1 = (int) event.getY();
                                return true;

                            case (MotionEvent.ACTION_UP):
                                myView.performClick();
                                Y2 = (int) event.getY();
                                deltaY = Y1 - Y2;
                                if (deltaY > 50) {
                                    mood++;
                                    if (mood > 4) mood = 4;
                                    MoodChange(mood);
                                }
                                if (deltaY < -50) {
                                    mood--;
                                    if (mood < 0) mood = 0;
                                    MoodChange(mood);
                                }
                                return true;
                            default:
                                return false;
                        }

                    }

                }

        );

        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note();
            }
        });
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(historyActivity);
            }
        });
    }

        private void MoodChange ( int screen){
            RelativeLayout layout = findViewById(R.id.moodScreen);
            MoodObject image = new MoodObject();
            ImageView moodImage = findViewById(R.id.moodImage);
            image.SetScreen(screen);
            moodImage.setImageResource(image.GetImage());
            layout.setBackgroundResource(image.GetBkground());
            mood = screen;
        }

        @Override
        protected void onSaveInstanceState (Bundle outState){
            outState.putInt(BUNDLE_CURRENT_MOOD, mood);
            super.onSaveInstanceState(outState);
        }

        private void note () {
            LayoutInflater factory = LayoutInflater.from(this);
            ViewGroup viewG = new ViewGroup(this) {
                @Override
                protected void onLayout(boolean changed, int l, int t, int r, int b) {

                }
            };
            final View alertDialogView = factory.inflate(R.layout.dialog_box, viewG);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(alertDialogView);
            builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText txt = alertDialogView.findViewById(R.id.EditTextnote);
                            dayNote = txt.getText().toString();
                            recMood();
                        }
                    })
                    .create()
                    .show();
        }

        private void recMood(){
            daylyMoodPref = getPreferences(MODE_PRIVATE);
            //moodPref = getPreferences(MODE_PRIVATE);
            Calendar now = Calendar.getInstance();
            Calendar moodDate = Calendar.getInstance();
            ArrayList<Long> dateRec = new ArrayList<>();
            ArrayList<Integer> moodRec = new ArrayList<>();
            ArrayList<String> noteRec = new ArrayList<>();
            Gson dateGson = new Gson();
            Gson moodGson = new Gson();
            Gson noteGson = new Gson();

            if (daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0) != 0) {
                moodDate.setTimeInMillis(daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0));
            }

            // Recover Arraylist of Mood memory
            if (moodPref.getString(DATE_MEM_MOOD, null) != null) {
                dateRec = recupMoodDate(DATE_MEM_MOOD);
                moodRec = recupMoodMood(MOOD_MEM_MOOD);
                noteRec = recupMoodNote(NOTE_MEM_MOOD);
            }

            // put new mood in dayly SharedPreferences
            if (now.get(Calendar.DATE)-moodDate.get(Calendar.DATE)==0){
                daylyMoodPref.edit().putLong(DATE_CURRENT_MOOD,now.getTime().getTime()).apply();
                daylyMoodPref.edit().putInt(MOOD_CURRENT_MOOD,mood).apply();
                daylyMoodPref.edit().putString(NOTE_CURRENT_MOOD,dayNote).apply();
            }
            else{
                dateRec.add(daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0));
                moodRec.add(daylyMoodPref.getInt(MOOD_CURRENT_MOOD, 0));
                noteRec.add(daylyMoodPref.getString(NOTE_CURRENT_MOOD, null));
                moodPref.edit().putString(DATE_MEM_MOOD,dateGson.toJson(dateRec)).apply();
                moodPref.edit().putString(MOOD_MEM_MOOD,moodGson.toJson(moodRec)).apply();
                moodPref.edit().putString(NOTE_MEM_MOOD,noteGson.toJson(noteRec)).apply();
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

