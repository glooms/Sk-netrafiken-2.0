package oat1137.com.skanetrafiken20station;

/**
 * Created by olle on 2015-04-11.
 */
public class Station {
    private String name;
    private int id;
    private double X;
    private double Y;
    private double distance;

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public int getId() {
        return id;
    }

    public Station(int id, double X, double Y,String name, double distance) {
        this.id = id;
        this.X = X;
        this.Y = Y;
        this.name = name;
        this.distance = distance;
    }
}
