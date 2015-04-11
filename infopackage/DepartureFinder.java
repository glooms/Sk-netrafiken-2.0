package oat1137.com.skanetrafiken20station;

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
public class DepartureFinder  {
    final String TAG = "DepartureFinder";
    ArrayList<Departure> departures;

    public DepartureFinder() {
        departures = new ArrayList<>();
    }

    public synchronized ArrayList<Departure> departures(Station s) {
        Departure d;

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://www.labs.skanetrafiken.se/v2.2/stationresults.asp?selPointFrKey=" + s.getId());
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
                }
                Log.d(TAG, "After reading xml");
            } catch (XmlPullParserException e) {
                Log.d(TAG, "PullParserException: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "IOException: " + e.getMessage());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        Log.d(TAG, "NR OF DEPARTURE " + departures.size());
        return departures;
    }


    private synchronized Departure readDeparture(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    private synchronized void waitFor(XmlPullParser parser, String s) throws XmlPullParserException, IOException {
        String type = parser.getName();
        while (type == null) {
            parser.next();
            type = parser.getName();
        }
        while (!type.equals(s)) {
            do {
                parser.next();
                type = parser.getName();
            } while (type == null);
        }
    }
}
