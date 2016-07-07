package com.example.zhao.top_school.internet;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.zhao.top_school.Activity.SetUpActivity;

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
 * Created by zhao on 2016/4/10.
 */
public class HttpSetUpThread extends Thread{
    private String result,traget,inline,outline,school;
    private JSONObject josn_str;
    private String token,departments,sections,userId;
    private int code;
    public Handler handler;
    @Override
    public void run(){
        result=null;
        HttpPost();
        if(code==201){
            Message msg=handler.obtainMessage();
            msg.what=1;
            msg.obj=result;
            handler.sendMessage(msg);
        }
        else {
            Message msg=handler.obtainMessage();
            msg.what=0;
            handler.sendMessage(msg);
        }
    }
    public void HttpPost(){
        traget="http://192.168.3.19:8000/api/v1/concerns/newconcerns/";
        URL traget_url;
        josn_str=new JSONObject();
        try{
            josn_str.put("userId",userId);
            josn_str.put("token",token);
            josn_str.put("departments",departments);
            josn_str.put("sections",sections);
            josn_str.put("school",school);
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
    public void setDate(String token,String departments,String sections,String userId,String school){
        this.token=token;
        this.departments=departments;
        this.sections=sections;
        this.userId=userId;
        this.school=school;
    }
    public void setHandler(SetUpActivity.TestHandler fatherHandler){
        this.handler=fatherHandler;
    }
}
