package com.via.paul.treasurehunt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class HuntCreatorActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    Button start, end, place, save;
    LocationManager lm;
    Location actualLocation;
    HuntCreatorActivity hca = this;
    GoogleMap myMap;
    Hunt myHunt;
    boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hunt_creator_layout);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        start = (Button) findViewById(R.id.buttonStart);
        end = (Button) findViewById(R.id.buttonEnd);
        end.setBackgroundColor(Color.GRAY);
        end.setEnabled(false);
        place = (Button) findViewById(R.id.buttonPlace);
        place.setEnabled(false);
        place.setBackgroundColor(Color.GRAY);
        save = (Button) findViewById(R.id.buttonSave);
        save.setEnabled(false);
        save.setBackgroundColor(Color.GRAY);

        myHunt = new Hunt();
        myHunt.forme = new ArrayList<>();

        LocationListener ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                actualLocation = location;

                LatLng latlngmylocation = new LatLng(actualLocation.getLatitude(), actualLocation.getLongitude());
                Toast.makeText(getApplicationContext(), latlngmylocation.toString(), Toast.LENGTH_SHORT).show();
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngmylocation, 16));

                if(isRecording) {
                    myHunt.addpoint(location.getLatitude(), location.getLongitude());

                    PolylineOptions rectOptions = new PolylineOptions();

                    for (LatLng point : myHunt.getForme()) {
                        rectOptions.add(point);
                    }

                    Polyline polyline = myMap.addPolyline(rectOptions);
                    polyline.setColor(Color.argb(255, 100, 50, 0));
                }

                //Toast.makeText(getApplicationContext(), actualLocation.toString(), Toast.LENGTH_SHORT).show();
//                hca.onMapReady(hca.mMap);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording=true;
                start.setEnabled(false);
                start.setBackgroundColor(Color.GRAY);
                end.setEnabled(true);
                end.setBackgroundColor(Color.argb(255, 100, 50, 0));
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = false;
                PolygonOptions rectOptions = new PolygonOptions();

                for (LatLng point : myHunt.getForme()) {
                    rectOptions.add(point);
                }

                Polygon polygon = myMap.addPolygon(rectOptions);
                polygon.setFillColor(Color.argb(100,255,255,150));
                polygon.setStrokeColor(Color.argb(255, 100, 50, 0));
                end.setEnabled(true);
                end.setBackgroundColor(Color.GRAY);
                place.setEnabled(true);
                place.setBackgroundColor(Color.argb(255, 100, 50, 0));
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHunt.setTreasure(actualLocation);
                myMap.addMarker(new MarkerOptions()
                        .title("Treasure")
                        .snippet("Your place")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasure))
                        .position(new LatLng(actualLocation.getLatitude(), actualLocation.getLongitude())));
                place.setEnabled(false);
                place.setBackgroundColor(Color.GRAY);
                save.setEnabled(true);
                save.setBackgroundColor(Color.argb(255,100,50,0));

            }
        });

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, ll);


    }

    @Override
    public void onMapReady(GoogleMap map) {

        myMap = map;
        map.setMyLocationEnabled(true);

        //Toast.makeText(getApplicationContext(), myHunt.getForme().toString(), Toast.LENGTH_SHORT).show();

        try {
            LatLng latlngmylocation = new LatLng(actualLocation.getLatitude(), actualLocation.getLongitude());
            Toast.makeText(getApplicationContext(), latlngmylocation.toString(), Toast.LENGTH_SHORT).show();
            map.moveCamera(CameraUpdateFactory.newLatLng(latlngmylocation));
        }catch(NullPointerException npe){
            Toast.makeText(getApplicationContext(), "Unknown location", Toast.LENGTH_SHORT).show();
        }

    }
}