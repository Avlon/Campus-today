package com.example.zhao.top_school.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;



import com.example.zhao.top_school.R;
import com.example.zhao.top_school.View.MyTextView;
import com.example.zhao.top_school.internet.*;
import com.example.zhao.top_school.adapter.*;
import com.example.zhao.top_school.fragment.*;
import com.example.zhao.top_school.Date.*;
import com.example.zhao.top_school.Tool.*;
import com.example.zhao.top_school.View.NaviHorizontalScrollView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhao on 2016/3/20.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<String> department;
    private HashMap<String,ArrayList<String>> maps;
    private NavigationView navigationView;
    private android.support.v7.widget.Toolbar toolbar;
    private NaviHorizontalScrollView head_hori;
    private ViewPager main_page;
    private LinearLayout hor_lin;
    private RelativeLayout hor_title_layout;
    private ImageView left_show,right_show;
    private int mScreenWidth,mItemWidth,now_select_index=0;
    private TestHandler myHandler;
    public ArrayList<Fragment> page_fragment_list;
    public ArrayList<HorItemClass> hor_list;
    public HttpReadThread test_thread;
    public HttpPostThread test_post_thread;
    public String getResult,token,userId;
    private Bundle bundle;
    public class  TestHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            //Log.e("1","get_date");
            getResult=(String)msg.obj;
            super.handleMessage(msg);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_set_activity);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        InitView();
    }
    private void InitFragment(){ //初始化fragment
        int cout=hor_list.size();
        try {
            for (int i = 0; i < cout; i++) {
                Bundle date = new Bundle();
                date.putString("text", hor_list.get(i).getTitle());
                //Log.e("text",date.getString("text"));
                PageFragment pageFragment = new PageFragment();//初始化推荐板块
                pageFragment.token=token;
                pageFragment.userId=userId;
                if(i==0)
                    pageFragment.setArguments(bundle);
                pageFragment.setDepartment(department.get(i));
                pageFragment.setInterest_list(maps.get(department.get(i)));
                page_fragment_list.add(pageFragment);
            }
            FragmentPageAdapter mfragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), page_fragment_list);
            main_page.setAdapter(mfragmentPageAdapter);
            main_page.setOnPageChangeListener(pageListener);
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    private void InitView(){
        mScreenWidth=getWindowsWidth(this);
        mItemWidth=mScreenWidth/5;

        hor_list=new ArrayList<HorItemClass>();
        page_fragment_list=new ArrayList<Fragment>();

        head_hori=(NaviHorizontalScrollView)this.findViewById(R.id.myHorView);
        main_page=(ViewPager)this.findViewById(R.id.pagerview);
        hor_lin=(LinearLayout)this.findViewById(R.id.hor_lin);
        hor_title_layout=(RelativeLayout)this.findViewById(R.id.hor_title_layout);
        left_show=(ImageView)this.findViewById(R.id.hor_left_view);
        right_show=(ImageView)this.findViewById(R.id.hor_right_view);
        toolbar=(Toolbar)this.findViewById(R.id.toolbar);
        navigationView=(NavigationView)this.findViewById(R.id.navigation_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.head_left_but);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("1", "nav_start");
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.change_date:
                        Intent nav_intent=new Intent(MainActivity.this,PersonalActivity.class);
                        startActivity(nav_intent);
                        return true;
                    case R.id.set_interest:
                        break;
                    case R.id.set_other:
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        InitDate();
        InitTabHor();
        InitFragment();
    }
    private void InitDate(){ //获得导航栏条目
        if(bundle.getInt("way")==1){//from login
            token=bundle.getString("token");
            userId=bundle.getString("userId");
            hor_list=new test_get_date().getDate();
        }
        else if(bundle.getInt("way")==2){//from setup
//            token=bundle.getString("token");
//            userId=bundle.getString("userId");
            Log.e("1","way 2");
            try {
                token=bundle.getString("token");
                userId=bundle.getString("userId");
                SetUpDateClass setUpDateClass=bundle.getParcelable("setUpDate");
                department=setUpDateClass.getDepartment();
                maps=setUpDateClass.getMaps();
                Log.e("1",department.toString());
            }catch (Exception e){
                Log.e("1",e.toString());
            }
            for(int i=0;i<department.size();i++){
                HorItemClass horItemClass=new HorItemClass();
                horItemClass.setId(i);
                horItemClass.setTitle(department.get(i));
                hor_list.add(horItemClass);
            }
        }
    }
    private void InitTabHor(){ //初始化滑动导航栏
        hor_lin.removeAllViews();
        int count=hor_list.size();
        head_hori.setParam(this, mScreenWidth, hor_lin, hor_title_layout, left_show, right_show);
        for(int i=0;i<count;i++){                               //初始化滑动栏栏目
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth , LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            MyTextView localTextView = new MyTextView(this);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(5, 0, 5, 0);
            localTextView.setId(i);
            localTextView.setText(hor_list.get(i).getTitle());
            //Log.e("1", hor_list.get(i).getTitle());
            localTextView.setTextColor(Color.rgb(255, 251, 240));
            localTextView.setSelected(false);
            if(now_select_index == i){
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i <hor_lin.getChildCount(); i++) {
                        View localView = hor_lin.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            main_page.setCurrentItem(i);
                        }
                    }
                }
            });
           hor_lin.addView(localTextView, i, params);
        }
    }
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    /**
     *  ViewPager切换监听方法
     * */
    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            try {
                main_page.setCurrentItem(position);
                selectTab(position);
            }catch (Exception e){
                Log.e("1",e.toString());
            }
        }
    };
    public void selectTab(int tab_position){  //导航点击滑动
        try {
            for (int i = 0; i < hor_lin.getChildCount(); i++) {
                View chile_view = hor_lin.getChildAt(tab_position);
                int k = chile_view.getMeasuredWidth();
                int l = chile_view.getLeft();
                int i2 = l + k / 2 - mScreenWidth / 2;
                head_hori.smoothScrollTo(i2, 0);
            }
            //Log.e("1","tab_position"+Integer.toString(tab_position));
            //Log.e("1","now_index"+Integer.toString(now_select_index));
            hor_lin.getChildAt(tab_position).setSelected(true);
            hor_lin.getChildAt(now_select_index).setSelected(false);
            now_select_index=tab_position;
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //侧滑监听器
        Intent nav_intent;
        Log.e("1","on_click");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e("id",Integer.toString(id));
        if(id==R.id.change_date){
            Log.e("1","come");
            nav_intent=new Intent(MainActivity.this,PersonalActivity.class);
            startActivity(nav_intent);
        }
        else if(id==R.id.my_collect){

        }
        else if(id==R.id.set_interest){

        }
        else if(id==R.id.set_message){

        }
        else if(id==R.id.set_other){

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
