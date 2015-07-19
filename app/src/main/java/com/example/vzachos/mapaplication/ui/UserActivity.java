package com.example.vzachos.mapaplication.ui;

import android.os.Bundle;


import com.example.vzachos.mapaplication.core.User;
import com.example.vzachos.mapaplication.core.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.vzachos.mapaplication.R;

public class UserActivity extends BootstrapActivity implements OnMapReadyCallback{

  /*  @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;*/
   protected SupportMapFragment supportMapFragment;


    private User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(Constants.Extra.USER);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         supportMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)) ;
        System.out.println("BEFOREASYNC : "+supportMapFragment);
                supportMapFragment.getMapAsync(this);
      /*  Picasso.with(this).load(user.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(avatar);

        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));*/

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
