package oat1137.com.skanetrafiken20station;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.util.Xml;

import org.apache.http.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olle on 2015-04-11.
 */

public class SkanetrafikenService extends AsyncTask {
    public static final String TAG = SkanetrafikenService.class.getSimpleName();
    double xCoord;
    double yCoord;
    String getStationURL;
    String getDeparturesURL;

    @Override
    protected Object doInBackground(Object[] params) {
        Log.d(TAG, "INTO DOINBACKGROUND");
        xCoord = 6179309;
        yCoord = 1338077;

        // Create a new HttpClient and Post Header
        Log.d(TAG, "INTO postData");
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost();
        httppost.setHeader("host", "http://www.labs.skanetrafiken.se");
        httppost.setHeader("path", "/v2.2/neareststation.asp");
        for (Header h : httppost.getAllHeaders()) {
            Log.d(TAG, "NAME: " + h.getName() + " VALUE: " + h.getValue());
        }
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("X", Double.toString(xCoord)));
            nameValuePairs.add(new BasicNameValuePair("Y", Double.toString(yCoord)));
            nameValuePairs.add(new BasicNameValuePair("R", "50")); //Radius value in meters?
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            //Response
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(resEntity.getContent(), resEntity.getContentEncoding().toString());
                ArrayList<String> entries = new ArrayList<>();
                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    Log.d(TAG, parser.getText());
                }
            } catch (XmlPullParserException e) {
                Log.d(TAG, e.getMessage());
            } catch (IOException e){
                Log.d(TAG, e.getMessage());
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    return null;
    }
}
