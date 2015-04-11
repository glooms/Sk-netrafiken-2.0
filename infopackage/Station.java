package oat1137.com.skanetrafiken20station;

/**
 * Created by olle on 2015-04-11.
 */
public class Station {
     String name;
     int id;
     double X;
     double Y;
     double distance;

    public Station(int id, double X, double Y,String name, double distance) {
        this.id = id;
        this.X = X;
        this.Y = Y;
        this.name = name;
        this.distance = distance;
    }
}
