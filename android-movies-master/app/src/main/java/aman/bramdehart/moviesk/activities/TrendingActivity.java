package aman.bramdehart.moviesk.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

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
 * Trending activity.
 * The startup activity that show the current trending movies.
 */
public class TrendingActivity extends AppCompatActivity {
    private TextView tvErrorMessage;
    private ProgressBar pbLoadingIndicator;
    private RecyclerView rvMovieList;
    private ArrayList<Movie> movies=new ArrayList<>();
    MovieRecyclerViewAdapter rvAdapter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    return true;
                case R.id.navigation_search:
                    Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                case R.id.navigation_favorites:
                    Intent favoritesIntent = new Intent(getApplicationContext(), FavoritesActivity.class);
                    startActivity(favoritesIntent);
                    return true;
            }
            return false;
        }
    };
    InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        MobileAds.initialize(this,
                "ca-app-pub-3136435737091654~2057566095");
        interstitialAd = new InterstitialAd(TrendingActivity.this);

        interstitialAd.setAdUnitId("ca-app-pub-3136435737091654/1059979507");
        // Create ad request.
        AdRequest adRequests = new AdRequest.Builder().build();

        interstitialAd.loadAd(adRequests);

  //      mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
    //    mAdView.loadAd(adRequest);

        // Use an activity context to get the rewarded video instance.
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set bottombar active item
        navigation.setSelectedItemId(R.id.navigation_trending);

        rvMovieList = findViewById(R.id.rv_movie_list);
        rvMovieList.setNestedScrollingEnabled(false);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(TrendingActivity.this, 3);
        rvMovieList.setLayoutManager(gridLayoutManager);

        rvAdapter = new MovieRecyclerViewAdapter(getApplicationContext(), movies);

        // Starts the query
        makeTMDBTrendingQuery();
    }

    /**
     * Makes the query to get current trending movies.
     */
    private void makeTMDBTrendingQuery() {
        URL TMDBTrendingURL = NetworkUtils.buildTrendingUrl();
        new TMDBQueryTask().execute(TMDBTrendingURL);
    }

    /**
     * Shows the recyclerview.
     */
    private void showRecyclerView() {
        tvErrorMessage.setVisibility(View.INVISIBLE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the error message.
     */
    private void showErrorMessage() {
        rvMovieList.setVisibility(View.INVISIBLE);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * Parses the movies JSON string and stores them into the movie array.
     *
     * @param moviesJSONString
     */
    private void parseMovies(String moviesJSONString) throws JSONException {
        JSONObject resultJSONObject = new JSONObject(moviesJSONString);
        JSONArray moviesJSONArray = resultJSONObject.getJSONArray("results");

        // Loop throught the JSON array results
        for (int i = 0; i < moviesJSONArray.length(); i++) {
            JSONObject movieJSONObject = new JSONObject(moviesJSONArray.get(i).toString());
            if (!movieJSONObject.isNull("poster_path")) {
                String posterPath = movieJSONObject.getString("poster_path");
                int movieId = movieJSONObject.getInt("id");

                // Add new movie object to the movie array
                movies.add(new Movie(movieId, posterPath));
            }
        }
    }

    /**
     * Populates the recyclerview with the retrieved movies.
     */
    private void populateRecyclerView() {
    rvAdapter.notifyDataSetChanged();
        // Decide the number of columns based on the screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int spanCount = 2;
        if (width > 1400) {
            spanCount = 5;
        } else if (width > 700) {
            spanCount = 3;
        }

        rvMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), spanCount));
        rvMovieList.setAdapter(rvAdapter);
    }

    /**
     * Inner class that takes care of the query task.
     */
    public class TMDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String TMDBTrendingResults = null;
            try {
                TMDBTrendingResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return TMDBTrendingResults;
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

                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        }
    }

    public void startAds(){
        // Set the AdListener.
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                if (interstitialAd.isLoaded()) {

                    interstitialAd.show();
                }


            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }
        });
    }
}
