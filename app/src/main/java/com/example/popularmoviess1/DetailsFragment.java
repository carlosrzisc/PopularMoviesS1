package com.example.popularmoviess1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviess1.model.Movie;
import com.squareup.picasso.Picasso;


/**
 * Movie Details fragment
 */
public class DetailsFragment extends Fragment {

    public static final String ARG_MOVIE = "param_movie";
    private Movie paramMovie;

    public DetailsFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Movie.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(Movie movie) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ImageView moviePoster = (ImageView) rootView.findViewById(R.id.details_image_poster);
        TextView movieTitle = (TextView) rootView.findViewById(R.id.details_movie_title);
        TextView movieRelease = (TextView) rootView.findViewById(R.id.details_movie_release);
        TextView movieRating = (TextView) rootView.findViewById(R.id.details_movie_rating);
        TextView movieOverview = (TextView) rootView.findViewById(R.id.details_movie_overview);

        Picasso.with(getActivity()).load(paramMovie.getPosterPath()).into(moviePoster);
        moviePoster.setContentDescription(paramMovie.getTitle());
        movieTitle.setText(paramMovie.getTitle());
        movieRelease.setText(String.format(getString(R.string.text_release_date), paramMovie.getReleaseDate()));
        movieRating.setText(String.format(getString(R.string.text_rating), paramMovie.getVoteAverage()));
        movieOverview.setText(paramMovie.getOverview());

        return rootView;
    }
}
