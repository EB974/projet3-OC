package com.eric_b.moodtracker.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eric_b.moodtracker.Views.MoodScreen;
import com.eric_b.moodtracker.R;

public class MainActivity extends AppCompatActivity{
    int screen = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout layout = findViewById(R.id.moodScreen);
        final MoodScreen image = new MoodScreen();
        final ImageView moodImage = (ImageView) findViewById(R.id.moodImage);
        image.SetImage(screen, moodImage);
        image.SetBkground(screen,layout);
        final View currentView;

            currentView = (View) findViewById(R.id.moodScreen);

            currentView.setOnTouchListener(
                    new View.OnTouchListener() {
                        int X, Y, Y1=0,Y2=0;
                        int deltaY=0;

                        public boolean onTouch(View myView, MotionEvent event) {
                            X = (int) event.getRawX();
                            Y = (int) event.getY();
                            int action = event.getAction();

                            switch (action) {
                                case (MotionEvent.ACTION_DOWN):
                                    Y1 = (int)event.getY();
                                    return true;

                                case (MotionEvent.ACTION_UP):
                                    Y2 = (int)event.getY();
                                    deltaY = Y1 - Y2;
                                    if (deltaY > 50) {
                                        screen++;
                                        if (screen > 4) screen = 4;
                                        image.SetImage(screen, moodImage);
                                        image.SetBkground(screen,layout);
                                    }
                                    if (deltaY < 50) {
                                        screen--;
                                        if (screen < 0) screen = 0;
                                        image.SetImage(screen, moodImage);
                                        image.SetBkground(screen, layout);
                                    }
                                    return true;
                                default:
                                    return false;
                            }

                        }

                    }

            );

        }

}
