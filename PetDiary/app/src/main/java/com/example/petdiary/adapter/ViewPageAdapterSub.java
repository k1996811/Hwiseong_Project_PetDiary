package com.example.petdiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.petdiary.R;

import java.util.ArrayList;

public class ViewPageAdapterSub extends PagerAdapter {

    private int[] images = {R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog};
    private ArrayList<String> imagess = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;

    public ViewPageAdapterSub(String uri1, Context context){
        if(uri1.length() > 0 ){
            imagess.add(uri1);
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
        View v = inflater.inflate(R.layout.slider_sub, container, false);
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


