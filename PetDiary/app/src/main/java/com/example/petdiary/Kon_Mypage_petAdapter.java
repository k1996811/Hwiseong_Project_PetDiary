package com.example.petdiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petdiary.adapter.ViewPageAdapterSub;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class Kon_Mypage_petAdapter extends RecyclerView.Adapter<Kon_Mypage_petAdapter.MypageViewHolder> {

    private ArrayList<PetData> arrayList;
    private Context context;

    private int squareSize;
    //private int columnNum;

    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public Kon_Mypage_petAdapter(ArrayList<PetData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        //this.columnNum = columnNum;
    }

    @NonNull
    @Override
    public MypageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kon_mypage_pet_item, parent, false);
        MypageViewHolder holder = new MypageViewHolder(view);

        int layout_width = parent.getMeasuredWidth();
        //int layout_height = parent.getMeasuredHeight();
        //int itemSize = layout_width / columnNum;
        //int itemSize = layout_width
        //squareSize = itemSize - (itemSize / 32 );

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MypageViewHolder holder, int position) {

        //  holder.itemView.getLayoutParams().width = squareSize;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        // holder.itemView.getLayoutParams().height = squareSize;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        // holder.itemView.requestLayout(); // 변경 사항 적용

        // 필요한부분
        String url = arrayList.get(position).getImageUrl();
        Glide.with(context).load(url).centerCrop().override(500).into(holder.postImage);


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
        MaterialCardView frame;
        ImageView postImage;
        int frameWidth;
        boolean isOn;

        public MypageViewHolder(@NonNull final View itemView) {
            super(itemView);

            frame = itemView.findViewById(R.id.mypage_pet_imageView);
            this.postImage = itemView.findViewById(R.id.mypage_pet_image);
            frameWidth = itemView.getLayoutParams().width / 10;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isOn = !isOn;

                    if (isOn) {
                        Log.d("로그로그로그~~~~", "강조색으로 바꾸기");
                        frame.setStrokeColor(ContextCompat.getColor(context, R.color.colorAccent));
                    }
                    else
                    {
                        Log.d("로그로그로그~~~~", "일반색으로 바꾸기");
                        //frame.setStrokeColor(R.color.colorPrimaryLight);
                        frame.setStrokeColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
                    }

                    //goPost(arrayList.get(getAdapterPosition()));
                }
            });
        }
    }

    private void goPost(PetData arrayList) {
        Intent intent = new Intent(context, Expand_ImageView.class);

//        intent.putExtra("postLike","unchecked");
//        intent.putExtra("bookmark", "unchecked");
//        intent.putExtra("friend", "unchecked");
//
//        intent.putExtra("postID", arrayList.getPostID());
//        intent.putExtra("nickName", arrayList.getNickName());
//        intent.putExtra("uid", arrayList.getUid());
//        intent.putExtra("imageUrl1", arrayList.getImageUrl1());
//        intent.putExtra("imageUrl2", arrayList.getImageUrl2());
//        intent.putExtra("imageUrl3", arrayList.getImageUrl3());
//        intent.putExtra("imageUrl4", arrayList.getImageUrl4());
//        intent.putExtra("imageUrl5", arrayList.getImageUrl5());
//        intent.putExtra("favoriteCount", arrayList.getFavoriteCount());
//        intent.putExtra("date", arrayList.getDate());
//        intent.putExtra("content", arrayList.getContent());

        context.startActivity(intent);
    }

}