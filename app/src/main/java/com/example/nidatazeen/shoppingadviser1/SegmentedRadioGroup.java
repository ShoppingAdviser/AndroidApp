package com.example.nidatazeen.shoppingadviser1;

/**
 * Created by Len on 07-03-2016.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

public class SegmentedRadioGroup extends RadioGroup {

    public SegmentedRadioGroup(Context context) {
        super(context);
    }

    public SegmentedRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        changeButtonsImages();
    }

    private void changeButtonsImages(){
        int count = super.getChildCount();

        if(count > 1)
        {
            super.getChildAt(0).setBackgroundResource
                    (R.drawable.segment_radio_left);

            super.getChildAt(1).setBackgroundResource
                    (R.drawable.segment_radio_middle);

            super.getChildAt(2).setBackgroundResource
                    (R.drawable.segment_radio_middle);

            super.getChildAt(3).setBackgroundResource
                    (R.drawable.segment_radio_right);
        }
    }
}


