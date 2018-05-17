package com.eric_b.moodtracker;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity{
    int screen = 2;

    //int img = R.drawable.smiley_normal;



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
            System.out.println("ok1");

            currentView.setOnTouchListener(
                    new View.OnTouchListener() {
                        int X, Y, Y1=0,Y2=0;
                        int deltaY=0;
                        boolean move=true;

                        public boolean onTouch(View myView, MotionEvent event) {
                            X = (int) event.getRawX();
                            Y = (int) event.getRawY();

                            int action = event.getAction();
                            if (!move) {
                                System.out.println("Y1 =  "+Y1);
                                System.out.println("Y2 =  "+Y2);
                                deltaY = Y1 - Y2;

                                Y1=0;
                                Y2=0;
                            }

                            System.out.println("deltaY =  "+deltaY);
                            System.out.println("action =  "+action);
                            System.out.println("move =  "+move);
                            if (deltaY > 200 && !move) {
                                screen = screen + 1;
                                System.out.println("move up ");
                                if (screen > 4) screen = 4;
                                image.SetImage(screen, moodImage);
                                image.SetBkground(screen,layout);
                            }
                            if (deltaY < -200 && !move) {
                                screen = screen - 1;
                                System.out.println("move down ");
                                if (screen < 0) screen = 0;
                                image.SetImage(screen, moodImage);
                                image.SetBkground(screen,layout);
                            }

                            switch (action) {
                                case (MotionEvent.ACTION_DOWN):
                                    Y1 = (int)event.getY();

                                    return true;

                                case (MotionEvent.ACTION_MOVE):
                                    Y2 = (int)event.getY();
                                    return true;

                                case (MotionEvent.ACTION_UP):
                                    move=false;

                                    return true;

                                case (MotionEvent.ACTION_CANCEL):
                                    return true;

                                case (MotionEvent.ACTION_OUTSIDE):
                                    return true;

                                default:
                                    return false;

                            }
                        }
                    }
            );

        }

}
