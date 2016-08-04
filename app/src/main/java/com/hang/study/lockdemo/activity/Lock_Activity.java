package com.hang.study.lockdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hang.study.lockdemo.R;
import com.hang.study.lockdemo.util.SPUtil;

/**
 * Created by hang on 16/8/4.
 */
public class Lock_Activity extends Activity implements View.OnClickListener{
    public EditText input;
    public Button ok;
    public Button cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_layout);
        input= (EditText) findViewById(R.id.input_psd);
        ok= (Button) findViewById(R.id.ok);
        cancle= (Button) findViewById(R.id.cancle);
        ok.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             case R.id.ok:
                 String psd=input.getText().toString();
                 if(!TextUtils.isEmpty(psd)) {
                     if(psd.equals(SPUtil.getString(this,"password"))) {
                        /* Intent intent = new Intent();
                         intent.setAction("com.hang.stopWatchdog");
                         intent.putExtra("result","stop");
                         sendBroadcast(intent);
                         finish();
                         */
                         finish();
                     }else {
                         Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
                     }
                 }else {
                     Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                 }
                 break;
             case R.id.cancle:
                 Intent intent=new Intent();
                 intent.setAction("android.intent.action.MAIN");
                 intent.addCategory("android.intent.category.HOME");
                 intent.addCategory("android.intent.category.DEFAULT");
                 intent.addCategory("android.intent.category.MONKEY");
                 startActivity(intent);
                 break;
         }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }
}
