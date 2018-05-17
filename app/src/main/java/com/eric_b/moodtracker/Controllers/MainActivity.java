package com.eric_b.moodtracker.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eric_b.moodtracker.Views.MoodScreen;
import com.eric_b.moodtracker.R;

public class MainActivity extends AppCompatActivity{

    public static final String BUNDLE_CURRENT_MOOD = "MOOD";
    int mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton noteButton = (ImageButton) findViewById(R.id.buttonAddNote);

        if (savedInstanceState != null) {
            mood = savedInstanceState.getInt(BUNDLE_CURRENT_MOOD);
        } else {
            mood = 2;
        }
        MoodChange(mood);

        final View currentView;
        currentView = (View) findViewById(R.id.moodScreen);

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
    }

        private void MoodChange ( int screen){
            RelativeLayout layout = findViewById(R.id.moodScreen);
            MoodScreen image = new MoodScreen();
            ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
            image.SetImage(screen, moodImage);
            image.SetBkground(screen, layout);
            mood = screen;
        }

        @Override
        protected void onSaveInstanceState (Bundle outState){
            outState.putInt(BUNDLE_CURRENT_MOOD, mood);
            super.onSaveInstanceState(outState);
        }

        private void note () {
            LayoutInflater factory = LayoutInflater.from(this);
            final View alertDialogView = factory.inflate(R.layout.dialog_box, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setView(alertDialogView);

            builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("Annuler");
                            //finish();
                        }
                    })
                    .setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText txt = (EditText)alertDialogView.findViewById(R.id.EditTextnote);
                            String chaine = txt.getText().toString();
                            System.out.println("Valider");
                            System.out.println(chaine);
                            //Intent intent = new Intent();
                            //intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                            //setResult(RESULT_OK, intent);
                            //mPreferences.edit().putInt(PREF_KEY_SCORE, mScore).commit();
                            //finish();
                        }
                    })
                    .create()
                    .show();
        }


}

