package aman.bramdehart.moviesL.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aman.bramdehart.moviesL.R;
import aman.bramdehart.moviesL.models.Trailer;
import aman.bramdehart.moviesL.network.URLConstants;

/**
 * Created by KeshavAggarwal on 19/02/17.
 */

public class RecyclerAdapterMovieTrailer extends RecyclerView.Adapter<RecyclerAdapterMovieTrailer.ViewHolder> {
    Context mContext;
    private ArrayList<Trailer> mTrailerMoviesArraylist;

    public RecyclerAdapterMovieTrailer(ArrayList<Trailer> trailerMovies, Context context) {
        mTrailerMoviesArraylist = trailerMovies;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_thumbnail_image_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mTrailerMoviesArraylist != null) {
            Picasso.get().load(URLConstants.TRAILER_THUMBNAIL_IMAGE_URL + mTrailerMoviesArraylist.get(position).getKey() + "/hqdefault.jpg").into(holder.trailerThumbnail);
            holder.trailerThumbnailName.setText(mTrailerMoviesArraylist.get(position).getName());

            holder.trailerCardView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URLConstants.YOUTUBE_WATCH_BASE_URL + mTrailerMoviesArraylist.get(position).getKey()));
                mContext.startActivity(i);
            });
        }

    }

    @Override
    public int getItemCount() {
        return mTrailerMoviesArraylist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView trailerCardView;
        ImageView trailerThumbnail;
        TextView trailerThumbnailName;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerThumbnail = itemView.findViewById(R.id.trailerThumbnail);
            trailerCardView = itemView.findViewById(R.id.trailerCardView);
            trailerThumbnailName = itemView.findViewById(R.id.trailerThumbnailName);
        }
    }
}
