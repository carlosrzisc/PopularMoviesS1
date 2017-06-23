package com.example.popularmoviess1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.popularmoviess1.loaders.MoviesLoader;
import com.example.popularmoviess1.model.Movie;
import com.example.popularmoviess1.utilities.Utility;

import java.util.List;


/**
 * Movies fragment
 */
public class MoviesFragment extends Fragment {
    private final static int COLUMNS = 3;
    private final static String URL_POPULARITY = "popular";
    private final static String URL_RATING = "top_rated";
    private final static String PREF = "sort";

    private LoaderManager.LoaderCallbacks loaderCallbacks;
    private MoviesAdapter moviesAdapter;
    private SharedPreferences preferences;

    public MoviesFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        moviesAdapter = new MoviesAdapter(getActivity(), new MoviesAdapter.OnMovieClickListener() {
            @Override
            public void onClick(Movie movie) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsFragment.ARG_MOVIE, movie);
                startActivity(intent);
            }
        });

        loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Movie>>() {

            @Override
            public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
                String sortBy = preferences.getString(PREF, URL_POPULARITY);
                return new MoviesLoader(getActivity(), sortBy);
            }

            @Override
            public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
                moviesAdapter.setMovies(data);
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLoaderReset(Loader<List<Movie>> loader) {
                moviesAdapter.clear();
            }
        };
        if (Utility.hasInternetConnection(getActivity())) {
            getActivity().getSupportLoaderManager().initLoader(0, null, loaderCallbacks).forceLoad();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), COLUMNS));
        recyclerView.setAdapter(moviesAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.apply();
        switch (item.getItemId()) {
            case R.id.popularity: editor.putString(PREF, URL_POPULARITY); break;
            case R.id.rating: editor.putString(PREF, URL_RATING); break;
        }
        editor.apply();
        fetchMovies();
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies() {
        if (Utility.hasInternetConnection(getActivity())) {
            getActivity().getSupportLoaderManager().restartLoader(0, null, loaderCallbacks).forceLoad();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        String sortBy = preferences.getString(PREF, URL_POPULARITY);
        switch (sortBy) {
            case URL_POPULARITY: menu.findItem(R.id.popularity).setChecked(true); break;
            case URL_RATING: menu.findItem(R.id.rating).setChecked(true); break;
        }
    }
}
