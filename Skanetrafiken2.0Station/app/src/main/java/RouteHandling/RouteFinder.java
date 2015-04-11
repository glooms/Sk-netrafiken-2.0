package RouteHandling;

/**
 * Created by olle on 2015-04-11.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
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
import java.io.InputStream;
import java.util.ArrayList;

import oat1137.com.skanetrafiken20station.Departure;
//import oat1137.com.skanetrafiken20station.Route;
//import oat1137.com.skanetrafiken20station.RouteResponse;

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
public class RouteFinder extends AsyncTask {
    public static final String TAG = "ClosestStation";
    ArrayList<Route> departures;

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(departures);
        Log.d(TAG, "A NEW HOPE");
      /*FIX  mListener.stationFindFinish(stations); */

    }


    /*FIX StationResponse mListener;

    public void setListener(StationResponse listener){
        mListener = listener;
    }
    */
    @Override
    protected Object doInBackground(Object[] params) {
        Log.d("HOPE", "FIRST HOPE");
        Departure d;
        departures = new ArrayList<>();
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet("http://www.labs.skanetrafiken.se/v2.2/journeypath.asp?cf=19110133954033103483800355900040016&id=1"); //"www.labs.skanetrafiken.se/v2.2/trafficmeans.asp");
        try {
            Log.d("HOPE", "TRYING HOPE");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity resEntity = response.getEntity();
            //Response
  //          try {
                String text = getASCIIContentFromEntity(resEntity);
                Log.d("HOPE",  text);


//                XmlPullParser parser = Xml.newPullParser();
//                InputStream is = resEntity.getContent();
 //               parser.setInput(resEntity.getContent(), null);
 //               String name;
//                while (parser.next() > 0) {
  //                  String contents = parser.getText();
 //                   Log.d(TAG, "HOPEFULPARSE: " + contents);
  //              }
  //
 /*
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    name = parser.getName();
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }


                    if (name.equals("NearestStopArea")) {

                    }
                }*/
                Log.d(TAG, "After reading xml");
        /*    } catch (XmlPullParserException e) {
                Log.d(TAG, "PullParserException: " + e.getMessage());
            } catch (IOException e){
                Log.d(TAG, "IOException: " + e.getMessage());
            } */
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
//        Log.d(TAG, "NR OF STATIONS " + stations.size());
        return null;
    }
    protected String getASCIIContentFromEntity(HttpEntity entity)
            throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }
    /*
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
        waitFor(parser, "Name");
        parser.next();
        String sName = parser.getText();
        Log.d(TAG, "Name: " + sName);
        if (sName != null) {
            name = sName;
        }
        parser.next();
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
    } */
}
