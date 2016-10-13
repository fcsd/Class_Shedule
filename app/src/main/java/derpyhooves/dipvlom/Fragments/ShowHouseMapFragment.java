package derpyhooves.dipvlom.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import derpyhooves.dipvlom.R;


@SuppressLint("ValidFragment")
public class ShowHouseMapFragment extends Fragment implements OnMapReadyCallback {

    private String mTitle;
    private double mlatitude;
    private double mlongitude;

    public ShowHouseMapFragment(double latitude, double longitude, String title) {
        mlatitude=latitude;
        mlongitude=longitude;
        mTitle=title;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_show_house_map, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng house = new LatLng(mlatitude, mlongitude);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(house, 16));
        googleMap.addMarker(new MarkerOptions().position(house).title(mTitle));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(house));
    }
}
