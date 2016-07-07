package com.example.zhao.top_school.internet;

/**
 * Created by zhao on 2016/3/21.
 */
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.zhao.top_school.Activity.MainActivity;
public class HttpPostThread extends Thread{
    public Handler son_handler;
    private String result,traget,inline,outline,token,userId,action,depart;
    private JSONObject josn_str;
    private int code;
    public void GetMainHandler(MainActivity.TestHandler a){
        son_handler=a;
    }
    @Override
    public void run(){
        try{
            result=null;
            HttpPost();
            Message m=son_handler.obtainMessage();
            m.obj=result;
            son_handler.sendMessage(m);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }
    public void HttpPost(){
        traget="http://192.168.1.127:8000/api/v1/news/";
        URL traget_url;
        josn_str=new JSONObject();
        try{
            josn_str.put("depart",depart);
            josn_str.put("action",action);
            josn_str.put("token",token);
            josn_str.put("userId",userId);
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
            Log.e("1",outline.toString());
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
            Log.e("1",result);
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
