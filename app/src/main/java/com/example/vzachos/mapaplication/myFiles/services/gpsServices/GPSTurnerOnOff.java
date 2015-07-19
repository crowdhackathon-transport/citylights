package com.example.vzachos.mapaplication.myFiles.services.gpsServices;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by v.zachos on 7/16/2015.
 */
public class GPSTurnerOnOff {


    public void turnGPSOn(Context ctx)
    {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);

        //FATAL EXCEPTION: main
        //Process: com.example.vzachos.mapaplication, PID: 926
        //java.lang.SecurityException: Permission Denial: not allowed to send broadcast android.location.GPS_ENABLED_CHANGE from pid=926, uid=10119

        //ctx.sendBroadcast(intent);

        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            ctx.sendBroadcast(poke);


        }
    }
    // automatic turn off the gps
    public void turnGPSOff(Context ctx)
    {
        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            ctx.sendBroadcast(poke);
        }
    }
}
