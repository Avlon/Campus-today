package com.example.zhao.top_school.internet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.zhao.top_school.Activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhao on 2016/3/27.
 */
public class HttpLoginThread extends Thread{
    private Bundle bundle;
    private String result,traget,inline,outline;
    private JSONObject josn_str;
    private int code;
    public Handler handler;
    private String user_id,pwa;
    @Override
    public void run(){
        bundle=new Bundle();
        try {
            result = null;
            HttpPost();
            if(Integer.toString(code).equals("201")){
                josn_str=new JSONObject(result);
                bundle.putString("token",josn_str.getString("tolen"));
                bundle.putString("news",josn_str.getString("新闻"));
                bundle.putString("userId",user_id);
                Message msg=handler.obtainMessage();
                msg.what=1;
                msg.obj=bundle;
                handler.sendMessage(msg);
            }
            else {
                Message msg=handler.obtainMessage();
                msg.what=0;
                handler.sendMessage(msg);
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    public void setHandler(LoginActivity.TestHandler ar){
        handler=ar;
    }
    public void setDate(String a,String b){
        user_id=a;
        pwa=b;
        //school=c;
    }
    private void HttpPost(){
        traget="http://192.168.1.124:8000/api/v1/users/login/";
        URL traget_url;
        josn_str=new JSONObject();
        try{
            josn_str.put("userId",user_id);
            josn_str.put("pwd",pwa);
            //josn_str.put("school",school);
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
