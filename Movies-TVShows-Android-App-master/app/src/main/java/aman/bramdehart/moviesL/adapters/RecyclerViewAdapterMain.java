package aman.bramdehart.moviesL.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aman.bramdehart.moviesL.AboutMovieActivity;
import aman.bramdehart.moviesL.utils.AppUtil;
import aman.bramdehart.moviesL.utils.HorizontalItemDecoration;
import aman.bramdehart.moviesL.utils.IntentConstants;
import aman.bramdehart.moviesL.OnRecyclerViewItemClickListener;
import aman.bramdehart.moviesL.R;
import aman.bramdehart.moviesL.SeeAllMoviesActivity;
import aman.bramdehart.moviesL.network.MovieResponse;

/**
 * Created by KeshavAggarwal on 07/01/17.
 */

public class RecyclerViewAdapterMain extends RecyclerView.Adapter<RecyclerViewAdapterMain.ViewHolder> implements OnRecyclerViewItemClickListener {

    private MovieResponse[] mMovies;
    Context mContext;
    private RecyclerViewAdapter recyclerViewAdapter;


    public RecyclerViewAdapterMain(MovieResponse[] movies, Context context) {
        mMovies = movies;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_second, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mMovies != null && mMovies.length > position) {
            if (getItemViewType(position) == 0) {
                if (mMovies[position] != null) {
                    holder.movieType.setText("Popular Movies");
                    holder.seeAlltextView.setText("See all");
                    holder.seeAlltextView.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
                        intent.putExtra("ABCD", mMovies[position].getMovies());
                        intent.putExtra("MOVIETYPE", "Popular Movies");
                        intent.setClass(mContext, SeeAllMoviesActivity.class);
                        mContext.startActivity(intent, bundle);
                    });
                    recyclerViewAdapter = new RecyclerViewAdapter(mMovies[position].getMovies(), mContext);
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
                    holder.horizontalRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2)));
                    holder.horizontalRecyclerView.setLayoutManager(horizontalManager);
                    holder.horizontalRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.setOnItemClickListener(this, position);
                }

            } else if (getItemViewType(position) == 1) {
                if (mMovies[position] != null) {
                    holder.movieType.setText("Now Playing");
                    holder.seeAlltextView.setText("See all");
                    holder.seeAlltextView.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
                        intent.setClass(mContext, SeeAllMoviesActivity.class);
                        intent.putExtra("ABCD", mMovies[position].getMovies());
                        intent.putExtra("MOVIETYPE", "Now Playing");
                        mContext.startActivity(intent, bundle);

                    });
                    recyclerViewAdapter = new RecyclerViewAdapter(mMovies[position].getMovies(), mContext);
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    holder.horizontalRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2)));
                    holder.horizontalRecyclerView.setLayoutManager(horizontalManager);
                    holder.horizontalRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.setOnItemClickListener(this, position);
                }

            } else if (getItemViewType(position) == 2) {
                if (mMovies[position] != null) {
                    holder.movieType.setText("Top Rated Movies");
                    holder.seeAlltextView.setText("See all");
                    holder.seeAlltextView.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
                        intent.putExtra("ABCD", mMovies[position].getMovies());
                        intent.putExtra("MOVIETYPE", "Top Rated Movies");
                        intent.setClass(mContext, SeeAllMoviesActivity.class);
                        mContext.startActivity(intent, bundle);

                    });
                    recyclerViewAdapter = new RecyclerViewAdapter(mMovies[position].getMovies(), mContext);
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    holder.horizontalRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2)));
                    holder.horizontalRecyclerView.setLayoutManager(horizontalManager);
                    holder.horizontalRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.setOnItemClickListener(this, position);
                }
            } else if (getItemViewType(position) == 3) {
                if (mMovies[position] != null) {
                    holder.movieType.setText("Upcoming Movies");
                    holder.seeAlltextView.setText("See all");
                    holder.seeAlltextView.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
                        intent.putExtra("ABCD", mMovies[position].getMovies());
                        intent.putExtra("MOVIETYPE", "Upcoming Movies");
                        intent.setClass(mContext, SeeAllMoviesActivity.class);
                        mContext.startActivity(intent, bundle);

                    });
                    recyclerViewAdapter = new RecyclerViewAdapter(mMovies[position].getMovies(), mContext);
                    LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                    holder.horizontalRecyclerView.addItemDecoration(new HorizontalItemDecoration(AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2), AppUtil.dpToPx(mContext, 2)));
                    holder.horizontalRecyclerView.setLayoutManager(horizontalManager);
                    holder.horizontalRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.setOnItemClickListener(this, position);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }


    @Override
    public int getItemViewType(int position) {
        return position % 4;
    }


    @Override
    public void onRecyclerViewItemClicked(int verticalPosition, int horizontalPosition, View view) {
        Intent intent = new Intent();
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, view.getTransitionName()).toBundle();
        //Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle();
        intent.setClass(mContext, AboutMovieActivity.class);
        intent.putExtra(IntentConstants.INTENT_KEY_MOVIE_ID, mMovies[verticalPosition].getMovies().get(horizontalPosition).getId());
        intent.putExtra(IntentConstants.INTENT_KEY_POSTER_PATH, mMovies[verticalPosition].getMovies().get(horizontalPosition).getPosterPath());
        intent.putExtra(IntentConstants.INTENT_KEY_MOVIE_NAME, mMovies[verticalPosition].getMovies().get(horizontalPosition).getTitle());
        mContext.startActivity(intent, bundle);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieType;
        RecyclerView horizontalRecyclerView;
        TextView seeAlltextView;

        public ViewHolder(View itemView) {
            super(itemView);
            movieType = itemView.findViewById(R.id.movieTypeTextView);
            seeAlltextView = itemView.findViewById(R.id.seeAllTextView);
            horizontalRecyclerView = itemView.findViewById(R.id.activityMainRecyclerViewHorizontal);
        }
    }


}