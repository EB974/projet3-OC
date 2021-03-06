package com.eric_b.moodtracker.Modeles;

import com.eric_b.moodtracker.R;

public class MoodObject {

    private int[] bkground = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow};
    private int[] smiley = {R.drawable.smiley_sad,R.drawable.smiley_disappointed,R.drawable.smiley_normal,R.drawable.smiley_happy,R.drawable.smiley_super_happy};
    private int[] sound = new int[]{R.raw.sd0, R.raw.sd1, R.raw.sd2, R.raw.sd3, R.raw.sd4};
    private int screen;

    public void SetMood(int screen){
        this.screen = screen;
    }

    public int GetImage(){
        return smiley[screen];
    }

    public int GetBkground() {
        return bkground[screen];
    }

    public int Getsound() {
        return sound[screen];
    }
}
