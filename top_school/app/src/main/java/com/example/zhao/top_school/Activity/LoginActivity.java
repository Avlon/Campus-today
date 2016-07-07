package com.example.zhao.top_school.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao.top_school.R;
import com.example.zhao.top_school.internet.HttpLoginThread;

/**
 * Created by zhao on 2016/3/27.
 */
public class LoginActivity extends Activity{
    private Button login;
    private TextView register,forget_pwa;
    private EditText userId,pwa;
    private HttpLoginThread mythread;
    private Intent intent;
    public class  TestHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            Log.e("1", "get_date");
            if(msg.what==1){
                intent=new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle=(Bundle)msg.obj;
                bundle.putInt("way",1);
//                Bundle bundle=new Bundle();
//                bundle.putString("init_date",(String)msg.obj);
                Log.e("1", "login: " + (String) msg.obj);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            else{
                Log.e("1","失败");
            }
            super.handleMessage(msg);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        InitView();
    }
    public void InitView(){
        register=(TextView)this.findViewById(R.id.register);
        forget_pwa=(TextView)this.findViewById(R.id.forget_pwa);
        userId=(EditText)this.findViewById(R.id.user_id);
        pwa=(EditText)this.findViewById(R.id.pwa);
        login=(Button)this.findViewById(R.id.login_button);

        final TestHandler myhandler=new TestHandler();

        mythread=new HttpLoginThread();

        pwa.setTransformationMethod(PasswordTransformationMethod.getInstance());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forget_pwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userId.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
                }
                else if(pwa.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                }
                else{
                    /*mythread.setHandler(myhandler);
                    mythread.setDate(userId.getText().toString(), pwa.getText().toString());
                    mythread.start();*/
                    intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
