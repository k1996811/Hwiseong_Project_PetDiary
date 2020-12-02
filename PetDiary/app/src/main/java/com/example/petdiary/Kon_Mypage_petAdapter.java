package com.example.petdiary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petdiary.adapter.ViewPageAdapterSub;
import com.example.petdiary.fragment.FragmentMy;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;


public class Kon_Mypage_petAdapter extends RecyclerView.Adapter<Kon_Mypage_petAdapter.MypagePetViewHolder> {

    private ArrayList<PetData> arrayList;
    private Context context;

    private int squareSize;
    //private int columnNum;

    FragmentMy.StringCallback stringCallback;
    int prePosition = -1;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public Kon_Mypage_petAdapter(ArrayList<PetData> arrayList, Context context, FragmentMy.StringCallback callback) {
        this.arrayList = arrayList;
        this.context = context;
        this.stringCallback = callback;
        //this.columnNum = columnNum;

    }


    @NonNull
    @Override
    public MypagePetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kon_mypage_pet_item, parent, false);
        MypagePetViewHolder holder = new MypagePetViewHolder(view);

        int layout_width = parent.getMeasuredWidth();
        //int layout_height = parent.getMeasuredHeight();
        //int itemSize = layout_width / columnNum;
        //int itemSize = layout_width
        //squareSize = itemSize - (itemSize / 32 );

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MypagePetViewHolder holder, int position) {

        //  holder.itemView.getLayoutParams().width = squareSize;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        // holder.itemView.getLayoutParams().height = squareSize;  // 아이템 뷰의 세로 길이를 구한 길이로 변경
        // holder.itemView.requestLayout(); // 변경 사항 적용

        // 필요한부분
        String url = arrayList.get(position).getImageUrl();
        Glide.with(context).load(url).centerCrop().override(500).into(holder.postImage);

        holder.changeFrameState(selectedItems.get(position));
    }

    ViewPageAdapterSub viewPageAdapter;
    //  ViewPager viewPager;
    ImageView pageView;


    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
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


    public class MypagePetViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView frame;
        ImageView postImage;
        int frameWidth;

        public MypagePetViewHolder(@NonNull final View itemView) {
            super(itemView);

            frame = itemView.findViewById(R.id.mypage_pet_imageView);
            this.postImage = itemView.findViewById(R.id.mypage_pet_image);
            frameWidth = itemView.getLayoutParams().width / 10;

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();


                    if (selectedItems.get(position)) {
                        selectedItems.delete(position);// 펼쳐진 Item을 클릭 시
                        stringCallback.callback("");
                    } else {
                        selectedItems.delete(prePosition); // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.put(position, true); // 클릭한 Item의 position을 저장
                        stringCallback.callback(arrayList.get(position).getPetId());
                    }

                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);

                    // 클릭된 position 저장
                    prePosition = position;

                }
            });
        }

        void onBind(){

        }

        private void changeFrameState(final boolean isOpen) {
            if(isOpen) {
                frame.setStrokeColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
            else {
                frame.setStrokeColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            }
        }

    }

}