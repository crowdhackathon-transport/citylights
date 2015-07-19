package com.example.vzachos.mapaplication.myFiles.services.gpsServices;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by v.zachos on 7/16/2015.
 */
public class GPSAccess {


    public void getGPSAccess(Context ctx,GoogleMap map)
    {
        GPSTracker gps = new GPSTracker(ctx);

        //if GPS is open
        if (gps.getLatitude()!= 0.0 && gps.getLongitude() != 0.0)
        {
            double lat = gps.getLatitude();
            double lng = gps.getLongitude();
            LatLng loc = new LatLng(lat,lng);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc)      // Sets the center of the map to Mountain View
                    .zoom(14)         // Sets the zoom
                            //.tilt(50)       // Sets the tilt of the camera to 30 degrees
                    .build();         // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else
        {

            Toast.makeText(ctx, "Please open your GPS", Toast.LENGTH_LONG).show();
            //turning GPS to enable
            GPSTurnerOnOff gpsTurn = new GPSTurnerOnOff();
            gpsTurn.turnGPSOn(ctx);
        }
    }

}
