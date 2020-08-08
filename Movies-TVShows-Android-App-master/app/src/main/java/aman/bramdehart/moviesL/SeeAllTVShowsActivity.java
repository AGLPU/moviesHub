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

import aman.bramdehart.moviesL.adapters.RecyclerAdapterSeeAllTvshows;
import aman.bramdehart.moviesL.models.TVShow;
import aman.bramdehart.moviesL.network.ApiService;
import aman.bramdehart.moviesL.network.TVShowResponse;
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

public class SeeAllTVShowsActivity extends AppCompatActivity {

    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView recyclerView;
    RecyclerAdapterSeeAllTvshows recyclerAdapterSeeAllTvshows;
    ArrayList<TVShow> tvShows;
    String tvShowType;

    Adsbuilder adsbuilder;

    @Override
    protected void onDestroy() {
        adsbuilder.destroyInterstialAds();
        adsbuilder.destroyBannerAds();
        super.onDestroy();
    }
    private void setupFBAds() {
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);


        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adsbuilder=new Adsbuilder(this,adContainer);
        adsbuilder.buildAdsListner();
        adsbuilder.loadAds();
        adsbuilder.loadBannerAds();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_activity_tvshows);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Slide slide = new Slide(Gravity.BOTTOM);
        getWindow().setEnterTransition(slide);
        getWindow().setAllowEnterTransitionOverlap(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupFBAds();
        Intent intent = getIntent();
        tvShows = (ArrayList<TVShow>) intent.getSerializableExtra("ABCD");
        tvShowType = intent.getStringExtra("TVSHOW_TYPE");

        setTitle(tvShowType);

        recyclerView = findViewById(R.id.seeAllActivityRecyclerViewTVShows);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        recyclerAdapterSeeAllTvshows = new RecyclerAdapterSeeAllTvshows(tvShows, this);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, AppUtil.dpToPx(this, 16), true));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerAdapterSeeAllTvshows);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadmoreData(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    private void loadmoreData(int page) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLConstants.TVSHOW_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        if (tvShowType.equals("Airing Today")) {

            Call<TVShowResponse> call = service.getAiringToday(URLConstants.API_KEY, page);
            call.enqueue(new Callback<TVShowResponse>() {
                @Override
                public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                    //Log.i("ABC2", "FUN");
                    ArrayList<TVShow> tvShowList = response.body().getTvShows();
                    if (tvShowList == null) {
                        return;
                    }
                    for (TVShow obj : tvShowList) {
                        tvShows.add(obj);
                    }
                    recyclerAdapterSeeAllTvshows.notifyDataSetChanged();
                    adsbuilder.showAds();
                }

                @Override
                public void onFailure(Call<TVShowResponse> call, Throwable t) {

                }
            });
        } else if (tvShowType.equals("On Air")) {
            Call<TVShowResponse> call = service.getOnAir(URLConstants.API_KEY, page);

            call.enqueue(new Callback<TVShowResponse>() {
                @Override
                public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                    ArrayList<TVShow> tvShowList = response.body().getTvShows();
                    if (tvShowList == null) {
                        return;
                    }
                    for (TVShow obj : tvShowList) {
                        tvShows.add(obj);
                    }
                    recyclerAdapterSeeAllTvshows.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TVShowResponse> call, Throwable t) {

                }
            });
        } else if (tvShowType.equals("Popular Shows")) {
            Call<TVShowResponse> call = service.getPopular(URLConstants.API_KEY, page);

            call.enqueue(new Callback<TVShowResponse>() {
                @Override
                public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                    //Log.i("ABC2", "FUN");
                    ArrayList<TVShow> tvShowList = response.body().getTvShows();
                    if (tvShowList == null) {
                        return;
                    }
                    for (TVShow obj : tvShowList) {
                        tvShows.add(obj);
                    }
                    recyclerAdapterSeeAllTvshows.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TVShowResponse> call, Throwable t) {

                }
            });
        } else if (tvShowType.equals("Top Rated Shows")) {
            Call<TVShowResponse> call = service.getTopRated(URLConstants.API_KEY, page);

            call.enqueue(new Callback<TVShowResponse>() {
                @Override
                public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                    //Log.i("ABC2", "FUN");
                    ArrayList<TVShow> tvShowList = response.body().getTvShows();
                    if (tvShowList == null) {
                        return;
                    }
                    for (TVShow obj : tvShowList) {
                        tvShows.add(obj);
                    }
                    recyclerAdapterSeeAllTvshows.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<TVShowResponse> call, Throwable t) {

                }
            });

        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}





