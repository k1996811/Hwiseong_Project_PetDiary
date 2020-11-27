package com.example.petdiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.petdiary.BookmarkInfo;
import com.example.petdiary.Data;
import com.example.petdiary.Expand_ImageView;
import com.example.petdiary.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {

    private DatabaseReference mReference;
    private FirebaseDatabase firebaseDatabase;

    private ArrayList<String> images = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context context;
    private Data arrayList;

    public ViewPageAdapter(Data arrayList, String url1, String url2, String url3, String url4, String url5, Context context){
        if(url1.length() > 0 ){
            images.add(url1);
        }
        if(url2.length() > 0 ){
            images.add(url2);
        }
        if(url3.length() > 0 ){
            images.add(url3);
        }
        if(url4.length() > 0 ){
            images.add(url4);
        }
        if(url5.length() > 0 ){
            images.add(url5);
        }

        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        //return images.length;
        return images.size();
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
        Glide.with(context).load(images.get(position)).centerCrop().override(1000).into(imageView);

        v.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                goPost(arrayList);
            }
        });

        container.addView(v);
        return v;
    }

    private void goPost(final Data arrayList){
        final Intent intent = new Intent(context, Expand_ImageView.class);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference("bookmark/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean chk = false;
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    BookmarkInfo bookmark = messageData.getValue(BookmarkInfo.class);
                    if(arrayList.getPostID().equals(bookmark.getPostID())){
                        chk = true;
                        break;
                    }
                }
                if(chk){
                    intent.putExtra("bookmark", "checked");
                } else {
                    intent.putExtra("bookmark", "unchecked");
                }
                intent.putExtra("postID", arrayList.getPostID());
                intent.putExtra("nickName", arrayList.getNickName());
                intent.putExtra("uid", arrayList.getUid());
                intent.putExtra("imageUrl1", arrayList.getImageUrl1());
                intent.putExtra("imageUrl2", arrayList.getImageUrl2());
                intent.putExtra("imageUrl3", arrayList.getImageUrl3());
                intent.putExtra("imageUrl4", arrayList.getImageUrl4());
                intent.putExtra("imageUrl5", arrayList.getImageUrl5());
                intent.putExtra("favoriteCount", arrayList.getFavoriteCount());
                intent.putExtra("date", arrayList.getDate());
                intent.putExtra("content", arrayList.getContent());
                intent.putExtra("postID", arrayList.getPostID());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}


