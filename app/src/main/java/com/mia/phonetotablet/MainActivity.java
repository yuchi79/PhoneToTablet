package com.mia.phonetotablet;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mia.phonetotablet.config.ConfigSharedPrefs;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;

import java.util.HashMap;


public class MainActivity extends Activity {
    private String TAG = "PhoneToTablet";
    private String mMyUuid;
    private EditText requestUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvUuid = (TextView)findViewById(R.id.uuid);
        requestUuid = (EditText) findViewById(R.id.request_uuid);

        mMyUuid = ParseInstallation.getCurrentInstallation().getInstallationId();
        tvUuid.setText(mMyUuid);

        String yourUuid = ConfigSharedPrefs.getConfig(this).get(ConfigSharedPrefs.PREFS_KEY_YOUR_UUID, null);
        if (yourUuid == null)
            requestUuid.setHint(mMyUuid);
        else
            requestUuid.setHint(yourUuid);

        // 서비스 실행
        this.startService(new Intent(this.getBaseContext(), LaunchService.class));
    }

    public void copyMyUuid(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Copied Text", mMyUuid));
        Toast.makeText(getBaseContext(), "클립보드에 내 UUID가 복사되었습니다.", Toast.LENGTH_LONG).show();
    }

    public void onTest(View v) {
        String uuidString = requestUuid.getText().toString();
        if (uuidString.equals("")) {
            uuidString = requestUuid.getHint().toString();
        }

        ConfigSharedPrefs.getConfig(this).set(ConfigSharedPrefs.PREFS_KEY_YOUR_UUID, uuidString);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("uuid", uuidString);
        params.put("message", "test");
        Log.v(TAG, "pushMyMessage params is " + params);
        ParseCloud.callFunctionInBackground("Push", params, new FunctionCallback<String>() {
            public void done(String result, ParseException e) {
                if (result != null)
                    Log.v(TAG, result);
            }
        });

    }
}