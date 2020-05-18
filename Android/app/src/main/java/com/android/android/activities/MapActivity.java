package com.android.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.android.R;
import com.android.android.entities.AppDetails;
import com.android.android.entities.Station;
import com.android.android.utilities.ActivityStarter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MapActivity extends AppCompatActivity
                         implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private AppDetails appDetails;

    public static Double longitude = null;

    public static Double latitude = null;

    private LocationListener locationListener;

    private boolean hasZoomedOnCurrentPosition = false;

    private GoogleMap googleMap = null;

    private HashMap<String, Marker> markerHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        appDetails = AppDetails.getAppDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //latitude = 47.114577;
                //longitude = 27.632415;
                updateCurrentPosition();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        int MY_PERMISSION_ACCESS_FINE_LOCATION = 1;

        while (ContextCompat.checkSelfPermission( this,android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSION_ACCESS_FINE_LOCATION
            );
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        List<Station> stationList = appDetails.getStationList();
        for(Station station : stationList) {
            String coordinatesString = station.getCoordinates();
            coordinatesString = coordinatesString.substring(1, coordinatesString.length()-1);
            String[] coordinates = coordinatesString.split(",");
            double latitude = Double.valueOf(coordinates[0]);
            double longitude = Double.valueOf(coordinates[1]);
            LatLng stationPoint = new LatLng(latitude, longitude);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(stationPoint).title(station.getName()));
            marker.setTag(station.getId());
            markerHashMap.put(station.getId().toString(), marker);
        }

        googleMap.setOnMarkerClickListener(this);

        if(latitude != null && longitude != null) {
            createAndMoveToCurrentPosition();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        UUID stationId = (UUID) marker.getTag();
        if(stationId != null) {
            Log.d("MARKER", "UUID of clicked station is " + stationId.toString());
            //TODO: lansat station activity
        }
        return false;
    }

    private void updateCurrentPosition() {
        if(!hasZoomedOnCurrentPosition) {
            createAndMoveToCurrentPosition();
        }
        else {
            Marker marker = markerHashMap.get("Current point");
            marker.remove();
            LatLng currentPoint = new LatLng(latitude, longitude);
            marker = googleMap.addMarker(new MarkerOptions().position(currentPoint).title("Current point"));
            markerHashMap.put("Current point", marker);
        }
    }

    private void createAndMoveToCurrentPosition() {
        if(latitude != null && longitude != null && googleMap != null) {
            LatLng currentPoint = new LatLng(latitude, longitude);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(currentPoint).title("Current point"));
            markerHashMap.put("Current point", marker);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPoint));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            hasZoomedOnCurrentPosition = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_account:
                ActivityStarter.openAccountActivity(this);
                return true;
            case R.id.menu_message:
                ActivityStarter.openMessageActivity(this);
                return true;
            case R.id.menu_payment:
                ActivityStarter.openPaymentActivity(this);
                return true;
            case R.id.menu_report_history:
                ActivityStarter.openReportHistoryActivity(this);
                return true;
            case R.id.menu_transaction_history:
                ActivityStarter.openTransactionHistoryActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
