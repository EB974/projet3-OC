package com.eric_b.moodtracker.Modeles;

import com.eric_b.moodtracker.R;

public class MoodObject {

    private int[] bkground = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow};
    private int[] smeily = {R.drawable.smiley_sad,R.drawable.smiley_disappointed,R.drawable.smiley_normal,R.drawable.smiley_happy,R.drawable.smiley_super_happy};
    private int screen;

    public void SetScreen(int screen){
        this.screen = screen;
    }

    public int GetImage(){
        return smeily[screen];
    }


    public int GetBkground() {
        return bkground[screen];
    }

}
