package com.example.petdiary.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.Comment;
import com.example.petdiary.Data;
import com.example.petdiary.Expand_ImageView;
import com.example.petdiary.R;
import com.example.petdiary.activity.MainActivity;

import java.util.ArrayList;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("@@@re", "button2클릭");
                Intent intent = new Intent(v.getContext(), Comment.class);
                v.getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.main_image).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Expand_ImageView.class);
                v.getContext().startActivity(intent);
            }
        });




        view.findViewById(R.id.onPopupButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {

                CharSequence info[] = new CharSequence[] {"Edit", "Delete","Share" };

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("");

                builder.setItems(info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which)
                        {
                            case 0:
                                // 내정보
                                Toast.makeText(view.getContext(), "Edit", Toast.LENGTH_SHORT).show();

                                break;

                            case 1:
                                // 로그아웃
                                Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();

                                break;

                            case 2:
                                Intent msg = new Intent(Intent.ACTION_SEND);
                                msg.addCategory(Intent.CATEGORY_DEFAULT);
                                msg.putExtra(Intent.EXTRA_SUBJECT, "주제");
                                msg.putExtra(Intent.EXTRA_TEXT, "내용");
                                msg.putExtra(Intent.EXTRA_TITLE, "제목");
                                msg.setType("text/plain");

                                view.getContext().startActivity(Intent.createChooser(msg, "공유"));

                                break;
                        }

                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });








        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));


    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.Profile_Name);
            textView2 = itemView.findViewById(R.id.main_textView);
            imageView = itemView.findViewById(R.id.main_image);
        }

        void onBind(Data data) {
            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            imageView.setImageResource(data.getResId());
        }
    }






}