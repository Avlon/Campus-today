package com.example.zhao.top_school.View;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

/**
 * Created by zhao on 2016/3/24.
 */
public class NaviHorizontalScrollView extends HorizontalScrollView {
    private View ll_content, rl_column;
    private ImageView left_view,right_view;
    private int ScreenWitdh;
    public Activity factivity;
    public NaviHorizontalScrollView(Context context) {
        super(context);
    }
    public NaviHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NaviHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
    }
    //拖动时执行下列重写代码
    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4){
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        show_vlock();
        if(!factivity.isFinishing() && ll_content !=null && left_view!=null && right_view!=null && rl_column !=null){
            if(ll_content.getWidth() <= ScreenWitdh){
                left_view.setVisibility(View.GONE);
                right_view.setVisibility(View.GONE);
            }
        }else{
            return;
        }
        if(paramInt1 ==0){
            left_view.setVisibility(View.GONE);
            right_view.setVisibility(View.VISIBLE);
            return;
        }
        if(ll_content.getWidth()-paramInt1+rl_column.getLeft()==ScreenWitdh){
            right_view.setVisibility(View.GONE);
            left_view.setVisibility(View.VISIBLE);
            return ;
        }
        left_view.setVisibility(View.VISIBLE);
        right_view.setVisibility(View.VISIBLE);

    }
    public void setParam(Activity ac,int SW,View lc,View rl,ImageView lv,ImageView rv){
        this.factivity=ac;
        this.ScreenWitdh=SW;
        ll_content=lc;
        rl_column=rl;
        left_view=lv;
        right_view=rv;
    }
    public void show_vlock(){
        if (!factivity.isFinishing() && ll_content != null) {
            measure(0, 0);
            //如果整体宽度小于屏幕宽度的话，那左右阴影都隐藏
            if (ScreenWitdh >= getMeasuredWidth()) {
                left_view.setVisibility(View.GONE);
                right_view.setVisibility(View.GONE);
            }
        } else {
            return;
        }
        //如果滑动在最左边时候，左边阴影隐藏，右边显示
        if (getLeft() == 0) {
            left_view.setVisibility(View.GONE);
            right_view.setVisibility(View.VISIBLE);
            return;
        }
        //如果滑动在最右边时候，左边阴影显示，右边隐藏
        if (getRight() == getMeasuredWidth() - ScreenWitdh) {
            left_view.setVisibility(View.VISIBLE);
            right_view.setVisibility(View.GONE);
            return;
        }
        //否则，说明在中间位置，左、右阴影都显示
        left_view.setVisibility(View.VISIBLE);
        right_view.setVisibility(View.VISIBLE);
    }
}
