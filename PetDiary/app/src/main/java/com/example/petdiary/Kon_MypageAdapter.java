package com.example.petdiary;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.petdiary.adapter.ViewPageAdapterSub;

import java.util.ArrayList;

public class Kon_MypageAdapter extends RecyclerView.Adapter<Kon_MypageAdapter.MypageViewHolder> {

    private ArrayList<Data> arrayList;
    private Context context;
    private LayoutInflater inf;
    private int layout;

    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public Kon_MypageAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public MypageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kon_mypage_item, parent, false);
        MypageViewHolder holder = new MypageViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MypageViewHolder holder, int position) {

        String url = arrayList.get(position).getImageUrl1();
       // if(url != null)
             Glide.with(context).load(url).centerCrop().override(500).into(holder.postImage);
    //    pageView = (ImageView)holder.itemView.findViewById(R.id.main_image);

//        viewPager = (ViewPager) holder.itemView.findViewById(R.id.main_image);
//        viewPageAdapter = new ViewPageAdapterSub(arrayList.get(position), arrayList.get(position).getImageUrl1(), context);
//        viewPager.setAdapter(viewPageAdapter);
    }
    ViewPageAdapterSub viewPageAdapter;
  //  ViewPager viewPager;
    ImageView pageView;




    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class MypageViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        public MypageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.postImage = itemView.findViewById(R.id.mypage_image);
        }
    }
}