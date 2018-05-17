package com.eric_b.moodtracker.Views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eric_b.moodtracker.Controllers.MainActivity;
import com.eric_b.moodtracker.R;

public class MoodScreen extends MainActivity {


    private int[] bkground = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow};
    private int[] smeily = {R.drawable.smiley_sad,R.drawable.smiley_disappointed,R.drawable.smiley_normal,R.drawable.smiley_happy,R.drawable.smiley_super_happy};
    int screen;
    ImageView image;
    RelativeLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void SetImage(int mem_screen, final ImageView image){
        screen = mem_screen;
        this.image = image;
        image.setImageResource(smeily[screen]);
    }


    public void SetBkground(int mem_screen, final RelativeLayout layout) {
        screen = mem_screen;
        this.layout = layout;
        layout.setBackgroundResource(bkground[screen]);
    }

}
