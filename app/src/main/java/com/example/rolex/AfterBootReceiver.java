package com.example.rolex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AfterBootReceiver extends BroadcastReceiver {

    PeriodRecalculateReceiver periodRecalculateReceiver = new PeriodRecalculateReceiver();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            periodRecalculateReceiver.setPredictionService(context);
        }

    }
}
