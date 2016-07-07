package com.example.zhao.top_school.internet;

/**
 * Created by zhao on 2016/3/20.
 */
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.zhao.top_school.Activity.MainActivity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpReadThread extends Thread{
    private String result,traget,inline;
    public Handler son_handler;
    public void GetMainHandler(Handler son){
        son_handler=son;
    }
    @Override
    public void run(){
        try {
            result = null;
           // sleep(1000);
            //HttpGet();
            Message m = son_handler.obtainMessage();
            m.what=1;
            m.obj = result;
            son_handler.sendMessage(m);
        }catch (RuntimeException e){
            e.printStackTrace();
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    public void HttpGet(){
        traget="http://192.168.1.127:8000/article";
        URL traget_url;
        try{
            traget_url=new URL(traget);
            HttpURLConnection urlCon=(HttpURLConnection)traget_url.openConnection();
            InputStreamReader in=new InputStreamReader(urlCon.getInputStream());
            BufferedReader buffer=new BufferedReader(in);
            inline=null;
            Log.e("1", "1");
            while((inline=buffer.readLine())!=null){
                result+=inline+"\n";
                //Log.e("1","2");
            }
            //Log.e("1",result);
            in.close();
            urlCon.disconnect();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
}
