package com.example.petdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.petdiary.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {

    private int[] images = {R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog};
    private ArrayList<String> imagess = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;

    public ViewPageAdapter(String uri1, String uri2, String uri3, String uri4, String uri5, Context context){
        if(uri1.length() > 0 ){
            imagess.add(uri1);
        }
        if(uri2.length() > 0 ){
            imagess.add(uri2);
        }
        if(uri3.length() > 0 ){
            imagess.add(uri3);
        }
        if(uri4.length() > 0 ){
            imagess.add(uri4);
        }
        if(uri5.length() > 0 ){
            imagess.add(uri5);
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        //return images.length;
        return imagess.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

        //imageView.setImageResource(images[position]);
        Glide.with(context).load(imagess.get(position)).into(imageView);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}


