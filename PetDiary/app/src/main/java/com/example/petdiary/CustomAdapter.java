package com.example.petdiary;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.example.petdiary.adapter.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Data> arrayList;
    private Context context;


    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public CustomAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);


        view.findViewById(R.id.Comment_btn).setOnClickListener(new View.OnClickListener(){
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

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getImageUrl1())
//                .into(holder.imageUrl1);
        viewPager = (ViewPager) holder.itemView.findViewById(R.id.main_image);
        viewPageAdapter = new ViewPageAdapter(arrayList.get(position).getImageUrl1(), arrayList.get(position).getImageUrl2(),
                arrayList.get(position).getImageUrl3(), arrayList.get(position).getImageUrl4(), arrayList.get(position).getImageUrl5(), context);
        viewPager.setAdapter(viewPageAdapter);
        holder.content.setText(arrayList.get(position).getContent());

        TabLayout tabLayout = (TabLayout) holder.itemView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.main_textView);

        }
    }
}