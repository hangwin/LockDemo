package com.hang.study.lockdemo.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hang.study.lockdemo.activity.Lock_Activity;

import java.util.List;

/**
 * Created by hang on 16/8/4.
 */
public class WatchDogService extends Service {
    public boolean tag = true;
    public String result = null;
    public MyReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("看门狗服务已开启");
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("com.hang.stopWatchdog");
        registerReceiver(receiver, filter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tag) {
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                    String packageName = cn.getPackageName();
                    String runningAppName = getRunningAppName();
                    System.out.println("runningApp======>" + packageName);
                    if (!TextUtils.isEmpty(packageName)) {
                        System.out.println("runningApp======>" + runningAppName);
                        if (packageName.equals(getPackageName())) {
                            if ("stop".equals(result)) {

                            } else {
                                System.out.println("+++++++++" + packageName + "+++++++++");
                                Intent intent = new Intent(getApplicationContext(), Lock_Activity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getRunningAppName() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(USAGE_STATS_SERVICE);
        long ts = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000 * 3600, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return null;
        }

        UsageStats recentStats = null;
        for (UsageStats usageStats : queryUsageStats) {
            if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                recentStats = usageStats;
            }
        }
        return recentStats.getPackageName();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("服务关闭");
        if (receiver != null)
            unregisterReceiver(receiver);
        tag = false;
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            result = intent.getStringExtra("result");
        }
    }
}
