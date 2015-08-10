package com.via.paul.treasurehunt;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Hunt {

    ArrayList<LatLng> forme;
    private Location start,Treasure;
    private String name, lieu;

    public void Hunt(){
        //this.forme = new ArrayList<>();
    }

    public void setForme(ArrayList<LatLng> forme) {
        this.forme = forme;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setTreasure(Location treasure) {
        Treasure = treasure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getLieu() {

        return lieu;
    }

    public ArrayList<LatLng> getForme() {
        return forme;
    }

    public Location getStart() {
        return start;
    }

    public Location getTreasure() {
        return Treasure;
    }

    public String getName() {
        return name;
    }

    public void addpoint(Double lat ,Double lng){
        forme.add(new LatLng(lat,lng));
    }

    public void saveHunt (String cheminFic){
        ObjectOutputStream oos = null;

        try{
            final FileOutputStream fichier = new FileOutputStream(cheminFic);
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(this);
        }catch (final java.io.IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadHunt(String cheminFic){
        ObjectInputStream ois = null;

        try {
            final FileInputStream fichier = new FileInputStream(cheminFic);
            ois = new ObjectInputStream(fichier);
            ois.readObject();

        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
