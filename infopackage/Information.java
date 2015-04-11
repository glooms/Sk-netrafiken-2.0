package oat1137.com.skanetrafiken20station;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import RouteHandling.RouteFinder;


public class Information {

    private static String[] mockRoutes = {"Staffanstorp Orkestervägen", "Staffanstorp Staffansvallen", "Staffanstorp Syrenvägen", "Staffanstorp Lundavägen", "Staffanstorp Storgatan"
            , "Staffanstorp Rådhuset", "Staffanstorp Rydbergs stig", "Staffanstorp Trekantsgränd", "Staffanstorp Hantverksvägen", "Staffanstorp Sliparevägen", "Staffanstorp Industrivägen",
            "Knästorp väg 108", "Lund Södra Tpl", "Lund S:t Lars Trädgårdsgatan", "Lund Ruben Rausings gata", "Lund Arenan", "Lund Idrottsplatsen", "Lund C", "Lund Allhelgonakyrkan",
            "Lund Univ-sjukhuset", "Lund BMC", "Lund LTH", "Lund Ideon", "Lund Höjdpunkten"};
    Station closestStation;
    StationFinder sf;
    DepartureFinder df;

    public Information() {
        final String TAG = "Information";
        sf = new StationFinder();
        closestStation = sf.getClosestStation();
    }

    public synchronized Station getClosestStation() {
        return closestStation;
    }

    public synchronized ArrayList<Departure> getDeparturesFromStation(Station s) {
        df = new DepartureFinder();
        return df.departures(s);
    }

    public synchronized String[] getRoutes() {
        return mockRoutes;
    }
}