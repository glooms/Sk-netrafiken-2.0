package oat1137.com.skanetrafiken20station;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AsyncResponse {
    Station closestStation;
    StationFinder sf;
    FindDepartures fd;
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
    @Override
    public void processFinish(ArrayList<Station> stations) {
        double minDistance = Double.MAX_VALUE;
        for (Station s : stations) {
            if (s.getDistance() < minDistance) {
                minDistance = s.getDistance();
                closestStation = s;
            }
        }

        fd = new FindDepartures();
        fd.execute();
        Log.d(TAG, "CLOSEST STATION " + closestStation.getName());
        Log.d(TAG, "CLOSEST STATIONID " + closestStation.getId());
    }
    public void finishReadStations(ArrayList<Station> stations){
                    /* find station with lowest distance */
        double minDistance = Double.MAX_VALUE;
        for (Station s : stations) {
            if (s.getDistance() < minDistance) {
                minDistance = s.getDistance();
                closestStation = s;
            }
        }
        Log.d(TAG, "CLOSEST STATION " + closestStation.getName());
        Log.d(TAG, "CLOSEST STATIONID " + closestStation.getId());
    }
    public void finishReadDeparture(ArrayList<Departure> departures){
                    /* find station with lowest distance */
        for (Departure d : departures) {
            Log.d(TAG, "NAME: " + d.name);
            Log.d(TAG, "LINE: " + d.id);
            Log.d(TAG, "TIME: " + d.time);
            Log.d(TAG, "DEST: " + d.dest);
        }
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
      class FindDepartures extends AsyncTask {
        final String TAG = "Skanetrafiken";
        protected void onPostExecute(ArrayList<Station> stations) {
            super.onPostExecute(stations);

        }
        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, "INTO FindDepartures");
            Departure d;
            ArrayList<Departure> departures = new ArrayList<>();
            // Create a new HttpClient and Post Header
            Log.d(TAG, "INTO postData");
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet("http://www.labs.skanetrafiken.se/v2.2/stationresults.asp?selPointFrKey=" + closestStation.getId()); //"www.labs.skanetrafiken.se/v2.2/trafficmeans.asp");
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity resEntity = response.getEntity();
                //Response
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(resEntity.getContent(), null);

                    String name;
                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        name = parser.getName();
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        /* Read stop area information */

                        if (name.equals("Line")) {
                            d = readDeparture(parser);
                            if (d != null) {
                                departures.add(d);
                            }
                        }
                        Log.d(TAG, "After readStation. Name: " + parser.getName());
                    }
                    Log.d(TAG, "After reading xml");
                } catch (XmlPullParserException e) {
                    Log.d(TAG, "PullParserException: " + e.getMessage());
                } catch (IOException e){
                    Log.d(TAG, "IOException: " + e.getMessage());
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            Log.d(TAG, "NR OF DEPARTURE " + departures.size());
            finishReadDeparture(departures);
            return null;
        }
    }


    private Departure readDeparture(XmlPullParser parser) throws XmlPullParserException, IOException {
        final String TAG = "Skanetrafiken";
        parser.next();
        String type = parser.getName();

        String name = "";
        int id = 0;
        String time = "";
        String dest = "";

        Log.d(TAG, "ReadParser: Type: " + type);
        waitFor(parser, "Name");
        parser.next();
        name = parser.getText();
        Log.d(TAG, "NAME: " + name);
        if (name == null) {
            Log.d(TAG, "RECIEVED NULL");
            return null;
        }
        waitFor(parser, "No");
        parser.next();
        String nbr = parser.getText();
        Log.d(TAG, "ID: " + name);
        if (nbr == null) {
            Log.d(TAG, "RECIEVED NULL");
            return null;
        }
        id = Integer.valueOf(nbr);

        waitFor(parser, "JourneyDateTime");
        parser.next();
        String dTime = parser.getText();
        Log.d(TAG, "Time: " + dTime);
        if (dTime == null) {
            Log.d(TAG, "RECIEVED NULL");
            return null;

        }
        time = dTime;
        parser.next();
        waitFor(parser, "Towards");
        parser.next();
        String to = parser.getText();
        Log.d(TAG, "Towards: " + to);
        if (to == null) {
            Log.d(TAG, "RECIEVED NULL");
            return null;
        }
        dest = to;
        parser.next();

        waitFor(parser, "Line");

        Log.d(TAG, "SURVIVED");
        return new Departure(name, id, time, dest);
    }

    private void waitFor(XmlPullParser parser, String s) throws XmlPullParserException, IOException{
        String type = parser.getName();
        while (type == null) {
            parser.next();
            type = parser.getName();
        }
        if (s == null)
            Log.d(TAG, "ERROR");
        while(!type.equals(s)) {
            do {
                parser.next();
                type = parser.getName();
            } while (type == null);
        }
    }
}
