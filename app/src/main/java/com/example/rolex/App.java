package com.example.rolex;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID="rolex";
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {



            NotificationChannel rolex = new NotificationChannel(CHANNEL_ID, "rolex", NotificationManager.IMPORTANCE_DEFAULT);
            rolex.setDescription("rolex");
            NotificationManager manager =  getSystemService(NotificationManager.class);
            manager.createNotificationChannel(rolex);

        }
    }
}
