package com.c_sharpphoto.rrinas_itad230_asg04;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private double newlat, newlong, oldlat, oldlong;
    private static final String TAG = "MEDIA";
    private static final String LOG_TAG = "MEDIA";

    public LocationService() {

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public boolean stopService(Intent intent) {
        mGoogleApiClient.disconnect();
        return super.stopService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null) {
            return;
        }
        oldlat = mLastLocation.getLatitude();
        oldlong = mLastLocation.getLongitude();
        LatLng currLoc = new LatLng(oldlat, oldlong);
        MapsActivity.mMap.addMarker(new MarkerOptions().position(currLoc).title("Start Point"));

        MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, (float) 18.0));


        //LogEntry();

        createLocationRequest();
        if (mLocationRequest != null) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        newlat = mLastLocation.getLatitude();
        newlong = mLastLocation.getLongitude();
        PolylineOptions trackOptions = new PolylineOptions()
                .add(new LatLng(oldlat, oldlong))
                .add(new LatLng(newlat, newlong)) ;
        Polyline polyline = MapsActivity.mMap.addPolyline(trackOptions);
        LatLng newLoc = new LatLng(newlat, newlong);
        MapsActivity.mMap.addMarker(new MarkerOptions().position(newLoc).title("Start Point"));
        MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
        oldlat = newlat;
        oldlong = newlong;

        //LogEntry();

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy()
    {
        mGoogleApiClient.disconnect();
        // For example: mGoogleApiClient.disconnect();
        super.onDestroy();
    }
}
