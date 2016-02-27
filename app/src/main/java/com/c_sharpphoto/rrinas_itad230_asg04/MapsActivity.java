package com.c_sharpphoto.rrinas_itad230_asg04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    protected static GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final String TAG = "BroadcastTest";
    protected double newlat, newlong, oldlat, oldlong;
    private Intent intent;
    private Intent locService;
    private Intent keyTape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        intent = new Intent(this, MapsActivity.class);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng startloc = new LatLng(47.7052345, -122.1692844);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startloc));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.c_sharpphoto.rrinas_itad230_asg04/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.c_sharpphoto.rrinas_itad230_asg04/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }



    private void updateMap(Intent intent) {

        newlat = intent.getDoubleExtra("lat",47.8);
        newlong = intent.getDoubleExtra("long", -122.2);
        if (oldlat == 0 && oldlong == 0) {
            oldlat = newlat;
            oldlong = newlong;
        }
        LatLng newlocation = new LatLng(newlat, newlong);
        mMap.addMarker(new MarkerOptions().position(newlocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newlocation));
        PolylineOptions trackOptions = new PolylineOptions()
                .add(new LatLng(oldlat, oldlong))
                .add(new LatLng(newlat, newlong)) ;
        Polyline polyline = mMap.addPolyline(trackOptions);
        oldlat = newlat;
        oldlong = newlong;






    }

    public void ServiceStart(View view)
    {
        locService = new Intent(this, LocationService.class);
        startService(locService);

    }

    public void ServiceStop(View view)
    {
        locService = new Intent(this, LocationService.class);
        stopService(locService);

    }


    public void KeyGen(View view) {
        keyTape = new Intent(this, KeyGenService.class);
        startService(keyTape);

    }

    public void KeyRead(View view) {


                TextView keyFileText = (TextView) findViewById(R.id.readKeyFile);
                keyFileText.setText(readSavedData());


    }

    public String readSavedData ( ) {
        StringBuilder datax = new StringBuilder("");
        try {
            FileInputStream fIn = openFileInput ( "keyLog.txt" ) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax.append(readString);
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        return datax.toString() ;
    }

        /*try
        {
            InputStream instream = openFileInput("keyLog.txt");
            if (instream != null)
            {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line = "",line1 = "";
                try
                {
                    while ((line = buffreader.readLine()) != null)
                        line1+=line;
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                TextView keyFileText = (TextView) findViewById(R.id.readKeyFile);
                keyFileText.setText(line);
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e(TAG, "******* File Not Found!");
        }
*/
        /*String filename = "keyLog.txt";
        String entry = dat+" "+KeyGenerator();

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "******* File not found.");
        }
        try {
            fos.write(entry.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "******* IOException");
        }*/


}
