package com.via.paul.treasurehunt;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by paul on 09/08/15.
 */
public class HuntSelectionActivity extends AppCompatActivity {

    String gameDirectory;
    ListView huntList;
    ArrayList<String> hunts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hunt_selection_layout);

        Toast.makeText(getApplicationContext(), "Long click on a hunt to upload it to the server", Toast.LENGTH_SHORT).show();

        gameDirectory = Environment.getExternalStorageDirectory().getPath() + "/treasurehunt";


        File dossier = new File(gameDirectory);
        if (!dossier.exists()) {
            new File(gameDirectory).mkdir();
            Toast.makeText(getApplicationContext(), "Creating directory " + gameDirectory, Toast.LENGTH_LONG).show();
        }

        huntList = (ListView) findViewById(R.id.huntList);
        hunts = new ArrayList<>();
        for (File fichier : dossier.listFiles()) {
            if (fichier.getName().endsWith(".hunt")) {
                hunts.add(fichier.getName().substring(0,fichier.getName().length()-5));
            }
        }

        huntList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HuntSelectionActivity.this, GameActivity.class);
                i.putExtra("hunt", (String) huntList.getItemAtPosition(position));
                startActivity(i);
            }
        });

        huntList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ftpUploadFile fuf = new ftpUploadFile();
                fuf.execute((String)huntList.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(), "Treasure hunt uploaded", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, hunts);
        huntList.setAdapter(adapter);
    }

    public class ftpUploadFile extends AsyncTask<String, Void, Void> {

        MyFTPClient ftp;
        ArrayList<String>list;

        @Override
        protected Void doInBackground(String... params) {
            String hunt = params[0];
            ftp = new MyFTPClient();
            ftp.ftpConnect("abbaye.noip.me", "hunt", "treasure", 21);
            ftp.ftpChangeDirectory("/home/hunt/hunts/");
            ftp.ftpUpload(Environment.getExternalStorageDirectory().getPath() + "/treasurehunt/" +hunt + ".hunt", hunt + ".hunt", "/home/hunt/hunts/", getApplicationContext());
            ftp.ftpDisconnect();

            return null;
        }
    }



}

