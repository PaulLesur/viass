package com.via.paul.treasurehunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceGroup;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by paul on 11/08/15.
 */
public class GameActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    LocationManager lm;
    Location actualLocation;
    Hunt myHunt;
    String huntFileName;
    SeekBar pb;
    LocationListener ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.game_layout);
        Bundle extras = getIntent().getExtras();

        pb = (SeekBar)findViewById(R.id.seekBar);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapGame);
        mapFragment.getMapAsync(this);

        huntFileName = extras.getString("hunt");

        Toast.makeText(getApplicationContext(), Environment.getExternalStorageDirectory().getPath() + "/treasurehunt/" + huntFileName + ".hunt", Toast.LENGTH_SHORT).show();

        myHunt = new Hunt();
        myHunt = Hunt.loadHunt(Environment.getExternalStorageDirectory().getPath() + "/treasurehunt/" + huntFileName + ".hunt");



        pb.setMax(500);
        pb.setProgress(0);

        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                actualLocation = location;
                LatLng latlngmylocation = new LatLng(actualLocation.getLatitude(), actualLocation.getLongitude());
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngmylocation, 16));

                int progress = (int)actualLocation.distanceTo(myHunt.getTreasure());

                pb.setProgress(progress);

                if (progress<3){
                    Toast.makeText(getApplicationContext(), "Treasure located !", Toast.LENGTH_LONG).show();
                    myMap.addMarker(new MarkerOptions()
                            .title("Treasure")
                            .snippet("You find it !")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasure))
                            .position(new LatLng(myHunt.getTreasure().getLatitude(), myHunt.getTreasure().getLongitude())));

                    showWinDialog();

                }

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


        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, ll);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(ll);
        ll = null;
        lm = null;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        myMap = map;
        map.setMyLocationEnabled(true);

        PolygonOptions rectOptions = new PolygonOptions();


        for (LatLng point : myHunt.getForme()) {
            rectOptions.add(point);
        }

        Polygon polygon = map.addPolygon(rectOptions);
        polygon.setFillColor(Color.argb(100, 255, 255, 150));
        polygon.setStrokeColor(Color.argb(255, 100, 50, 0));

    }


    protected void showWinDialog() {

        //Button button = (Button) findViewById(R.id.buttonOk);
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(GameActivity.this);
        View promptView = layoutInflater.inflate(R.layout.treasure_found_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
        alertDialogBuilder.setView(promptView);

        // setup a dialog window


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



}
