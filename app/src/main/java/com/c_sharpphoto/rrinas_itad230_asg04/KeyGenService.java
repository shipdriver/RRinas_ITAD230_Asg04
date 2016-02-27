package com.c_sharpphoto.rrinas_itad230_asg04;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class KeyGenService extends Service {

    private static final String TAG = "MEDIA";



    public KeyGenService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        KeyGenerator();
        SaveKey();
        return super.onStartCommand(intent, flags, startId);
    }
    public String KeyGenerator() {
        String keyMorsel = "Key:";
        for (int i = 0;i<18;i++) {
            int keyFrag;
            Random r = new Random();
            int randFrag = r.nextInt(35);
            if (randFrag<10){
                keyFrag = randFrag+48;
            } else {
                keyFrag = randFrag+55;
            }
            keyMorsel = keyMorsel+Character.toString((char) keyFrag)+"\n";
        }
        return keyMorsel;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void SaveKey() {
        String dat = DateFormat.getDateTimeInstance().format(new Date());
        String filename = "keyLog.txt";
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
        }
        stopSelf();



        /*FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(entry.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*public File getFileStorageDir(String fileName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), fileName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }*/

}
