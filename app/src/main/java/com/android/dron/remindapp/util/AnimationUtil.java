package com.android.dron.remindapp.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Andrey on 02.12.2016.
 */

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 280 : -280, 0);
        animatorTranslateY.setDuration(900);
//        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -50, 50, -30, 30, -20, 20, -5, 5, 0);
//        animatorTranslateX.setDuration(500);
//        animatorSet.playTogether(animatorTranslateX, animatorTranslateY);
        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();
    }
}
