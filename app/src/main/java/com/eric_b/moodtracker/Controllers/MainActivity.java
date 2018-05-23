package com.eric_b.moodtracker.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

    public static final String BUNDLE_CURRENT_MOOD = "MOOD";
    public static final String BUNDLE_CURRENT_NOTE = "NOTE";
    SharedPreferences daylyMoodPref;
    public static final String DATE_CURRENT_MOOD = "DATE_CURRENT_MOOD";
    public static final String MOOD_CURRENT_MOOD = "MOOD_CURRENT_MOOD";
    public static final String NOTE_CURRENT_MOOD = "NOTE_CURRENT_MOOD";
    SharedPreferences moodPref;
    public static final String PREF_MEM_MOOD = "NAME_MEM_MOOD";
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
        moodPref = getSharedPreferences(PREF_MEM_MOOD, MODE_PRIVATE);

        if (savedInstanceState != null) {
            mood = savedInstanceState.getInt(BUNDLE_CURRENT_MOOD);
            dayNote = savedInstanceState.getString(BUNDLE_CURRENT_NOTE);
        } else {
            LoadRecordedMood();
        }
        MoodChange(mood,false);

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
                                    MoodChange(mood,true);
                                    sound(mood);
                                }
                                if (deltaY < -50) {
                                    mood--;
                                    if (mood < 0) mood = 0;
                                    MoodChange(mood,true);
                                    sound(mood);
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

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        daylyRecordMood();
    }

    private void MoodChange (int screen, boolean noteChange){
            RelativeLayout layout = findViewById(R.id.moodScreen);
            MoodObject image = new MoodObject();
            ImageView moodImage = findViewById(R.id.moodImage);
            image.SetMood(screen);
            moodImage.setImageResource(image.GetImage());
            layout.setBackgroundResource(image.GetBkground());
            //daylyRecordMood();
            mood = screen;
            if (noteChange) dayNote = "";
        }

        @Override
        protected void onSaveInstanceState (Bundle outState){
            outState.putInt(BUNDLE_CURRENT_MOOD, mood);
            outState.putString(BUNDLE_CURRENT_NOTE, dayNote);
            super.onSaveInstanceState(outState);
        }

        private void note () {
            ViewGroup viewG = new ViewGroup(this) {
                @Override
                protected void onLayout(boolean unchanged, int l, int t, int r, int b) {
                }
            };
            final View boxView = LayoutInflater.from(this).inflate(R.layout.dialog_box, viewG, false);
            final AlertDialog.Builder box = new AlertDialog.Builder(this);
            box.setView(boxView);
            box.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText inputNote = boxView.findViewById(R.id.EditTextnote);
                        dayNote = inputNote.getText().toString();
                        //daylyRecordMood();
                    }
                })
                .create()
                .show();
        }


    private void LoadRecordedMood(){
        Calendar now = Calendar.getInstance();
        Calendar moodDate = Calendar.getInstance();
        if (daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0) != 0) {
            moodDate.setTimeInMillis(daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0));
        }
        if (now.get(Calendar.DATE)-moodDate.get(Calendar.DATE)==0) {
            mood = daylyMoodPref.getInt(MOOD_CURRENT_MOOD, 3);
            dayNote = daylyMoodPref.getString(NOTE_CURRENT_MOOD, null);
        }else{
            recordMood();
            mood = 3;

        }
    }


    private void daylyRecordMood(){
        Calendar now = Calendar.getInstance();
            daylyMoodPref.edit().putLong(DATE_CURRENT_MOOD, now.getTime().getTime()).apply();
            daylyMoodPref.edit().putInt(MOOD_CURRENT_MOOD, mood).apply();
            daylyMoodPref.edit().putString(NOTE_CURRENT_MOOD, dayNote).apply();

    }

        private void recordMood(){
            ArrayList<Long> dateRec = new ArrayList<>();
            ArrayList<Integer> moodRec = new ArrayList<>();
            ArrayList<String> noteRec = new ArrayList<>();
            Gson dateGson = new Gson();
            Gson moodGson = new Gson();
            Gson noteGson = new Gson();

            // Recover Arraylist of Mood memory
            if (moodPref.getString(DATE_MEM_MOOD, null) != null) {
                dateRec = recupMoodDate(DATE_MEM_MOOD);
                moodRec = recupMoodMood(MOOD_MEM_MOOD);
                noteRec = recupMoodNote(NOTE_MEM_MOOD);
            }

                dateRec.add(daylyMoodPref.getLong(DATE_CURRENT_MOOD, 0));
                moodRec.add(daylyMoodPref.getInt(MOOD_CURRENT_MOOD, 0));
                noteRec.add(daylyMoodPref.getString(NOTE_CURRENT_MOOD, null));
                moodPref.edit().putString(DATE_MEM_MOOD,dateGson.toJson(dateRec)).apply();
                moodPref.edit().putString(MOOD_MEM_MOOD,moodGson.toJson(moodRec)).apply();
                moodPref.edit().putString(NOTE_MEM_MOOD,noteGson.toJson(noteRec)).apply();
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

    protected MediaPlayer mPlayer = null;

    private void sound (int soundMood){
        MoodObject sound = new MoodObject();
        sound.SetMood(soundMood);
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this, sound.Getsound());
        mPlayer.start();
    }
}

