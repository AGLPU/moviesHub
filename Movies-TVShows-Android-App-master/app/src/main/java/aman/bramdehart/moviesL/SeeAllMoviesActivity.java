package aman.bramdehart.moviesL;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AudienceNetworkAds;

import java.util.ArrayList;

import aman.bramdehart.moviesL.adapters.RecyclerViewAdapterSeeAllActivity;
import aman.bramdehart.moviesL.models.Movie;
import aman.bramdehart.moviesL.network.ApiService;
import aman.bramdehart.moviesL.network.MovieResponse;
import aman.bramdehart.moviesL.network.URLConstants;
import aman.bramdehart.moviesL.utils.Adsbuilder;
import aman.bramdehart.moviesL.utils.AppUtil;
import aman.bramdehart.moviesL.utils.EndlessRecyclerViewScrollListener;
import aman.bramdehart.moviesL.utils.GridSpacingItemDecoration;
import aman.bramdehart.moviesL.utils.SpacesItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeeAllMoviesActivity extends AppCompatActivity {
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView recyclerView;
    RecyclerViewAdapterSeeAllActivity adapter;
    ArrayList<Movie> movies;
    String movieType;

    Adsbuilder adsbuilder;

    private void setupFBAds() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);


        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adsbuilder = new Adsbuilder(this, adContainer);
        adsbuilder.buildAdsListner();
        adsbuilder.loadAds();
        adsbuilder.loadBannerAds();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity_movie);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Slide slide = new Slide(Gravity.BOTTOM);
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);

        setupFBAds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        movies = (ArrayList<Movie>) intent.getSerializableExtra("ABCD");
        movieType = intent.getStringExtra("MOVIETYPE");

        setTitle(movieType);

        recyclerView = findViewById(R.id.seeAllActivityRecyclerViewMovies);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        adapter = new RecyclerViewAdapterSeeAllActivity(movies, this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, AppUtil.dpToPx(this, 16), true));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    private void loadMoreData(int page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLConstants.MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        if (movieType.equals("Popular Movies")) {

            Call<MovieResponse> call = service.getPopularMovies(URLConstants.API_KEY, page);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    //Log.i("ABC2", "FUN");
                    ArrayList<Movie> movieList = response.body().getMovies();
                    if (movieList == null) {
                        return;
                    }
                    for (Movie obj : movieList) {
                        movies.add(obj);
                    }
                    adapter.notifyDataSetChanged();
                    adsbuilder.showAds();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        } else if (movieType.equals("Now Playing")) {
            Call<MovieResponse> call = service.getNowPlayingMovies(URLConstants.API_KEY, page);

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    ArrayList<Movie> movieList = response.body().getMovies();
                    if (movieList == null) {
                        return;
                    }
                    for (Movie obj : movieList) {
                        movies.add(obj);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        } else if (movieType.equals("Top Rated Movies")) {
            Call<MovieResponse> call = service.getTopRatedMovies(URLConstants.API_KEY, page);

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    //Log.i("ABC2", "FUN");
                    ArrayList<Movie> movieList = response.body().getMovies();
                    if (movieList == null) {
                        return;
                    }
                    for (Movie obj : movieList) {
                        movies.add(obj);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        } else if (movieType.equals("Upcoming Movies")) {
            Call<MovieResponse> call = service.getUpcomingMovies(URLConstants.API_KEY, page);

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    ArrayList<Movie> movieList = response.body().getMovies();
                    if (movieList == null) {
                        return;
                    }
                    for (Movie obj : movieList) {
                        movies.add(obj);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onDestroy() {
        adsbuilder.destroyInterstialAds();
        adsbuilder.destroyBannerAds();
        super.onDestroy();
    }

}
