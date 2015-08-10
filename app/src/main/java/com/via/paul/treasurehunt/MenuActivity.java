package com.via.paul.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity {

    Button find, hide, download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        find = (Button)findViewById(R.id.buttonFind);
        hide = (Button)findViewById(R.id.buttonHide);
        download = (Button)findViewById(R.id.buttonDownload);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Find a treasure", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MenuActivity.this, HuntSelectionActivity.class);
                startActivity(i);
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hide a treasure", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MenuActivity.this, HuntCreatorActivity.class);
                startActivity(i);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Download a treasure", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
