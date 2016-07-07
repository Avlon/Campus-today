package com.example.zhao.top_school.internet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.zhao.top_school.Activity.RegisterActivity;

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
import java.util.Map;

/**
 * Created by zhao on 2016/3/28.
 */
public class HttpRegisterThread extends Thread{
    private Bundle mybundle;
    private HashMap<String,ArrayList<String>> maps;
    private String result,traget,inline,outline;
    private JSONObject josn_str,get_response;
    private int code;
    public Handler handler;
    private String user_id,pwa,school,tel_number,mac;
    @Override
    public void run(){
        result=null;
        mybundle=new Bundle();
        //Log.e("1","show");
        HttpPost();
        if(code==201){
            Message msg=handler.obtainMessage();
            msg.what=1;
            msg.obj=mybundle;
            handler.sendMessage(msg);
        }
        else {
            Message msg=handler.obtainMessage();
            msg.what=0;
            handler.sendMessage(msg);
        }
    }
    public void setDate(String a,String b, String c,String d,String e){
        this.user_id=a;
        this.pwa=b;
        this.school=c;
        this.tel_number=d;
        this.mac=e;
    }
    public void setHandler(RegisterActivity.MyHandler handler){
        this.handler=handler;
    }
    private void getDate(){
        try {
            get_response=new JSONObject(result);
            mybundle.putString("token",get_response.getString("token"));
            mybundle.putString("userId",get_response.getString("userId"));
            mybundle.putString("school",get_response.getString("school"));
            get_response.remove("school");
            get_response.remove("userId");
            get_response.remove("pwd");
            get_response.remove("tel");
            get_response.remove("token");
            mybundle.putString("depart_date",get_response.toString());
        }catch (Exception e) {
            Log.e("1", e.toString());
        }
    }
    private void HttpPost(){
        traget="http://192.168.3.19:8000/api/v1/newusers/";
        URL traget_url;
        josn_str=new JSONObject();
        try{
            josn_str.put("userId",user_id);
            josn_str.put("pwd",pwa);
            josn_str.put("tel",tel_number);
            josn_str.put("school",school);
            josn_str.put("email","794712861@qq.com");
            josn_str.put("mac",mac);
            outline=josn_str.toString();
            traget_url=new URL(traget);
            HttpURLConnection urlcon=(HttpURLConnection)traget_url.openConnection();
            urlcon.setRequestMethod("POST");
            urlcon.setDoInput(true);
            urlcon.setDoOutput(true);
            urlcon.setUseCaches(false);
            urlcon.setInstanceFollowRedirects(true);
            urlcon.setRequestProperty("Content-Type", "application/json");
            urlcon.setReadTimeout(3000);
            urlcon.setConnectTimeout(1500);
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
            result=result.subSequence(4,result.length()).toString();
            Log.e("1", result);
            in.close();
            urlcon.disconnect();
            getDate();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (JSONException e2){
            e2.printStackTrace();
        }
    }
}
