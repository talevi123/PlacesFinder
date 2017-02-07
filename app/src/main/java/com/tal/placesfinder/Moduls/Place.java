package com.tal.placesfinder.Moduls;

import android.database.Cursor;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.tal.placesfinder.DB.PlacesSQLiteHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by tal on 02/12/16.
 */
public class Place implements Serializable {

    public String name;
    public String vicinity;
    public String photo;
    public String latitude;
    public String longitude;
    public String distance;

//    public static Place createPlace (Object object) throws JSONException {
//        if (object instanceof Place) {
//            return (Place) object;
//        } else if (object instanceof JSONObject) {
//            return new Place((JSONObject) object);
//        }
//        else {
//            return new Place((Cursor) object);
//        }
//    }

    public Place(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.vicinity = jsonObject.optString("vicinity");
        JSONArray photosArray = jsonObject.optJSONArray("photos");
        if (photosArray != null && photosArray.length() > 0) {
            JSONObject photoObject = photosArray.optJSONObject(0);
            this.photo = photoObject.optString("photo_reference");
        }
        this.latitude = jsonObject.optJSONObject("geometry").optJSONObject("location").optString("lat");
        this.longitude = jsonObject.optJSONObject("geometry").optJSONObject("location").optString("lng");
    }

    public Place(JSONObject jsonObject, int i) {
        this.name = jsonObject.optString("name");
        this.vicinity = jsonObject.optString("vicinity");
        this.photo = jsonObject.optString("photo");
        this.latitude = jsonObject.optString("latitude");
        this.longitude = jsonObject.optString("longitude");
        this.distance = jsonObject.optString("distance");
    }

    public Place(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_NAME));
        this.vicinity = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_ADRESS));
        this.photo = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_PHOTO));
       // this.latitude = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_LATITUDE));
       // this.longitude = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_LONGITUDE));
        this.distance = cursor.getString(cursor.getColumnIndex(PlacesSQLiteHelper.COLUMN_DISTANCE));
    }

    public String getName(){return name;}

    public String getVicinity(){return vicinity;}

    public String getPhotoUrl() {
        if(photo != null){
            return buildUrl();
        }
        return null;
    }

    public String getPhoto(){return photo;}


    public String getLatitude(){return latitude;}

    public String getLongitude(){return longitude;}

    public String getDistance(){return distance;}

    public void setDistance(LatLng latLng1, LatLng latLng2){
        String distance = distanceBetweenTwoPoints(latLng1, latLng2);
        this.distance = distance;
    }

    private String buildUrl() {
        String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
        String REFERENCE_PARAM = photo;
        String SUFFIX_URL = "&key=AIzaSyD5anlL6tztkNRrsZK9PBiEhkuzVRsz5Ow";
        return BASE_URL + REFERENCE_PARAM + SUFFIX_URL;
    }

    private String distanceBetweenTwoPoints(LatLng latLng1, LatLng latLng2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latLng1.latitude);
        locationA.setLongitude(latLng2.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latLng2.latitude);
        locationB.setLongitude(latLng2.longitude);

        String distance = String.valueOf(locationA.distanceTo(locationB) / 1000);

        return distance;
    }

}



