package com.example.vzachos.mapaplication.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.vzachos.mapaplication.Injector;
import com.example.vzachos.mapaplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserListFragment extends Fragment implements OnMapReadyCallback {
/*
    @Inject protected BootstrapServiceProvider serviceProvider;
    @Inject protected LogoutService logoutService;*/

    protected SupportMapFragment supportMapFragment;
    MainActivity mainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*  Injector.inject(this);*/

        supportMapFragment = ((SupportMapFragment) mainActivity.getSupportFragmentManager().findFragmentById(R.id.map)) ;
        System.out.println("BEFOREASYNC : " + supportMapFragment);
        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        System.out.println("GMAPCALLED "+map);
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}
