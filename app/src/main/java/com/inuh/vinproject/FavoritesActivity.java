package com.inuh.vinproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.inuh.vinproject.util.PrefManager;

/**
 * Created by artimus on 23.05.16.
 */
public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.favorites_fragment_container);

        if(fragment == null){
            fragment = new FavoritesCatalogFragment();
            fm.beginTransaction()
                    .add(R.id.favorites_fragment_container, fragment)
                    .commit();
        }

    }

    public static class FavoritesCatalogFragment extends CatalogFragment{

        public FavoritesCatalogFragment(){
            super();
        }

        @Override
        public String getWhereClause() {
            String whereClause = "objectId in (" + PrefManager.getInstance(getContext()).getFavoritesNovelsString() + ")";
            return whereClause;
        }
    }
}
