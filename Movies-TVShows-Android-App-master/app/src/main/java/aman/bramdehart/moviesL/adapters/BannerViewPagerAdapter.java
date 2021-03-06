package aman.bramdehart.moviesL.adapters;

import android.content.Context;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import aman.bramdehart.moviesL.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KeshavAggarwal on 19/01/17.
 */

public class BannerViewPagerAdapter extends PagerAdapter {

    Context mContext;
    private List<String> mAllBannerImageFullLinks;

    public BannerViewPagerAdapter(Context context, ArrayList<String> allBannerImageFullLinks) {
        mContext = context;
        mAllBannerImageFullLinks = allBannerImageFullLinks;
    }

    @Override
    public int getCount() {
        return mAllBannerImageFullLinks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.banner_image_view_layout, container, false);
        ImageView bannerImage = v.findViewById(R.id.bannerImage);
        Picasso.get().load(mAllBannerImageFullLinks.get(position)).into(bannerImage);
        container.addView(v);
        return v;
    }

    public void refreshBannerUrls(List<String> list) {
        this.mAllBannerImageFullLinks = list;
        notifyDataSetChanged();
    }
}

