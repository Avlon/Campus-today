package com.example.zhao.top_school.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhao.top_school.R;
import com.example.zhao.top_school.Tool.test_get_date;
import com.example.zhao.top_school.fragment.PageFragment;
import com.example.zhao.top_school.internet.HttpReadThread;
import com.example.zhao.top_school.internet.HttpRefreshThread;

/**
 * Created by zhao on 2016/5/18.
 */
public class PullToRefreshListView extends LinearLayout implements View.OnTouchListener{
    private PageFragment father_fragment;
    private HttpRefreshThread httpReadThread;
    private Handler handler;
    private int PULLSTATE,PUSHSTATE; //0——刷新完成||未刷新 1——下拉状态 2——释放立即刷新 3——正在刷新
    private int LASTPULLSTATE,LASTPUSHSTATE;
    private TextView header_text,footer_text;
    private ImageView arrow;
    private View header,footer;
    private ProgressBar header_bar,footer_bar;
    private ListView listView;
    private boolean able_pull_refresh=false,once_layout=false,able_foot_refresh=false;
    private String head_text,foot_text;
    private MarginLayoutParams header_layoutparams,footer_layoutparams;//header和footer的布局参数
    private int header_height,footer_height;
    private int pull_speed,touchSlop;
    private float yDown,yMove;
    public PullToRefreshListView(Context context,AttributeSet attrs){
        super(context, attrs);
        header= LayoutInflater.from(context).inflate(R.layout.pulltorefresh_header, null, true);
        arrow=(ImageView)header.findViewById(R.id.arrow);
        header_bar=(ProgressBar)header.findViewById(R.id.head_progress_bar);
        header_text=(TextView)header.findViewById(R.id.header_text);
        //footer=LayoutInflater.from(context).inflate(R.layout.pulltorefresh_footer,null,true);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOrientation(VERTICAL);
        addView(header,0);
        //addView(footer,2);
    }
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    public void setFather_fragment(PageFragment pageFragment){
        father_fragment=pageFragment;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed&&!once_layout){
            //footer=getChildAt(2);
            //addView(footer);
            //footer_text=(TextView)footer.findViewById(R.id.footer_text);
            //footer_bar=(ProgressBar)footer.findViewById(R.id.foot_progressbar);
            // footer_layoutparams.bottomMargin=footer_height;
            // footer_height=-footer.getHeight();
            // footer_layoutparams=(MarginLayoutParams)footer.getLayoutParams();
            header_height=-header.getHeight();
            header_layoutparams=(MarginLayoutParams)header.getLayoutParams();
            header_layoutparams.topMargin=header_height;
            listView=(ListView)getChildAt(1);
            listView.setOnTouchListener(this);
            once_layout=true;
        }
    }
    @Override
    public boolean onTouch(View v,MotionEvent event){
        checkAbleRefresh(event);
        if(able_pull_refresh){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    yDown = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    yMove = event.getRawY();
                    int distance = (int) (yMove - yDown);
                    if (distance < 0 && header_layoutparams.topMargin <= header_height) {
                        return false;
                    }
                    if (distance < touchSlop) {
                        return false;
                    }
                    if (PULLSTATE != 3) {
                        if (header_layoutparams.topMargin > 0) {
                            PULLSTATE = 2;
                        } else {
                            PULLSTATE = 1;
                        }
                        header_layoutparams.topMargin = (distance / 3) + header_height;
                        header.setLayoutParams(header_layoutparams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (PULLSTATE == 2) {
                        //调用下拉刷新任务
                        try {
                            hideHeader(PULLSTATE);
                            httpReadThread=new HttpRefreshThread();
                            httpReadThread.setDate(father_fragment.token,"1",father_fragment.userId,father_fragment.tsp,father_fragment.department,father_fragment.interest_list);
                            httpReadThread.setHandler(handler);
                            httpReadThread.start();
                            PULLSTATE=3;
                            updateHeader();
//                            Message message=handler.obtainMessage();//测试用
//                            message.what=1;
//                            message.obj= test_get_date.getList();
//                            handler.sendMessage(message);
                        }catch (Exception e){
                            Log.e("1",e.toString());
                        }
                    } else if (PULLSTATE == 1) {
                        hideHeader(PULLSTATE);
                        //调用隐藏下拉头
                    }
                    break;
            }
            if(PULLSTATE==2||PULLSTATE==1){
                updateHeader();
                LASTPULLSTATE=PULLSTATE;
                listView.setPressed(false);
                listView.setFocusable(false);
                listView.setFocusableInTouchMode(false);
                return true;
            }
        }
        if(able_foot_refresh){
            Log.e("1", "is_foot_able");
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    yDown=event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    yMove=event.getRawY();
                    int foot_distance=(int)(yMove-yDown);
                    Log.e("1","distance"+Integer.toString(foot_distance));
                    // Log.e("1","touchSlop"+Integer.toString(touchSlop));
                    if(foot_distance>0&&footer_layoutparams.bottomMargin<footer_height){
                        return false;
                    }
                    if(-foot_distance<touchSlop){
                        return false;
                    }
                    //Log.e("1","able");
                    if(PUSHSTATE!=3){
                        PUSHSTATE=2;
//                        if(footer_layoutparams.bottomMargin>0){
//                            PUSHSTATE=2;
//                        }else {
//                            PUSHSTATE=1;
//                        }
//                        footer_layoutparams.bottomMargin=((-foot_distance)/2)+footer_height;
//                        Log.e("1","foot height"+Integer.toString(footer_height));
//                        Log.e("1",Integer.toString(footer_layoutparams.bottomMargin));
//                        footer.setLayoutParams(footer_layoutparams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if(PUSHSTATE==2){
                        //调用上推刷新
                        Log.e("1","上拉");
                    }
                    else if(PUSHSTATE==1){
                        //调用隐藏footer
                    }
                    break;
            }
            if(PUSHSTATE==2||PUSHSTATE==1){
                updateFooter();
                LASTPUSHSTATE=PUSHSTATE;
                listView.setPressed(false);
                listView.setFocusable(false);
                listView.setFocusableInTouchMode(false);
                return true;
            }
        }
        return false;
    }
    private void checkAbleRefresh(MotionEvent event){
        View firstView=listView.getChildAt(0);
        View lastView=listView.getChildAt(listView.getChildCount()-1);
        if(firstView!=null){
            int firstVisiblePos=listView.getFirstVisiblePosition();
            if(firstVisiblePos==0&&firstView.getTop()==0){
                if(!able_pull_refresh){
                    yDown=event.getRawY();
                }
                able_pull_refresh=true;
            }
            else {
                if(header_height!=header_layoutparams.topMargin){
                    header_layoutparams.topMargin=header_height;
                    header.setLayoutParams(header_layoutparams);
                }
                able_pull_refresh = false;
            }
        }
        //Log.e("1","check_foot");
        if(lastView!=null){
            int lastVisiblePos=listView.getLastVisiblePosition();
            //Log.e("1","lastVisiblepos"+Integer.toString(lastVisiblePos));
            //Log.e("1","childcount"+Integer.toString(listView.getChildCount()));
            // Log.e("1","bottom"+Integer.toString(lastView.getBottom()));
            if(lastVisiblePos==listView.getCount()-1){//&&lastView.getBottom()==0
                if(!able_foot_refresh){
                    yDown=event.getRawY();
                }
                able_foot_refresh=true;
            }
            else{
              /*  if(footer_height!=footer_layoutparams.bottomMargin){
                    footer_layoutparams.bottomMargin=footer_height;
                    footer.setLayoutParams(footer_layoutparams);
                }*/
                able_foot_refresh=false;
            }
        }
        if(firstView==null&&lastView==null){
            able_pull_refresh=true;
            able_foot_refresh=true;
        }
    }
    private void hideHeader(int state){//2-回到刷新中的状态 1-完全隐藏
        if(state==1) {
            for (; header_layoutparams.topMargin >= header_height; header_layoutparams.topMargin-=20) {
                header.setLayoutParams(header_layoutparams);
                for (int i = 0; i < 10000; i++) {
                    for(int j=0;j<100;j++);
                }
            }
        }
        if(state==2){
            for(;header_layoutparams.topMargin>=0;header_layoutparams.topMargin-=20){
                header.setLayoutParams(header_layoutparams);
                for(int i=0;i<10000;i++){
                    for(int j=0;j<100;j++);
                }
            }
        }
    }
    public void finishRefresh(){
        hideHeader(1);
        PULLSTATE=0;
    }
    private void rotateArrow(){
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (PULLSTATE == 0) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (PULLSTATE ==2) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }
    private void updateHeader(){
        if(LASTPULLSTATE!=PULLSTATE){
            if(PULLSTATE==2){
                header_text.setText(getResources().getString(R.string.refresh_up_to_load));
                arrow.setVisibility(View.VISIBLE);
                header_bar.setVisibility(View.GONE);
                rotateArrow();
            }
            else if(PULLSTATE==1){
                header_text.setText(getResources().getString(R.string.refresh_pull_to_load));
                arrow.setVisibility(View.VISIBLE);
                header_bar.setVisibility(View.GONE);
                rotateArrow();
            }
            else if(PULLSTATE==3){
                header_text.setText(getResources().getString(R.string.refresh_load));
                header_bar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }
        }
    }
    private void updateFooter(){
        if(LASTPUSHSTATE!=PUSHSTATE){
            if(PUSHSTATE==3){
//                footer_text.setText(getResources().getString(R.string.refresh_load));
//                footer_bar.setVisibility(VISIBLE);
            }
            if(PUSHSTATE==2){
                
            }
        }
    }
}
