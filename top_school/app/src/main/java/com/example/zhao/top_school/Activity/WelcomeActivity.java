package com.example.zhao.top_school.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.zhao.top_school.R;

/**
 * Created by zhao on 2016/6/12.
 */
public class WelcomeActivity extends AppCompatActivity implements Runnable{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Thread(this).start();

    }
    @Override
    public void run(){
        try{
            Thread.sleep(2000);
            Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Log.e("1", e.toString());
        }
    }

}
