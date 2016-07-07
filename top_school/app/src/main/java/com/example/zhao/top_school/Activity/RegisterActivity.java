package com.example.zhao.top_school.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao.top_school.R;
import com.example.zhao.top_school.internet.HttpRegisterThread;

import org.w3c.dom.Text;

/**
 * Created by zhao on 2016/3/28.//基本写完
 */
public class RegisterActivity extends Activity{
    private Intent intent;
    private TextView tel_where;
    private EditText user_id,pwa,tel_number,school;
    private Button register;
    private HttpRegisterThread mythread;
    private String[] telecom,unicom,mobile;
    private String mac;
    public class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                intent=new Intent(RegisterActivity.this,SetUpActivity.class);
                Bundle bundle=(Bundle)msg.obj;
                bundle.putString("userId",user_id.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(RegisterActivity.this,"注册失败,请检查网络连接",Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initView();
    }
    public void initView(){
        user_id=(EditText)this.findViewById(R.id.uesrId);
        pwa=(EditText)this.findViewById(R.id.register_pwa);
        tel_number=(EditText)this.findViewById(R.id.phone_number);
        school=(EditText)this.findViewById(R.id.school);
        register=(Button)this.findViewById(R.id.register_button);
        tel_where=(TextView)this.findViewById(R.id.tel_where);

        final MyHandler myhandler=new MyHandler();
        mobile=getResources().getStringArray(R.array.移动);
        unicom=getResources().getStringArray(R.array.联通);
        telecom=getResources().getStringArray(R.array.电信);

        tel_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId()==tel_number.getId()&&(!hasFocus)){
                    String a=tel_number.getText().toString();
                    if(a.length()==11) {
                        a = a.substring(0, 3);
                        for (int i = 0; i < mobile.length; i++) {
                            if (a.equals(mobile[i])) {
                                tel_where.setText("移动");
                                return ;
                            }
                        }
                        for (int i = 0; i < unicom.length; i++) {
                            if (a.equals(unicom[i])) {
                                tel_where.setText("联通");
                                return ;
                            }
                        }
                        for (int i = 0; i < telecom.length; i++) {
                            if (a.equals(telecom[i])) {
                                tel_where.setText("电信");
                                return ;
                            }
                        }
                    }
                    tel_where.setText("");
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               if(checkID()&&checkPW()&&checkTEL()){
//                   return;
//               }
                TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                mac=TelephonyMgr.getDeviceId();
                mythread = new HttpRegisterThread();
                mythread.setDate(user_id.getText().toString(), pwa.getText().toString(), school.getText().toString(), tel_number.getText().toString(),mac);
                mythread.setHandler(myhandler);
                mythread.start();
            }
        });
    }
    private boolean checkID(){
        String a=user_id.getText().toString();
        if(a.length()<3||a.length()>10){
            Toast.makeText(RegisterActivity.this,"用户名长度必须大于2小于10",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean checkPW(){
        String a=pwa.getText().toString();
        if(a.length()<5){
            Toast.makeText(RegisterActivity.this,"密码长度必须大于5",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean checkTEL(){
        String a=tel_number.getText().toString();
        if(a.length()==11){
            return true;
        }
        Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
        return false;
    }
}
