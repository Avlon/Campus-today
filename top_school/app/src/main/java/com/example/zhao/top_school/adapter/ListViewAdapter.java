package com.example.zhao.top_school.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhao.top_school.Activity.MainActivity;
import com.example.zhao.top_school.Activity.WebActivity;
import com.example.zhao.top_school.Date.ListItemClass;
import com.example.zhao.top_school.R;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/3/27.
 */
public class ListViewAdapter extends BaseAdapter{
    public ArrayList<ListItemClass> new_list;
    Activity activity;
    LayoutInflater inflater = null;
    public ListViewAdapter(Activity activity,ArrayList<ListItemClass> a){
        this.activity=activity;
        this.new_list=a;
        inflater = LayoutInflater.from(activity);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return new_list == null ? 0 : new_list.size();
    }
    @Override
    public ListItemClass getItem(int position) {
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
        final Myltem item;
        if(item_view==null){
            item_view=inflater.inflate(R.layout.list_item,null);
            item=new Myltem();
            item.creat_time=(TextView) item_view.findViewById(R.id.creat_time);
            item.new_title = (TextView)item_view.findViewById(R.id.new_title);
            item.watch_times=(TextView)item_view.findViewById(R.id.watch_times);
            item_view.setTag(item);
        }
        else{
            item=(Myltem)item_view.getTag();
        }
        final ListItemClass news=getItem(position);
        item.new_title.setText(news.getNew_title());
        item.creat_time.setText(news.getCreat_time());
        item.watch_times.setText(news.getWatch_times());
        item.url=news.getNews_url();
     //   Log.e("1",item.url);
        item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,WebActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",news.getNews_url());
                intent.putExtras(bundle);
                activity.startActivity(intent);
                Log.e("1","ok");
            }
        });
        return item_view;
    }
    static class Myltem {
        TextView new_title;
        TextView creat_time;
        TextView watch_times;
        public String url;
    }
}
