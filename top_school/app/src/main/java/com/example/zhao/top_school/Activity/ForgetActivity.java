package com.example.zhao.top_school.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhao.top_school.R;

/**
 * Created by zhao on 2016/5/9.
 */
public class ForgetActivity extends AppCompatActivity{
    private Button get_ident_code,forget_sure,cancel_button;
    private EditText nubmer,ident_code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_activity);
        initView();
    }
    private void initView(){
        get_ident_code=(Button)this.findViewById(R.id.get_ident_code);
        forget_sure=(Button)this.findViewById(R.id.forget_sure);
        cancel_button=(Button)this.findViewById(R.id.cancel_button);

        nubmer=(EditText)this.findViewById(R.id.forget_number);
        ident_code=(EditText)this.findViewById(R.id.ident_code);

        get_ident_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        forget_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
