package com.example.zhao.top_school.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao.top_school.Date.HorItemClass;
import com.example.zhao.top_school.Date.InterestItemClass;
import com.example.zhao.top_school.Date.SetUpDateClass;
import com.example.zhao.top_school.R;
import com.example.zhao.top_school.Tool.test_get_date;
import com.example.zhao.top_school.adapter.InterestListViewAdapter;
import com.example.zhao.top_school.internet.HttpSetUpThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by zhao on 2016/3/28  //基本写完
 */
public class SetUpActivity extends Activity{
    private SetUpDateClass setUpDateClass;
    private HttpSetUpThread httpSetUpThread;
    private HashMap<String,ArrayList<String>> maps;
    private InterestItemClass interestItemClass;
    private ArrayList<InterestItemClass> new_list;
    private ArrayList<String> depatment;
    private InterestListViewAdapter listViewAdapter;
    private ListView interest_list;
    private Intent intent;
    private Button my_sure,my_cancel;
    public String json_list,token,school,name,userId;
    private  Bundle bundle;
    //private int count;
    private JSONObject jsonObject;
    private Dialog dia;
    public class TestHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            Log.e("1", "get_date");
            if(msg.what==1){
                intent=new Intent(SetUpActivity.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("news", (String)msg.obj);
                setUpDateClass =new SetUpDateClass();
                setUpDateClass.setDepartment(depatment);
                Log.e("1", setUpDateClass.getDepartment().toString());
                Log.e("1",maps.toString());
                setUpDateClass.setMaps(maps);
                bundle.putParcelable("setUpDate", setUpDateClass);
                bundle.putString("userId",userId);
                bundle.putString("token",token);
                bundle.putInt("way",2);
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
        setContentView(R.layout.activity_setup);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        initDate();
        init_view();

    }
    private void initDate(){
        maps=new HashMap<>();
        new_list=new ArrayList<InterestItemClass>();

        interestItemClass=new InterestItemClass();
        json_list=bundle.getString("depart_date");
        userId=bundle.getString("userId");
        token=bundle.getString("token");
        school=bundle.getString("school");
        try {
            jsonObject = new JSONObject(json_list);
//            number=jsonObject.getString("0");
//            count=Integer.parseInt(number);
            for(int i=10;i<=jsonObject.length()/2+9;i++){
//                if(jsonObject.get(Integer.toString(i)+"1").toString().equals("[]")){
//                    continue;
//                }
                name=jsonObject.getString(Integer.toString(i));
                name=name.replace("南京邮电大学","");
               // Log.e("1","1"+name);
               // Log.e("1","2"+jsonObject.get(Integer.toString(i)+"1").toString());
                depatment=new ArrayList<String>();
                JSONArray jsonArray=(JSONArray)jsonObject.get(Integer.toString(i)+"1");
                for(int j=0;j<jsonArray.length();j++){
                   depatment.add(jsonArray.get(j).toString());
                }
                //depatment=(ArrayList<String>)jsonObject.get(Integer.toString(i)+"1");
               // Log.e("1", depatment.toString());
                interestItemClass.set_name((i - 10), name);
                interestItemClass.setBUT((i - 10) % 3, depatment);
                if((i-9)%3==0){
                    new_list.add(interestItemClass);
                    interestItemClass=new InterestItemClass();
                }
            }
        }catch (Exception e){
            Log.e("1",e.toString());
        }
    }
    private void init_view(){
//        depatment=new ArrayList<String>();
//        maps=new HashMap<>();
        final TestHandler myHandler=new TestHandler();
        my_sure=(Button)this.findViewById(R.id.sure_button);
        my_cancel=(Button)this.findViewById(R.id.cancel_button);

        interest_list=(ListView)this.findViewById(R.id.interest_list);

        //new_list=test_get_date.get_interest_list();

        dia = new Dialog(SetUpActivity.this, R.style.Dia_log_style);
        listViewAdapter=new InterestListViewAdapter(SetUpActivity.this,new_list,dia);

        interest_list.setAdapter(listViewAdapter);


        my_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        my_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depatment.clear();
                for(int i=0;i<new_list.size();i++){
                    InterestItemClass news=new_list.get(i);
                    Log.e("1",Integer.toString(i));
                    for(int j=1;j<=3;j++){
                        try {
                            if(news.checkBut(j)==1){
                                depatment.add(news.getButName(j));
                                maps.put(news.getButName(j), news.checkBut_list(j));
                            }
                        }catch (Exception e){
                            Log.e("1","i="+Integer.toString(i)+"  j="+Integer.toString(j)+e.toString());
                        }
                    }
                }
                Log.e("1","ok");
//                Message message=myHandler.obtainMessage();
//                message.what=1;
//                message.obj=test_get_date.getList().toString();
//                myHandler.sendMessage(message);
                httpSetUpThread=new HttpSetUpThread();
                httpSetUpThread.setHandler(myHandler);
                httpSetUpThread.setDate(token,depatment.toString(),maps.toString(),userId,school);
                httpSetUpThread.start();
            }
        });
    }
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
