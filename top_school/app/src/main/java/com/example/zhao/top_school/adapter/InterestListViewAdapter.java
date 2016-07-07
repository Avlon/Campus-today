package com.example.zhao.top_school.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.zhao.top_school.Activity.SetUpActivity;
import com.example.zhao.top_school.Date.InterestItemClass;
import com.example.zhao.top_school.R;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/5/18.
 */
public class InterestListViewAdapter extends BaseAdapter{
    public ArrayList<InterestItemClass> new_list;
    public Activity activity;
    private LayoutInflater layoutInflater=null;
    private Dialog dialog;
    private InterestItemClass news;
    public InterestListViewAdapter(Activity activity,ArrayList<InterestItemClass> list,Dialog dialog){
        this.activity=activity;
        this.new_list=list;
        this.dialog=dialog;
        layoutInflater=LayoutInflater.from(activity);
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return new_list == null ? 0 : new_list.size();
    }
    @Override
    public InterestItemClass getItem(int position) {
        // TODO Auto-generated method stub
        if (new_list != null && new_list.size() != 0) {
            return new_list.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item_view=convertView;
        final Interest_Item item;
        if(item_view==null){
            item_view=layoutInflater.inflate(R.layout.interest_list_item,null);
            item=new Interest_Item();
            item.one=(Button)item_view.findViewById(R.id.one);
            item.two=(Button)item_view.findViewById(R.id.two);
            item.three=(Button)item_view.findViewById(R.id.three);
            item_view.setTag(item);
        }
        else{
            item=(Interest_Item)item_view.getTag();
        }
        news=getItem(position);
        try {
            initButton(item.one, 1, news);
        }catch (Exception e){
            Log.e("1","1"+e.toString());
        }
        try {
            initButton(item.two, 2, news);
        }catch (Exception e){
            Log.e("1","2"+e.toString());
        }

        try {
            initButton(item.three, 3, news);
        }catch (Exception e){
            Log.e("1",e.toString()+"3");
        }
        return item_view;
    }
    static class Interest_Item{
        Button one;
        Button two;
        Button three;
    }
    private void showDia(final InterestItemClass news,final int num,final Button button){
        ArrayList<String> but_list=news.getButList(num);
        dialog.setContentView(R.layout.setup_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        w.setGravity(Gravity.CENTER);
        lp.x = 0;
        lp.y = -160;
        lp.width = 650;
        lp.height = 1050;
        dialog.onWindowAttributesChanged(lp);
        Button back_but = (Button) dialog.findViewById(R.id.dialog_back);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.checkBut(num)==0){
                    button.setTextColor(activity.getResources().getColor(R.color.colorSetUpText));
                    button.setTag("false");
                }
                dialog.dismiss();
            }
        });
        initDiaButton((Button) dialog.findViewById(R.id.dialog_1), but_list.get(0), news, num, 0);
        initDiaButton((Button)dialog.findViewById(R.id.dialog_2),but_list.get(1),news,num,1);
        initDiaButton((Button)dialog.findViewById(R.id.dialog_3),but_list.get(2),news,num,2);
        if(but_list.size()>3){
            initDiaButton((Button)dialog.findViewById(R.id.dialog_4),but_list.get(3),news,num,3);
        }
        else{
            initDiaButton((Button)dialog.findViewById(R.id.dialog_4),null,news,num,3);
        }
        if(but_list.size()>4) {
            initDiaButton((Button) dialog.findViewById(R.id.dialog_5), but_list.get(4), news, num, 4);
        }
        else{
            initDiaButton((Button) dialog.findViewById(R.id.dialog_5),null, news, num, 4);
        }
        if(but_list.size()>5) {
            initDiaButton((Button) dialog.findViewById(R.id.dialog_6), but_list.get(5), news, num, 5);
        }
        else{
            initDiaButton((Button) dialog.findViewById(R.id.dialog_6), null, news, num, 5);
        }
    }
    private void initDiaButton(final Button button,String but_name,InterestItemClass news,int num, final int this_num){
        final boolean[] state=news.getBool(num);
        if(but_name!=null) {
            button.setVisibility(View.VISIBLE);
            button.setText(but_name);
            if(state[this_num]){
                button.setTextColor(activity.getResources().getColor(R.color.colorSetUpPreText));
                button.setTag("true");
            }
            else{
                button.setTextColor(activity.getResources().getColor(R.color.colorSetUpText));
                button.setTag("false");
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(button.getTag().toString().equals("false")){
                        button.setTextColor(activity.getResources().getColor(R.color.colorSetUpPreText));
                        button.setTag("true");
                        state[this_num]=true;
                    }
                    else{
                        button.setTag("false");
                        button.setTextColor(activity.getResources().getColor(R.color.colorSetUpText));
                        state[this_num]=false;
                    }
                }
            });
        }
        else{
            button.setVisibility(View.INVISIBLE);
        }
    }
    private void initButton(final Button button,final int num, final InterestItemClass news){
        String name=news.getButName(num);
        if(name!=null) {
            button.setVisibility(View.VISIBLE);
            button.setText(name);
            try {
                if (news.checkBut(num) == 0) {
                    button.setTextColor(activity.getResources().getColor(R.color.colorSetUpText));
                    button.setTag("false");
                }
                if (news.checkBut(num) == 1) {
                    button.setTextColor(activity.getResources().getColor(R.color.colorSetUpPreText));
                    button.setTag("true");
                }
            }catch (Exception e){
                Log.e("1",e.toString()+"GG");
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (button.getTag().toString().equals("false")) {
                        button.setTextColor(activity.getResources().getColor(R.color.colorSetUpPreText));
                        news.ClickBut(num,true);
                        button.setTag("true");
                        try {
                            showDia(news, num,button);
                        }catch (Exception e){
                            Log.e("1",e.toString());
                        }
                    } else {
                        if(news.checkBut(num)==0) {
                            button.setTag("false");
                            news.ClickBut(num,false);
                            button.setTextColor(activity.getResources().getColor(R.color.colorSetUpText));
                        }
                        else{
                            try {
                                showDia(news, num,button);
                            }catch (Exception e){
                                Log.e("1",e.toString());
                            }
                        }
                    }
                }
            });
        }
        else{
            button.setVisibility(View.INVISIBLE);
        }
    }
}
