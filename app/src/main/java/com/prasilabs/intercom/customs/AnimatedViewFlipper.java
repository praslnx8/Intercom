package com.prasilabs.intercom.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.prasilabs.intercom.R;


/**
 * Created by prasi on 26/2/16.
 */
public class AnimatedViewFlipper extends ViewFlipper
{
    private boolean isAnimation = false;

    public AnimatedViewFlipper(Context context) {
        super(context);
    }

    public AnimatedViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIsAnimation(boolean isAnimation) {
        this.isAnimation = isAnimation;
    }

    @Override
    public void showNext()
    {
        if(isAnimation)
        {
            setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_in_left));
            setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_out_left));
        }

        super.showNext();
    }

    @Override
    public void showPrevious()
    {
        if(isAnimation)
        {
            setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_in_right));
            setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_slide_out_right));
        }

        super.showPrevious();
    }
}
