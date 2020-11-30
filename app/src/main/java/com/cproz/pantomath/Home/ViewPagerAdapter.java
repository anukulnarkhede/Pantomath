package com.cproz.pantomath.Home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jackandphantom.circularimageview.RoundedImage;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String[] imageUrls;
    private Intent intent;

    public ViewPagerAdapter(Context mContext, String[] imageUrls, Intent intent) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
        this.intent = intent;



    }


    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RoundedImage roundedImage = new RoundedImage(mContext);
        roundedImage.setRoundedRadius(20);
        Picasso.get()
                .load(imageUrls[position])
                .fit()
                .centerCrop()
                .into(roundedImage);
        container.addView(roundedImage);

        roundedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(intent);
            }
        });

        return roundedImage;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
