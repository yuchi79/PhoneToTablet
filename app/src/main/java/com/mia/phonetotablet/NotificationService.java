package com.mia.phonetotablet;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.mia.phonetotablet.config.ConfigSharedPrefs;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2015-04-17.
 */
public class NotificationService extends AccessibilityService {

    private static final String TAG = "NotificationService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            final String sourcePackageName = (String)event.getPackageName();
            Parcelable parcelable = event.getParcelableData();

            if (parcelable instanceof Notification) {
                // Statusbar Notification

                //Notification notification = (Notification) parcelable;
                //Log.e(TAG, "Notification -> notification.tickerText :: " + notification.tickerText);
                List<CharSequence> messages = event.getText();
                if (messages.size() > 0) {
                    final String notificationMsg = (String) messages.get(0);
                    pushMyMessage(sourcePackageName, notificationMsg);
                    Log.v(TAG, "Captured notification message [" + notificationMsg + "] for source [" + sourcePackageName + "]");
                    Log.v(TAG, "Broadcasting for " + Constants.ACTION_CATCH_NOTIFICATION);
                    try {
                        Intent mIntent = new Intent(Constants.ACTION_CATCH_NOTIFICATION);
                        mIntent.putExtra(Constants.EXTRA_PACKAGE, sourcePackageName);
                        mIntent.putExtra(Constants.EXTRA_MESSAGE, notificationMsg);
                        NotificationService.this.getApplicationContext().sendBroadcast(mIntent);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                } else {
                    Log.e(TAG, "Notification Message is empty. Can not broadcast");
                }
            }
        } else {
            Log.v(TAG, "Got un-handled Event");
        }
//Code when the event is caught
    }

    private void pushMyMessage(String packagename, String message) {
        Log.v(TAG, "pushMyMessage");
        String appName = "";
        if (packagename.equals("com.wizardlab.wizmessage"))
            appName = "메시지";
        else if (packagename.equals("com.android.phone"))
            appName = "전화";
        else if (packagename.equals("com.kakao.talk"))
            appName = "카카오톡";
        else if (packagename.equals("org.telegram.messenger"))
            appName = "텔레그램";
        else
            return;
        Log.v(TAG, "pushMyMessage app name is " + appName);
        String tabletUuid = ConfigSharedPrefs.getConfig(this).get(ConfigSharedPrefs.PREFS_KEY_YOUR_UUID, null);
        if (tabletUuid == null || tabletUuid.length() == 0) {
            Toast.makeText(getBaseContext(), "메시지를 보낼 단말을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("uuid", tabletUuid);
        params.put("message", message);
        Log.v(TAG, "pushMyMessage params is " + params);
        ParseCloud.callFunctionInBackground("Push", params, new FunctionCallback<String>() {
            public void done(String result, ParseException e) {
                Log.v(TAG, result);
            }
        });
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub.
        Log.v(TAG, "onInterrupt");

    }

    @Override
    protected void onServiceConnected() {
        Log.v(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.feedbackType = 1;
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    public static final class Constants {

        public static final String EXTRA_MESSAGE = "extra_message";
        public static final String EXTRA_PACKAGE = "extra_package";
        public static final String ACTION_CATCH_TOAST = "com.mytest.accessibility.CATCH_TOAST";
        public static final String ACTION_CATCH_NOTIFICATION = "com.mytest.accessibility.CATCH_NOTIFICATION";
    }
}