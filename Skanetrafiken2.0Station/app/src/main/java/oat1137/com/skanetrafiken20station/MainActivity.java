package oat1137.com.skanetrafiken20station;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements StationResponse, DepartureResponse {
    Station closestStation;
    StationFinder sf;
    DepartureFinder df;
    public static final String TAG = "Skanetrafiken";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Main", "Started application");
        Intent i = new Intent(this, SkanetrafikenService.class);
        sf = new StationFinder();
        sf.setListener(this);
        sf.execute();
    }

    public void stationFindFinish(ArrayList<Station> stations) {
        double minDistance = Double.MAX_VALUE;
        for (Station s : stations) {
            if (s.getDistance() < minDistance) {
                minDistance = s.getDistance();
                closestStation = s;
            }
        }

        df = new DepartureFinder();
        df.setListener(this);
        df.execute();
    }
    public void departureFindFinish(ArrayList<Departure> departures){
        for (Departure d : departures) {
            Log.d(TAG, "NAME: " + d.name);
            Log.d(TAG, "LINE: " + d.id);
            Log.d(TAG, "TIME: " + d.time);
            Log.d(TAG, "DEST: " + d.dest);
        }
        Log.d(TAG, "CLOSEST STATION " + closestStation.getName());
        Log.d(TAG, "CLOSEST STATIONID " + closestStation.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public Station getStation() {
        return closestStation;
    }
}
