package aman.bramdehart.moviesL.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import aman.bramdehart.moviesL.AboutTVShowActivity;
import aman.bramdehart.moviesL.utils.IntentConstants;
import aman.bramdehart.moviesL.R;
import aman.bramdehart.moviesL.models.TVShow;
import aman.bramdehart.moviesL.network.URLConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KeshavAggarwal on 31/03/17.
 */

public class RecyclerAdapterSearchTvShows extends RecyclerView.Adapter<RecyclerAdapterSearchTvShows.ViewHolder> {
    Context mContext;
    private ArrayList<TVShow> mTvShows;

    public RecyclerAdapterSearchTvShows(ArrayList<TVShow> searchedShows, Context context) {
        mContext = context;
        mTvShows = searchedShows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_tvshow_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mTvShows != null) {
            holder.name.setText(mTvShows.get(position).getTitle());
            Picasso.get().load(URLConstants.IMAGE_BASE_URL + mTvShows.get(position).getPosterPath()).into(holder.thumbnailImage);
            if (mTvShows.get(position).getDate().length() >= 5) {
                String date = mTvShows.get(position).getDate().substring(0, 4);
                holder.releaseDate.setText(date);
            }
            String rating = Double.toString(mTvShows.get(position).getRating());
            holder.rating.setText(rating);
            holder.cv.setOnClickListener(v -> {
                Intent intent = new Intent();
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.thumbnailImage, holder.thumbnailImage.getTransitionName()).toBundle();
                intent.setClass(mContext, AboutTVShowActivity.class);
                intent.putExtra(IntentConstants.INTENT_KEY_TVSHOW_ID, mTvShows.get(position).getId());
                intent.putExtra(IntentConstants.INTENT_KEY_POSTER_PATH, mTvShows.get(position).getPosterPath());
                intent.putExtra(IntentConstants.INTENT_KEY_TVSHOW_NAME, mTvShows.get(position).getTitle());
                mContext.startActivity(intent, bundle);
            });
        }

    }

    @Override
    public int getItemCount() {
        return mTvShows.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView thumbnailImage;
        TextView name;
        TextView releaseDate;
        TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            thumbnailImage = itemView.findViewById(R.id.thumbnailImageView);
            name = itemView.findViewById(R.id.nameTextView);
            releaseDate = itemView.findViewById(R.id.releaseDateTextView);
            rating = itemView.findViewById(R.id.ratingTextView);
        }
    }
}
