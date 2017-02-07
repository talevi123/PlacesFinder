package com.tal.placesfinder.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tal.placesfinder.Fragments.PlacesListFragment;
import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.Network.ApiManager;
import com.tal.placesfinder.Network.Utility;
import com.tal.placesfinder.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlacesActivity extends BaseActivity implements PlacesListFragment.PlacesListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    GoogleMap map;
    double latitude;
    double longitude;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    PlacesListFragment placesListFragment;
    SupportMapFragment mapFragment;
    Place placeOnMap;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private ProgressDialog progressDialog;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        initDrawer();

        placesListFragment = new PlacesListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, placesListFragment).commit();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        } else {
            isTablet = true;
        }

        buildGoogleApiClient();

        Utility.requstlocationPremission(PlacesActivity.this);
    }

    private void initDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.favorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
        }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(PlacesActivity.this, "", "Loading...");
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }
        if (!isTablet) {
            createMarker(placeOnMap);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if (mLastLocation != null && placesListFragment.isNetworkAvailable()) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
                searchByLocation();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void putMarkerInMap(Place place) {
        placeOnMap = place;
        mapFragment.getMapAsync(this);
        if (!isTablet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            createMarker(place);
        }
    }

    private void createMarker(Place place){
        double lat = Double.parseDouble(place.getLatitude());
        double lng = Double.parseDouble(place.getLongitude());
        String placeName = place.getName();
        String vicinity = place.getVicinity();
        LatLng latLng = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions()
        .position(latLng)
        .title(placeName + " : " + vicinity)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void nearbySearch() {
        searchByLocation();
    }

    private  void searchByLocation() {
        String latLng = (latitude + "," + longitude).toString();
        new searchNearbyPlaces().execute(latLng);
    }

    @Override
    public void textSearch(String input) {
        new textsearchPlaces().execute(input);
    }

    public void setDistance(Place place) {
        LatLng latLng1 = new LatLng(latitude, longitude);
        double lat = Double.parseDouble(place.getLatitude());
        double lng = Double.parseDouble(place.getLongitude());
        LatLng latLng2 = new LatLng(lat, lng);
        place.setDistance(latLng1, latLng2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PREMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        if (map != null) {
                            map.setMyLocationEnabled(true);
                        }
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void sortByDistance(List<Place> places) {
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place place1, Place place2) {
                return place1.getDistance().compareTo(place2.getDistance());
            }
        });
    }

    private void setBeforeAdapter(List<Place> places) {
        for(Place place : places){
            setDistance(place);
        }
        sortByDistance(places);
        placesListFragment.setPlacesAdapter(places);
    }

    public class textsearchPlaces extends AsyncTask<String, Void, List<Place>> {

        @Override
        protected void onPreExecute() {
            if (placesListFragment.isNetworkAvailable()) {
                showProgressDialog();
            }
        }

        @Override
        protected List<Place> doInBackground(String... strings) {
            return ApiManager.textSearch(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            setBeforeAdapter(places);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public class searchNearbyPlaces extends AsyncTask<String, Void, List<Place>> {

        @Override
        protected void onPreExecute() {
            if (placesListFragment.isNetworkAvailable()) {
                showProgressDialog();
            }
        }

        @Override
        protected List<Place> doInBackground(String... strings) {
            return ApiManager.nearbySearch(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            setBeforeAdapter(places);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
