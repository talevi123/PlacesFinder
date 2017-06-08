package com.tal.placesfinder.Network;

import com.tal.placesfinder.Moduls.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tal on 06/12/16.
 */
public class ApiManager {

    private static final String BASE_URL_TEXT = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    private static final String API_KEY = "&key=AIzaSyBGqisLORCjLIY65OTIMQ1SZ9EDxQy7oKg";

    private static final String BASE_URL_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String GOOGLEAPIS_URL_SUFFIX = "&radius=10000&type=post_office";


    private static final String QUEUE_NEW_URL = "http://127.0.0.1:5000/api/requestNewQueue/business_id/brunch_id/queue_id/signature";
    private static final String QUEUE_GET_URL = "http://127.0.0.1:5000/api/getUserInQueue/queue_id";
    //private static final String SUFFIX_URL = "&radius=1000";

    public static List<Place> textSearch(String query) {
        String newquery = query.replace(' ', '+');
        String results = ConnectionManager.sendGetRequest(BASE_URL_TEXT + newquery + API_KEY);
        List<Place> places = getPlaces(results);
        return places;
    }

    public static List<Place> nearbySearch(String latLng) {
        String results = ConnectionManager.sendGetRequest(BASE_URL_NEARBY + latLng + GOOGLEAPIS_URL_SUFFIX + API_KEY);
        List<Place> places = getPlaces(results);
        return places;
    }

    public static List<Place> getPlaces(String results) {
        if (results != null) {
            List<Place> places = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(results);
                JSONArray jsonArray = jsonObject.optJSONArray("results");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Place place = new Place(jsonArray.optJSONObject(i));
                        places.add(place);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return places;
        }
        return null;
    }
}
