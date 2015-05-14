package com.mia.phonetotablet;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Administrator on 2015-04-17.
 */
public class NotificationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "UNbWE8OzVp8Gxv6e0EMwCJZNJacJ284TQBjiD2k4", "mUPux9CqcgDlmMoYBi5gl6XQTuRkYBCpmH4YnZVO");

        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().saveInBackground();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Save the current Installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("Checker", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}
