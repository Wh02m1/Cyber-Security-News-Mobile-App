package com.example.final_project_3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class InternetRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = CheckInternet.getNetworkinto(context);
        if (status.equals("connected")){
            Toast.makeText(context.getApplicationContext(),"welcome To Our App",Toast.LENGTH_LONG).show(); //test
        }else if(status.equals("disconnected")){
            Toast.makeText(context.getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }
}
