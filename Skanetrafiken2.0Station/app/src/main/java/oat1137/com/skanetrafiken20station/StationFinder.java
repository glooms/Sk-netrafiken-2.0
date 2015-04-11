package oat1137.com.skanetrafiken20station;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by olle on 2015-04-11.
 */
public class StationFinder extends AsyncTask  {
    public static final String TAG = "ClosestStation";
    ArrayList<Station> stations;

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(stations);
        Log.d(TAG, "ONPOSTEXECUTE");
        mListener.processFinish(stations);

    }


    AsyncResponse mListener;

    public void setListener(AsyncResponse listener){
        mListener = listener;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Station s;
        stations = new ArrayList<>();
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
        return null;
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
            waitFor(parser, "Id");
                parser.next();
                String sId = parser.getText();
                Log.d(TAG, "ID: " + sId);
                if (sId != null) {
                    id = Integer.valueOf(sId);
                }
        waitFor(parser, "X");
                parser.next();
                String sX = parser.getText();
                Log.d(TAG, "X: " + sX);
                if (sX != null) {
                    X = Integer.valueOf(sX);
                }
                parser.next();
            waitFor(parser, "Y");
                parser.next();
                String sY = parser.getText();
                Log.d(TAG, "Y: " + sY);
                if (sY != null) {
                    Y = Integer.valueOf(sY);
                }
        waitFor(parser, "Name");
                parser.next();
                String sName = parser.getText();
                Log.d(TAG, "Name: " + sName);
                if (sName != null) {
                    name = sName;
                }
                parser.next();
        waitFor(parser, "Distance");
                parser.next();
                String sDist = parser.getText();
                Log.d(TAG, "Distance: " + sDist);
                if (sDist != null) {
                    distance = Integer.valueOf(sDist);
                }
        waitFor(parser, "NearestStopArea");
        if (!name.equals("") && id != -1 && X != -1 && Y != -1 && distance != -1) {
            return new Station(id, X, Y, name, distance);
        }
        return null;
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
