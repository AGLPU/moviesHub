package aman.bramdehart.moviesL.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aman.bramdehart.moviesL.models.Movie;
import aman.bramdehart.moviesL.R;
import aman.bramdehart.moviesL.models.Trailer;
import aman.bramdehart.moviesL.adapters.RecyclerAdapterMovieTrailer;
import aman.bramdehart.moviesL.adapters.RecyclerAdapterSimilarMovies;
import aman.bramdehart.moviesL.utils.AppUtil;
import aman.bramdehart.moviesL.utils.HorizontalItemDecoration;

import java.util.ArrayList;

/**
 * Created by KeshavAggarwal on 24/01/17.
 */

public class InfoAboutMovieFragment extends Fragment {

    private TextView abouFilmTextView;
    private TextView releasedTextView;
    private TextView budgetTextView;
    private TextView seeAlltextViewMovieInfofragment;
    private TextView noReviewMovieTextView;
    private TextView noSimilarMoviesTextView;
    private TextView revenueTextView;
    private RecyclerView trailorsRecyclerView;
    private RecyclerView similarMoviesRecyclerView;
    private RecyclerAdapterMovieTrailer recyclerAdapterMovieTrailer;
    private RecyclerAdapterSimilarMovies recyclerAdapterSimilarMovies;
    private Context context;
    private ArrayList<Movie> mainSimilarMovies;
    private ArrayList<Trailer> mainTrailerMoviesThumbnails;
    private InfoAboutMovieFragmentListener infoAboutMovieFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        infoAboutMovieFragmentListener = (InfoAboutMovieFragmentListener) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface InfoAboutMovieFragmentListener {
        void onSeeAllSimilarMoviesClicked();
    }

    public void setInfoAboutMovieFragmentListener(InfoAboutMovieFragmentListener listener) {
        this.infoAboutMovieFragmentListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View v = inflater.inflate(R.layout.fragment_info_movie, container, false);
        abouFilmTextView = v.findViewById(R.id.aboutFilmTextView);
        releasedTextView = v.findViewById(R.id.releasedTextView);
        budgetTextView = v.findViewById(R.id.budgetTextView);
        noReviewMovieTextView = v.findViewById(R.id.noReviewMovieTextView);
        revenueTextView = v.findViewById(R.id.revenueTextView);
        noSimilarMoviesTextView = v.findViewById(R.id.noSimilarMoviesTextView);
        trailorsRecyclerView = v.findViewById(R.id.trailorsRecyclerView);
        similarMoviesRecyclerView = v.findViewById(R.id.similarMoviesRecyclerView);
        seeAlltextViewMovieInfofragment = v.findViewById(R.id.seeAllTextViewMovieInfoFragment);
        seeAlltextViewMovieInfofragment.setOnClickListener(v1 -> infoAboutMovieFragmentListener.onSeeAllSimilarMoviesClicked());
        return v;
    }


    public static InfoAboutMovieFragment newInstance() {
        return new InfoAboutMovieFragment();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setUIArguements(final Bundle args) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (args.getBoolean("INFO")) {
                    abouFilmTextView.setText(args.getString("OVERVIEW"));
                    String releaseDate = dateGenerator(args.getString("RELEASE_DATE"));
                    releasedTextView.setText(releaseDate);
                    String budget = "£" + String.valueOf(args.getLong("BUDGET"));
                    budgetTextView.setText(budget);
                    String revenue = "£" + String.valueOf(args.getLong("REVENUE"));
                    revenueTextView.setText(revenue);
                    mainTrailerMoviesThumbnails = (ArrayList<Trailer>) args.getSerializable("TRAILER_THUMBNAILS");
                    if (mainTrailerMoviesThumbnails.size() == 0) {
                        noReviewMovieTextView.setVisibility(View.VISIBLE);
                        noReviewMovieTextView.setText("No Trailers are currently available.");
                    } else {
                        recyclerAdapterMovieTrailer = new RecyclerAdapterMovieTrailer(mainTrailerMoviesThumbnails, context);
                        LinearLayoutManager HorizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        trailorsRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(context, 16), AppUtil.dpToPx(context, 6), AppUtil.dpToPx(context, 16)));
                        trailorsRecyclerView.setLayoutManager(HorizontalManager);
                        trailorsRecyclerView.setAdapter(recyclerAdapterMovieTrailer);
                    }

                } else if (args.getBoolean("SIMILAR")) {
                    mainSimilarMovies = (ArrayList<Movie>) args.getSerializable("SIMILAR_MOVIES");
                    if (mainSimilarMovies.size() == 0) {
                        noSimilarMoviesTextView.setVisibility(View.VISIBLE);
                        noSimilarMoviesTextView.setText("No Similar Movies are currently available.");
                    } else {
                        recyclerAdapterSimilarMovies = new RecyclerAdapterSimilarMovies(mainSimilarMovies, context);
                        LinearLayoutManager HorizontalManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        similarMoviesRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(context, 16), AppUtil.dpToPx(context, 6), AppUtil.dpToPx(context, 16)));
                        similarMoviesRecyclerView.setLayoutManager(HorizontalManager1);
                        similarMoviesRecyclerView.setAdapter(recyclerAdapterSimilarMovies);
                    }
                }

            }


        });
    }

    private String dateGenerator(String date) {
        if (date.length() == 9 || date.length() == 10) {
            String month = date.substring(5, 7);
            String ans = "";
            switch (month) {
                case "01":
                    ans = "January";
                    break;
                case "02":
                    ans = "February";
                    break;
                case "03":
                    ans = "March";
                    break;
                case "04":
                    ans = "April";
                    break;
                case "05":
                    ans = "May";
                    break;
                case "06":
                    ans = "June";
                    break;
                case "07":
                    ans = "July";
                    break;
                case "08":
                    ans = "August";
                    break;
                case "09":
                    ans = "September";
                    break;
                case "10":
                    ans = "October";
                    break;
                case "11":
                    ans = "November";
                    break;
                case "12":
                    ans = "December";
                    break;

            }
            return (ans + " " + date.substring(8, 10) + "," + " " + date.substring(0, 4));
        } else
            return "NA";
    }
}
