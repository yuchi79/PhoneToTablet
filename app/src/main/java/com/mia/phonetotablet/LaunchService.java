package com.mia.phonetotablet;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015-05-06.
 */
public class LaunchService extends Service {

    private int callingCount;

    // 서비스 생성시 1번만 실행
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getBaseContext(), "Service is Created", Toast.LENGTH_LONG).show();
    }

    // 서비스가 호출될때마다 매번 실행(onResume()과 비슷)
    public int onStartCommand(Intent intent, int flags, int startId) {

        DetectLogsThread thread = new DetectLogsThread(getBaseContext());
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    // 서비스가 종료될때 실행
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "Service is Destroied", Toast.LENGTH_LONG).show();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    class DetectLogsThread extends Thread {
        ActivityManager am = null;
        Context context = null;
        private String preAppName = null;

        public DetectLogsThread(Context con){
            context = con;
            am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }

        public void run(){
            Looper.prepare();
            while(true){
                ActivityManager manager = (ActivityManager)getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
                String currentRunningActivityName = tasks.get(0).processName;

                if (!currentRunningActivityName.equals(preAppName)) {
                    Log.v("PhoneToTablet", currentRunningActivityName);
                    writeLog(currentRunningActivityName);
                    preAppName = currentRunningActivityName;
                }

                if (currentRunningActivityName.equals("PACKAGE_NAME.ACTIVITY_NAME")) {
                    break;
                }
            }
            Looper.loop();
        }
    }

    private void writeLog(String log) {
//        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//        File dir = new File(sdPath,  "testing");
//
//        dir.mkdir();

        File file = new File(Environment.getExternalStorageDirectory(), "log.txt");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String fullLog = currentDateandTime + " : " + log;

        try {
//            FileOutputStream fos = new FileOutputStream(file);
//
//            fos.write(log.getBytes());
//
//            fos.close();
//

            BufferedWriter bW;
            bW = new BufferedWriter(new FileWriter(file, true));
            bW.write(fullLog);
            bW.newLine();
            bW.flush();
            bW.close();
        } catch (FileNotFoundException e) {
            Log.e("LaunchService", e.toString());
        } catch (IOException e) {
            Log.e("LaunchService", e.toString());
        }
    }
}
