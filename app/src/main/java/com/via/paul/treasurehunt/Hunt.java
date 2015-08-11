package com.via.paul.treasurehunt;

import android.location.Location;
import android.os.Environment;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hunt implements Serializable{

    transient ArrayList<LatLng> forme;
    ArrayList<Double> lats, lngs;
    private transient Location start,Treasure;
    private String name, lieu;
    private double latitudeTreasure, longitudeTreasure;

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

    public void saveHunt (String cheminFic, Hunt hunt){
        ObjectOutputStream oos = null;
        this.longitudeTreasure = Treasure.getLongitude();
        this.latitudeTreasure = Treasure.getLatitude();

        lats = new ArrayList<>(this.forme.size());
        lngs = new ArrayList<>(this.forme.size());

        int i;

        for (i=0; i<forme.size(); i++){
            lats.add(i, 0.0);
            lngs.add(i, 0.0);
        }

        for(i=0; i<forme.size(); i++){
            lats.set(i, this.forme.get(i).latitude);
            lngs.set(i, this.forme.get(i).longitude);
        }


        try{
            final FileOutputStream fichier = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/treasurehunt/" +cheminFic+".hunt");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(hunt);
        }catch (final java.io.IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    //oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Hunt loadHunt(String cheminFic){
        ObjectInputStream ois = null;

        Hunt  hunt=null;
        try  {
            FileInputStream  fin=new FileInputStream(cheminFic);
            ObjectInputStream  oin=new ObjectInputStream(fin);
            hunt=(Hunt)oin.readObject();


            Location tempLoc = new Location("");
            tempLoc.setLatitude(hunt.latitudeTreasure);
            tempLoc.setLongitude(hunt.longitudeTreasure);
            hunt.setTreasure(tempLoc);


            hunt.forme = new ArrayList<>(hunt.lats.size());

            int i;

            for (i=0; i<hunt.lats.size(); i++){
                hunt.forme.add(i, new LatLng(0.0,0.0));
            }

            for(i=0; i<hunt.lats.size(); i++){
                hunt.forme.set(i, new LatLng(hunt.lats.get(i), hunt.lngs.get(i)));
            }


            oin.close();
            fin.close();
        }catch(ClassNotFoundException  nfe) {
            nfe.printStackTrace();
        }  catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return hunt;
//
//        this.Treasure = new Location("");
//        this.Treasure.setLatitude(this.latitudeTreasure);
//        this.Treasure.setLatitude(this.longitudeTreasure);






    }

    public boolean pointInPolygon(LatLng point, Polygon polygon) {
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size()-1); //remove the last point that is added automatically by getPoints()

        // for each edge
        for (int i=0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    public boolean rayCrossesSegment(LatLng point, LatLng a,LatLng b) {
        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
        double px = point.longitude,
                py = point.latitude,
                ax = a.longitude,
                ay = a.latitude,
                bx = b.longitude,
                by = b.latitude;
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax <0 || bx <0) { px += 360; ax+=360; bx+=360; }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;


        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))){
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)){
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }

    }
}
