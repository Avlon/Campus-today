package com.example.zhao.top_school.View;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.zhao.top_school.R;

/**
 * Created by zhao on 2016/3/25.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public MyTextView(Activity activity){
        super(activity);
    }
    @Override
    public void setSelected(boolean flag){
        if(flag){
            this.setBackgroundResource(R.drawable.chose_bacg);
        }
        else{
            this.setBackground(null);
        }
    }
}
