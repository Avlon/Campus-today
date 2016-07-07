package com.example.zhao.top_school.internet;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhao on 2016/6/6.
 */
public class HttpRefreshThread extends Thread{
    private HashMap<String,HashMap<String,Integer>> refresh;
    private HashMap<String,Integer> map;
    private ArrayList<String> interest_list;
    private String result,traget,inline,outline;
    private JSONObject josn_str,json_refresh=new JSONObject();
    private int code;
    public Handler handler;
    private String token,action,userId,department;
    @Override
    public void run(){
        result=null;
        HashMapToJson();
        HttpPost();
        try {
            if (Integer.toString(code).equals("201")) {
                Message msg = handler.obtainMessage();
                msg.what =Integer.parseInt(action);
                msg.obj = result;
                handler.sendMessage(msg);
            } else {
                Message msg = handler.obtainMessage();
                msg.what = -1;
                handler.sendMessage(msg);
            }
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    private void HashMapToJson(){
        try {
            JSONObject object = new JSONObject();
            for (int i = 0; i < interest_list.size(); i++) {
                String name = interest_list.get(i);
                object.put(name, map.get(name));
            }
            json_refresh.put(department,object);
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    public void setDate(String token,String action ,String userId,HashMap<String,Integer> refresh,String department,ArrayList<String> interest_list){
        this.token=token;
        this.action=action;
        this.userId=userId;
        this.department=department;
        this.interest_list=interest_list;
        map=refresh;
        this.refresh=new HashMap<String,HashMap<String,Integer>>();
        this.refresh.put(this.department,map);
    }
    private void HttpPost(){
        traget="http://192.168.3.19:8000/api/v1/news/";
        URL traget_url;
        josn_str=new JSONObject();
        try{
            josn_str.put("token",token);
            josn_str.put("userId",userId);
            if(action.equals("0")){
                josn_str.put("action","1");
            }
            else {
                josn_str.put("action", action);
            }
            josn_str.put("depart",json_refresh);
            Log.e("1",json_refresh.toString());
            Log.e("1",refresh.toString());
            outline=josn_str.toString();
            traget_url=new URL(traget);
            HttpURLConnection urlcon=(HttpURLConnection)traget_url.openConnection();
            urlcon.setRequestMethod("POST");
            urlcon.setDoInput(true);
            urlcon.setDoOutput(true);
            urlcon.setUseCaches(false);
            urlcon.setInstanceFollowRedirects(true);
            urlcon.setRequestProperty("Content-Type", "application/json");
            urlcon.connect();
            OutputStreamWriter out=new OutputStreamWriter(urlcon.getOutputStream());
            Log.e("1", outline.toString());
            out.write(outline,0,outline.length());
            out.flush();
            code=urlcon.getResponseCode();
            Log.e("1",Integer.toString(code));
            out.close();
            InputStreamReader in=new InputStreamReader(urlcon.getInputStream());
            BufferedReader read=new BufferedReader(in);
            inline=null;
            while((inline=read.readLine())!=null){
                result+=inline+"\n";
            }
            Log.e("1", result);
            result=result.subSequence(4,result.length()).toString();
            in.close();
            urlcon.disconnect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (JSONException e2){
            e2.printStackTrace();
        }
    }
}
