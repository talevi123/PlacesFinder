package com.tal.placesfinder.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tal.placesfinder.Adapters.ItemClickSupport;
import com.tal.placesfinder.Adapters.PlacesAdapter;
import com.tal.placesfinder.Adapters.SearchPlacesAdapter;
import com.tal.placesfinder.DB.DBManager;
import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tal on 05/12/16.
 */
public class PlacesListFragment extends Fragment {

   RecyclerView recyclerView;
   PlacesAdapter placesAdapter;
   PlacesListener placeSelectedListener;
   EditText searchView;
   Button nearbySearchBtn;
   SwipeRefreshLayout swipeRefreshLayout;
   SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        initItem(view);
        SearchByText(view);
        SearchNearby(view);
        swipeAndRefresh(view);

        String onSaveInstanceState = sharedPreferences.getString("onSaveInstanceState", null);
        if (onSaveInstanceState != null) {
            parseAndSwap(onSaveInstanceState);
        }

        if (!isNetworkAvailable()) {
            String placesJson = sharedPreferences.getString("Places", null);
            parseAndSwap(placesJson);
        }

        return view ;
    }

    @Override
    public void onPause() {
        super.onPause();
        String placesJson = sharedPreferences.getString("Places", null);
        sharedPreferences.edit().putString("onSaveInstanceState", placesJson).apply();
    }

    private void parseAndSwap(String placesJson) {
        List<Place> places = parseToPlaces(placesJson);
        placesAdapter.swap(places);
    }

    private List<Place> parseToPlaces (String jsonArr) {
        List<Place> places = new ArrayList<>();
        try {
            JSONArray jsonarray = new JSONArray(jsonArr);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.optJSONObject(i);
                if (jsonObject != null) {
                    Place place = new Place(jsonObject, 0);
                    places.add(place);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return places;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
        placesAdapter.notifyDataSetChanged();
    }

    public void setPlacesAdapter(List<Place> places) {
        if (places.isEmpty()) {
            Toast.makeText(getActivity(), "Sorry..we didnt find results", Toast.LENGTH_SHORT).show();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Place>>() {}.getType();
        String json = gson.toJson(places, type);
        sharedPreferences.edit().putString("Places", json).apply();
        placesAdapter.swap(places);
    }

    private void SearchByText(View view) {
        ImageButton searchBtn = (ImageButton) view.findViewById(R.id.search_btn);
        sharedPreferences.edit().putInt("btnType", R.id.search_btn).apply();
        searchView = (EditText) view.findViewById(R.id.searchview);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchView != null & searchView.getText() != null & searchView.getText().length() > 0) {
                        placeSelectedListener.textSearch(searchView.getText().toString());
                        searchView.setText("");
                        InputMethodManager inputManager =
                                (InputMethodManager) getActivity().
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(
                                getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } else {
                        Toast.makeText(getActivity(), "Please put place to search", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void SearchNearby(View view) {
        nearbySearchBtn = (Button) view.findViewById(R.id.nearby_btn);
        sharedPreferences.edit().putInt("btnType", R.id.nearby_btn).apply();
        nearbySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeSelectedListener.nearbySearch();
            }
        });
    }

    private void swipeAndRefresh(View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
               int btnType = sharedPreferences.getInt("btnType", R.id.nearby_btn);

               if (btnType == R.id.nearby_btn) {
                   placeSelectedListener.nearbySearch();
               }
               else if (btnType == R.id.search_btn) {
                   placeSelectedListener.textSearch(searchView.getText().toString());
               }
               swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initItem(final View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        List<Place> places = new ArrayList<>();
        placesAdapter = new SearchPlacesAdapter(getActivity(), places);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(placesAdapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Place place = placesAdapter.getItem(position);
                        placeSelectedListener.putMarkerInMap(place);
                    }
                }
        );

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(
                new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        final Place place = placesAdapter.getItem(position);
                        PopupMenu popup = new PopupMenu(getActivity(), v);
                        popup.inflate(R.menu.popup_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.shareMenu:

                                        break;
                                    case R.id.saveMenu:
                                        if(!DBManager.getInstance(getContext()).checkIfExsists(place.getName())) {
                                            DBManager.getInstance(getContext()).addToFav(place);
                                            Toast.makeText(getActivity(), "Add to favorites", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Place is exist in favorites", Toast.LENGTH_SHORT).show();
                                        }

                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                        return false;
                    }
                }
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            placeSelectedListener = (PlacesListener) context;
        }
    }

    public interface PlacesListener {
        void putMarkerInMap(Place place);

        void textSearch(String input);

        void nearbySearch();
    }

}