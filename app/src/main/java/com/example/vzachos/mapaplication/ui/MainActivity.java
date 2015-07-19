

package com.example.vzachos.mapaplication.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.vzachos.mapaplication.BootstrapServiceProvider;
import com.example.vzachos.mapaplication.R;
import com.example.vzachos.mapaplication.authenticator.ApiKeyProvider;
import com.example.vzachos.mapaplication.events.NavItemSelectedEvent;
import com.example.vzachos.mapaplication.myFiles.classes.ArvadaTrafficLights;
import com.example.vzachos.mapaplication.myFiles.classes.NottinTrafficLights;
import com.example.vzachos.mapaplication.myFiles.services.CsvFileReaderTown;
import com.example.vzachos.mapaplication.myFiles.services.IntenetAccess;
import com.example.vzachos.mapaplication.myFiles.services.directionServices.DirectionsJSONParser;
import com.example.vzachos.mapaplication.myFiles.services.gpsServices.GPSAccess;
import com.example.vzachos.mapaplication.myFiles.services.gpsServices.GPSTracker;
import com.example.vzachos.mapaplication.util.Ln;
import com.example.vzachos.mapaplication.util.UIUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Initial activity for the application.
 *
 * If you need to remove the authentication from the application please see
 * {@link ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    @Inject protected BootstrapServiceProvider serviceProvider;


    AssetManager assetManager;
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }


    GPSTracker gps;

    ArrayList<LatLng> markerPoints;
    Marker pointingMarker;
    Polyline pointingLine;
    GoogleMap mMap;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;
    private DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if(isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
      /*  Views.inject(this);*/

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if(!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        userHasAuthenticated = true;
        initScreen();

    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    protected SupportMapFragment supportMapFragment;

    private void initScreen() {

        supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        System.out.println("BEFOREASYNC : " + supportMapFragment);
        supportMapFragment.getMapAsync(this);

        markerPoints = new ArrayList<LatLng>();

    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mapReadyToChange(map);

        map.setMyLocationEnabled(true);
        map.setOnMapLongClickListener(this);


        Context ctx = getApplicationContext();
        IntenetAccess intAcc = new IntenetAccess();
        GPSAccess gpsAcc = new GPSAccess();

        if (intAcc.isNetworkAvailable(ctx))
            gpsAcc.getGPSAccess(ctx,mMap);
        else
            Toast.makeText(ctx,"Please turn on your Internet Access",Toast.LENGTH_LONG).show();


        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                    mapReadyToChange(mMap);
                }

                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = directionsJSONParser.getDirectionsUrl(origin, dest);

                    directionsJSONParser.drawUrlPAth(url);

                }
            }
        });
    }



    private void mapReadyToChange(GoogleMap map) {
        AssetManager as = getApplicationContext().getAssets();

        long start = System.currentTimeMillis();
        List<NottinTrafficLights> toInit = new ArrayList<NottinTrafficLights>();
        //List<ArvadaTrafficLights> toInitAll = new ArrayList<ArvadaTrafficLights>();
        try {

            //klisi tou nottingham to ekopsa prosorina gia ton diagonismo
            /*boolean flag = CsvFileReader.getInstance().loadCsv(toInit, as.open(CsvFileReader.CSV_FILE_NAME));
            if (flag)
                setDataToMap(toInit, map, as);
            else
                Toast.makeText(getApplicationContext(), "Could Not Load CSV", Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), ((double) System.currentTimeMillis() - start) / 1000 + " Seconds", Toast.LENGTH_LONG).show();
            */
            for (int i = 0; i < CsvFileReaderTown.CSV_FILE_NAMES.length; i++) {
                List<ArvadaTrafficLights> toInitAll = new ArrayList<ArvadaTrafficLights>();

               /* System.out.println("In file == : " + CsvFileReaderTown.CSV_FILE_NAMES[i]);
                boolean flag2 = CsvFileReaderTown.getInstance().loadCsv(toInitAll, as.open(CsvFileReaderTown.CSV_FILE_NAMES[i]));
                if (flag2)
                    setOtherDataToMap(toInitAll, map, as);
                else
                    Toast.makeText(getApplicationContext(), "Could Not Load Other CSV", Toast.LENGTH_LONG).show();
            }*/

                boolean flag2 = CsvFileReaderTown.getInstance().loadCsv(toInitAll, as.open(CsvFileReaderTown.CSV_FILE_NAMES[i]));
                if (flag2) {
                    //Toast.makeText(getApplicationContext(), i +" + " + toInitAll.size(), Toast.LENGTH_LONG).show();
                    setOtherDataToMap(toInitAll, map, as);
                }
                else
                    Toast.makeText(getApplicationContext(), "Could Not Load Other CSV", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setOtherDataToMap(List<ArvadaTrafficLights> list,GoogleMap map, AssetManager as) {

        //Toast.makeText(getApplicationContext(),""+list.size(),Toast.LENGTH_LONG).show();

        try {
            BitmapDescriptor bitDisc = BitmapDescriptorFactory.fromResource(R.mipmap.lights_image);

            for (ArvadaTrafficLights model : list) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(model.getLat(), model.getLng()))
                        .title("Lat " + model.getLat() + " Lng " + model.getLng())
                        .icon(bitDisc));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }



    private void setDataToMap(List<NottinTrafficLights> list,GoogleMap map, AssetManager as) {

        try {
            BitmapDescriptor bitDisc = BitmapDescriptorFactory.fromResource(R.mipmap.lights_image);

            for (NottinTrafficLights model : list) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(model.getLat(), model.getLng()))
                        .title(model.getName())
                        .icon(bitDisc));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onMapLongClick(LatLng point) {

        try {
            BitmapDescriptor bitDisc = BitmapDescriptorFactory.fromResource(R.mipmap.pin);

            if (pointingMarker == null) {

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(point.toString())
                        .icon(bitDisc));
                DrawLine(point);
                pointingMarker = marker;
            } else {
                pointingMarker.remove();
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(point.toString())
                        .icon(bitDisc));
                DrawLine(point);
                pointingMarker = marker;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       /* Toast.makeText(getApplicationContext(),
                "New marker added@" + point.toString(), Toast.LENGTH_LONG)
                .show();*/
    }



    public void DrawLine(LatLng toHere)
    {

        Context ctx = getApplicationContext();
        GPSTracker gpsTracker = new GPSTracker(ctx);
        double startlat = gpsTracker.getLatitude();
        double startlng = gpsTracker.getLongitude();

        LatLng startCoordinets = new LatLng(startlat,startlng);

        try {
            String url = directionsJSONParser.getDirectionsUrl(startCoordinets, toHere);
            directionsJSONParser.drawUrlPAth(url);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch(event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Timer
                //navigateToTimer();
                break;
        }
    }
}
