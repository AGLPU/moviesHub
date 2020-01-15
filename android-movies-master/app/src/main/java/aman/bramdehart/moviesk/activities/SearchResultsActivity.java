package aman.bramdehart.moviesk.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import aman.bramdehart.moviesk.adapters.MovieRecyclerViewAdapter;
import aman.bramdehart.moviesk.data.NetworkUtils;
import aman.bramdehart.moviesk.R;
import aman.bramdehart.moviesk.models.Movie;

/**
 * Search results activity.
 * Used to display movie search results.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private String searchQuery;
    private TextView tvErrorMessage;
    private TextView tvResultsTitle;
    private ProgressBar pbLoadingIndicator;
    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies;

    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);





        // Bind components
        tvErrorMessage = findViewById(R.id.tv_error_message);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        tvResultsTitle = findViewById(R.id.tv_results_title);

        rvMovieList = findViewById(R.id.rv_movie_list);
        rvMovieList.setNestedScrollingEnabled(false);

        // Get received data from previous intent
        Intent intent = getIntent();
        searchQuery = intent.getExtras().getString("SearchQuery");

        // Start search query
        URL TMDBSearchURL = NetworkUtils.buildSearchUrl(searchQuery);
        new SearchResultsActivity.TMDBQueryTask().execute(TMDBSearchURL);
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TMDBSearchResults = null;
            try {
                TMDBSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return TMDBSearchResults;

        }

        /**
         * Executes when the API call is finished.
         */
        @Override
        protected void onPostExecute(String s) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (s != null && !s.equals("")) {
                showRecyclerView();
                try {
                    parseMovies(s);
                    populateRecyclerView();
                    interstitialAd = new InterstitialAd(SearchResultsActivity.this);
                    interstitialAd.setAdUnitId("ca-app-pub-3259229841451623/7856401616");

                    // Create ad request.
                    AdRequest adRequests = new AdRequest.Builder().build();

                    interstitialAd.loadAd(adRequests);

                    // Set the AdListener.
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Log.d(LOG_TAG, "onAdLoaded");

                            if (interstitialAd.isLoaded()) {
                                interstitialAd.show();
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    }

    /**
     * Parses the movie JSON string and inserts the results as a new Movie object into the Movie array.
     *
     * @param moviesJSONString
     * The JSON string containing the results.
     */
    private void parseMovies(String moviesJSONString) throws JSONException {
        JSONObject resultJSONObject = new JSONObject(moviesJSONString);
        JSONArray moviesJSONArray = resultJSONObject.getJSONArray("results");
        movies = new ArrayList<Movie>();

        // Loop through json array
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            JSONObject movieJSONObject = new JSONObject(moviesJSONArray.get(i).toString());
            if (!movieJSONObject.isNull("poster_path")) {
                int movieId = movieJSONObject.getInt("id");
                String posterPath = movieJSONObject.getString("poster_path");

                // Add movie object
                movies.add(new Movie(movieId, posterPath));
            }
        }

        // Set result count
        tvResultsTitle.setText(movies.size() + " " +  getResources().getString(R.string.results_heading) + " '" + searchQuery + "'");
    }

    /**
     * Populates the recyclerview with the retrieved movies.
     */
    private void populateRecyclerView() {
        MovieRecyclerViewAdapter rvAdapter = new MovieRecyclerViewAdapter(getApplicationContext(), movies);
        rvMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvMovieList.setAdapter(rvAdapter);
    }

    /**
     * Show recycler view.
     */
    private void showRecyclerView() {
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    /**
     * Show error message.
     */
    private void showErrorMessage() {
        rvMovieList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
}
