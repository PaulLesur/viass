package com.via.paul.treasurehunt;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by paul on 10/08/15.
 */
public class DownloadHuntActivity extends Activity {

    ListView listHunt;
    MyFTPClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.setContentView(R.layout.download_hunt_layout);

        client = new MyFTPClient();
        client.ftpConnect("abbaye.noip.me", "hunt", "treasure", 21);
        String doc = client.ftpGetCurrentWorkingDirectory();
        Toast.makeText(getApplicationContext(), doc, Toast.LENGTH_SHORT).show();

        listHunt = (ListView) findViewById(R.id.listHunt);

    }
}
