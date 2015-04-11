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


public class MainActivity extends ActionBarActivity  {
    Station closestStation;
    FindClosestStation fcs;
    FindDepartures fd;
    public static final String TAG = "Skanetrafiken";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Log.d("Main", "Started application");
        Intent i = new Intent(this, SkanetrafikenService.class);
        // SkanetrafikenService sts = new SkanetrafikenService();
        fcs = new FindClosestStation();
        fcs.execute();
        while (closestStation == null) {
            Log.d(TAG, "WAITING");
        }
        fd = new FindDepartures();
        fd.execute();
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
    class FindClosestStation extends AsyncTask {

        String getStationURL;
        String getDeparturesURL;
        public AsyncResponse delegate=null;

        protected void onPostExecute(ArrayList<Station> stations) {
            super.onPostExecute(stations);

        }
        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, "INTO DOINBACKGROUND");
            Station s;
            ArrayList<Station> stations = null;
            // Create a new HttpClient and Post Header
            Log.d(TAG, "INTO postData");
            HttpClient httpclient = new DefaultHttpClient();
            double x = 6179310;
            double y = 1338078;
            double R = 50;
            HttpGet httpget = new HttpGet("http://www.labs.skanetrafiken.se/v2.2/neareststation.asp?x=6179309&y=1338077&R=500"); //"www.labs.skanetrafiken.se/v2.2/trafficmeans.asp");
            try {
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity resEntity = response.getEntity();
                //Response
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(resEntity.getContent(), null);
                    stations = new ArrayList<>();

                    String name;
                    while (parser.next() != XmlPullParser.END_DOCUMENT) {
                        name = parser.getName();
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        /* Read stop area information */

                        if (name.equals("NearestStopArea")) {
                            Log.d(TAG, "Before readStation. Name: " + name);
                            s = readStation(parser);
                            if (s != null) {
                                stations.add(s);
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
            Log.d(TAG, "NR OF STATIONS " + stations.size());
            finishReadStations(stations);
            return null;
        }
    }
    private Station readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        final String TAG = "Skanetrafiken";
        parser.next();
        String type = parser.getName();
        String name = "";
        int id = -1;
        double X = -1;
        double Y = -1;
        double distance = -1;
        Log.d(TAG, "ReadParser: Type: " + type);
        while (!parser.getName().equals("NearestStopArea")) {
            if (type.equals("Id")) {
                parser.next();
                String sId = parser.getText();
                Log.d(TAG, "ID: " + sId);
                if (sId != null) {
                    id = Integer.valueOf(sId);
                }
                parser.next();
            } else if (type.equals("X")) {
                parser.next();
                String sX = parser.getText();
                Log.d(TAG, "X: " + sX);
                if (sX != null) {
                    X = Integer.valueOf(sX);
                }
                parser.next();
            } else if (type.equals("Y")) {
                parser.next();
                String sY = parser.getText();
                Log.d(TAG, "Y: " + sY);
                if (sY != null) {
                    Y = Integer.valueOf(sY);
                }
                parser.next();
            } else if (type.equals("Name")) {
                parser.next();
                String sName = parser.getText();
                Log.d(TAG, "Name: " + sName);
                if (sName != null) {
                    name = sName;
                }
                parser.next();
            } else if (type.equals("Distance")) {
                parser.next();
                String sDist = parser.getText();
                Log.d(TAG, "Distance: " + sDist);
                if (sDist != null) {
                    distance = Integer.valueOf(sDist);
                }
                parser.next();
            } else {
                Log.d(TAG, "ELSE: " + parser.getText());
            }
            parser.next();
            type = parser.getName();
        }
        if (!name.equals("") && id != -1 && X != -1 && Y != -1 && distance != -1) {
            return new Station(id, X, Y, name, distance);
        }
        return null;
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
