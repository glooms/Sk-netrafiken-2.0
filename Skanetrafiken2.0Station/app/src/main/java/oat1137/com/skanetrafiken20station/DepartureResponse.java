package oat1137.com.skanetrafiken20station;

import java.util.ArrayList;

/**
 * Created by olle on 2015-04-11.
 */
interface DepartureResponse {
    void departureFindFinish(ArrayList<Departure> departures);
    Station getStation();
}
