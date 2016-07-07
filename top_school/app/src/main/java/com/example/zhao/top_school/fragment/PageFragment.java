package com.example.zhao.top_school.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.zhao.top_school.Date.ListItemClass;
import com.example.zhao.top_school.R;
import com.example.zhao.top_school.Tool.test_get_date;
import com.example.zhao.top_school.View.PullToRefreshListView;
import com.example.zhao.top_school.adapter.ListViewAdapter;
import com.example.zhao.top_school.internet.HttpLoginThread;
import com.example.zhao.top_school.internet.HttpReadThread;
import com.example.zhao.top_school.internet.HttpRefreshThread;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhao on 2016/3/25.
 */
public class PageFragment extends Fragment {
    public String token,userId;
    public HashMap<String,Integer> tsp; //储存各部门新闻时序
    public String department;
    public ArrayList<String> interest_list;
    private PullToRefreshListView pullToRefreshListView;
    private Bundle args;
    public ListView my_listview;
    private Activity activity;
    private ListViewAdapter my_adapter;
    private ArrayList<ListItemClass> news_list=new ArrayList<ListItemClass>();
    public final static int SET_NEWSLIST = 0;
    public final static int ADD_NEWLIST=1;
    public final static int ADD_OLDLIST=2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        args=null;
        args = getArguments();
        //Log.e("1",args.getString("init_date"));
        initData();
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        super.onAttach(activity);
    }
    private void initData() {
        tsp=new HashMap<String,Integer>();
        if(args!=null) {
            Message message=handler.obtainMessage();
            message.what=SET_NEWSLIST;
            message.obj=args.getString("news");
            handler.sendMessage(message);
////        try{
////            JSONArray init_date = new JSONArray(args.getString("news"));
////            Log.e("1",Integer.toString(init_date.length()));
////            for(int i=0;i<init_date.length();i++){
////                JSONObject json_ob=(JSONObject)init_date.opt(i);
////                Log.e("1", json_ob.toString());
////                ListItemClass news=new ListItemClass();
////                news.setNew_title(json_ob.getString("title"));
////                news.setCreat_time("发布时间:"+json_ob.getString("date"));
////                news.setWatch_times("点击数：0");
////                news.setNews_url(json_ob.getString("url"));
////                //Log.e("1",news.getNews_url());
////                news_list.add(news);
////            }
//        }catch (JSONException e){
//            //Log.e("1","we_error");
//            e.printStackTrace();
//        }
        }
        for(int i=0;i<interest_list.size();i++){
            tsp.put(interest_list.get(i),new Integer(0));
        }
        //news_list = test_get_date.getList();
    }
    public void setInterest_list(ArrayList<String> interest_list){
        this.interest_list=interest_list;
    }
    public  void setDepartment(String department){
        this.department=department;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
            //添加子线程请求数据
            if(news_list !=null && news_list.size() !=0){
                handler.obtainMessage(SET_NEWSLIST).sendToTarget();//列表有值，可作为后续添加
            }else{
                Log.e("1", "无数据");
                HttpRefreshThread httpRefreshThread=new HttpRefreshThread();
                httpRefreshThread.setHandler(handler);
                httpRefreshThread.setDate(token, "0", userId, tsp, department, interest_list);
                httpRefreshThread.start();
                //handler.obtainMessage(SET_NEWSLIST).sendToTarget();
            }
        }else{
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.new_fragment,null);
        my_listview=(ListView)view.findViewById(R.id.my_listview);
        pullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_view);

        pullToRefreshListView.setHandler(handler);
        pullToRefreshListView.setFather_fragment(this);
        return view;
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case ADD_NEWLIST:
                    try {
                        pullToRefreshListView.finishRefresh();
//                        ArrayList<ListItemClass> arrayList=(ArrayList<ListItemClass>)msg.obj;
//                        for(int i=0;i<arrayList.size();i++){
//                            news_list.add(0,arrayList.get(i));
//                        }
                        JSONArray jsonArray=new JSONArray((String)msg.obj);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject json_ob=(JSONObject)jsonArray.opt(i);
                            Log.e("1", json_ob.toString());
                            ListItemClass news=new ListItemClass();
                            news.setNew_title(json_ob.getString("title"));
                            news.setCreat_time("发布时间:"+json_ob.getString("date"));
                            news.setWatch_times("点击数：0");
                            news.setNews_url(json_ob.getString("url"));
                            Log.e("1",news.getNews_url());
                            news_list.add(0,news);
                        }
                        my_adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Log.e("1",e.toString());
                    }
                    break;
                case SET_NEWSLIST:
                    pullToRefreshListView.finishRefresh();
                    try {
                        if(msg.obj!=null){
                            JSONArray jsonArray=new JSONArray((String)msg.obj);
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject json_ob = (JSONObject) jsonArray.opt(i);
                                Log.e("1", json_ob.toString());
                                ListItemClass news = new ListItemClass();
                                news.setNew_title(json_ob.getString("title"));
                                news.setCreat_time("发布时间:" + json_ob.getString("date"));
                                news.setWatch_times("点击数：0");
                                news.setNews_url(json_ob.getString("url"));
                                Log.e("1", news.getNews_url());
                                news_list.add(news);
                            }
                        }
                        my_adapter = new ListViewAdapter(activity, news_list);
                        my_listview.setAdapter(my_adapter);
                    }catch (Exception e){
                        Log.e("1",e.toString());
                    }
                    break;
                case ADD_OLDLIST:
                    pullToRefreshListView.finishRefresh();
                    break;
                default:
                    pullToRefreshListView.finishRefresh();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
