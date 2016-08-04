package com.hang.study.lockdemo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hang.study.lockdemo.R;
import com.hang.study.lockdemo.util.SPUtil;

public class MainActivity extends Activity implements View.OnClickListener {
    public Button toggleLock;

    public Button stopLock;
    private ScreenOff off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        off = new ScreenOff(); //接收锁屏的广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(off, filter);
        setContentView(R.layout.content_main);
        toggleLock = (Button) findViewById(R.id.toggleLock);
        stopLock = (Button) findViewById(R.id.stopLock);
        stopLock.setOnClickListener(this);
        toggleLock.setOnClickListener(this);
        if (SPUtil.getBoolean(this, "isLock")) {  //若有开启密码锁，则进入输入密码界面
            Intent intent = new Intent(this, Lock_Activity.class);
            startActivity(intent);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleLock:
                if (SPUtil.getString(this, "password") == null) {
                    showDialog();
                    if (SPUtil.getString(this, "password") != null) {
                        SPUtil.setBoolean(this, "isLock", true);
                        Toast.makeText(this, "密码锁已打开", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SPUtil.setBoolean(this, "isLock", true);
                    Toast.makeText(this, "密码锁已打开", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopLock:
                SPUtil.setBoolean(this, "isLock", false);
                Toast.makeText(this, "密码锁已关闭", Toast.LENGTH_SHORT).show();
                break;
        }
       /* Intent intent=new Intent(this, WatchDogService.class);
        switch (v.getId()) {
            case R.id.toggleLock:
                System.out.println("===>"+SPUtil.getString(this,"password"));
                if(ServiceUtil.isServiceAlive(this,"com.hang.study.lockdemo.service.WatchDogService")) {
                    Toast.makeText(this,"密码锁已打开",Toast.LENGTH_SHORT).show();
                }else if(SPUtil.getString(this,"password")==null){
                    showDialog();

                    if(SPUtil.getString(this,"password")!=null) {
                        startService(intent);
                    }
                }else {
                    startService(intent);
                }
                break;
            case R.id.stopLock:
                if(ServiceUtil.isServiceAlive(this,"com.hang.study.lockdemo.service.WatchDogService"))
                    stopService(intent);
                Toast.makeText(this,"密码锁已关闭",Toast.LENGTH_SHORT).show();
                break;
        }*/
    }

    EditText password, ensure_password;
    Button ok, cancle;
    public AlertDialog dialog = null;

    //用于设置密码
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        password = (EditText) view.findViewById(R.id.password);
        ensure_password = (EditText) view.findViewById(R.id.ensure_password);
        ok = (Button) view.findViewById(R.id.sure);
        cancle = (Button) view.findViewById(R.id.cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd1 = password.getText().toString();
                String psd2 = ensure_password.getText().toString();
                if (TextUtils.isEmpty(psd1) || TextUtils.isEmpty(psd2)) {
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!psd1.equals(psd2)) {
                    Toast.makeText(MainActivity.this, "两次的密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    SPUtil.setString(MainActivity.this, "password", psd1);
                    dialog.dismiss();
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setView(view);
        /*builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 String psd1=password.getText().toString();
                 String psd2=ensure_password.getText().toString();
                 if(TextUtils.isEmpty(psd1)||TextUtils.isEmpty(psd2)) {
                     Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!psd1.equals(psd2)) {
                     Toast.makeText(MainActivity.this,"两次的密码不一致",Toast.LENGTH_SHORT).show();
                 }else {
                     SPUtil.setString(MainActivity.this,"password",psd1);
                 }
            }
        });*/

        dialog = builder.show();
    }

    private class ScreenOff extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("锁屏了");
            if (SPUtil.getBoolean(MainActivity.this, "isLock")) {
                Intent i = new Intent(MainActivity.this, Lock_Activity.class);
                startActivity(i);
            }
        }
    }
}
