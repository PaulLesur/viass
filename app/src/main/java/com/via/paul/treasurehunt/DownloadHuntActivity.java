package com.via.paul.treasurehunt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by paul on 10/08/15.
 */
public class DownloadHuntActivity extends AppCompatActivity {

    ListView huntListView;
    ArrayAdapter adapter;
    MyFTPClient client;
    ArrayList<String> huntList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.download_hunt_layout);

        huntListView = (ListView) findViewById(R.id.listHunt);

        client = new MyFTPClient();


        ftpGetFilesList fgfl = new ftpGetFilesList();
        huntList = new ArrayList<>();
        try {
            huntList = fgfl.execute(client).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Vérifiez votre connexion internet", Toast.LENGTH_SHORT);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Vérifiez votre connexion internet", Toast.LENGTH_SHORT);
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, huntList);
        huntListView.setAdapter(adapter);

        huntListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ftpDownloadFile fdf = new ftpDownloadFile();
                fdf.execute((String) huntListView.getItemAtPosition(position));
                Toast.makeText(getApplicationContext(), "Treasure hunt downloaded", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class ftpGetFilesList extends AsyncTask<MyFTPClient, Void, ArrayList<String>> {

        MyFTPClient ftp;
        ArrayList<String> list;

        @Override
        protected ArrayList<String> doInBackground(MyFTPClient... params) {
            ftp = params[0];
            ftp.ftpConnect("abbaye.noip.me", "hunt", "treasure", 21);
            ftp.ftpChangeDirectory("/home/hunt/hunts/");
            list = new ArrayList<>();

            try {
                for (FTPFile file : ftp.getFiles()) {
                    list.add(file.getName());
                }
            }catch (NullPointerException npe){
            }
            return list;
        }
    }

    public class ftpDownloadFile extends AsyncTask<String, Void, Void> {

        MyFTPClient ftp;
        ArrayList<String> list;

        @Override
        protected Void doInBackground(String... params) {
            String hunt = params[0];
            ftp = new MyFTPClient();
            ftp.ftpConnect("abbaye.noip.me", "hunt", "treasure", 21);
            ftp.ftpChangeDirectory("/home/hunt/hunts/");
            ftp.ftpDownload("./" + hunt, Environment.getExternalStorageDirectory().getPath() + "/treasurehunt/" + hunt);

            return null;
        }
    }


}


