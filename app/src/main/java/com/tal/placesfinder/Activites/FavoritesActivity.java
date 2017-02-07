package com.tal.placesfinder.Activites;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.tal.placesfinder.Adapters.FavrotietsPlacesAdapter;
import com.tal.placesfinder.Adapters.PlacesAdapter;
import com.tal.placesfinder.DB.DBManager;
import com.tal.placesfinder.Moduls.Place;
import com.tal.placesfinder.R;

import java.util.List;

public class FavoritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        List<Place> placeList = DBManager.getInstance(this).getAllFav();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        PlacesAdapter placesAdapter = new FavrotietsPlacesAdapter(this, placeList);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(placesAdapter);
    }
}
