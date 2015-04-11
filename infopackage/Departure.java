package oat1137.com.skanetrafiken20station;

/**
 * Created by olle on 2015-04-11.
 */


public class Departure {
    String name;
    int id;
    String time;
    String dest;

    public Departure(String name, int id, String time, String dest) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.dest = dest;
    }
}