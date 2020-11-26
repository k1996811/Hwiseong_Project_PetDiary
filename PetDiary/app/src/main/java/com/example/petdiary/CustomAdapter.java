package com.example.petdiary;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.petdiary.activity.SetPasswordActivity;
import com.example.petdiary.adapter.ViewPageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Data> arrayList;
    private Context context;
    private Button Comment_btn;
    private Button onPopupButton;
    private CheckBox bookmark_button;

    private FirebaseDatabase firebaseDatabase;

    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.
    public CustomAdapter(ArrayList<Data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void clear(){
        arrayList.clear();
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        if(arrayList.get(position).getImageUrl1() == null){
            ImageView imageView = holder.itemView.findViewById(R.id.main_image);
            Glide.with(context).load(R.drawable.ic_launcher_foreground).centerCrop().override(1000).into(imageView);
        }

        viewPager = (ViewPager) holder.itemView.findViewById(R.id.main_image);
        viewPageAdapter = new ViewPageAdapter(arrayList.get(position), arrayList.get(position).getImageUrl1(), arrayList.get(position).getImageUrl2(),
                arrayList.get(position).getImageUrl3(), arrayList.get(position).getImageUrl4(), arrayList.get(position).getImageUrl5(), context);
        viewPager.setAdapter(viewPageAdapter);
        holder.content.setText(arrayList.get(position).getContent());
        holder.nickName.setText(arrayList.get(position).getNickName());

        bookmark_button = (CheckBox) holder.itemView.findViewById(R.id.bookmark_button);
        bookmark_button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference bookmark = firebaseDatabase.getReference("bookmark").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+arrayList.get(position).getPostID());
                if(b){
                    BookmarkInfo bookmarkInfo = new BookmarkInfo();
                    bookmarkInfo.setPostID(arrayList.get(position).getPostID());
                    //게시물을 데이터를 생성 및 엑티비티 종료
                    bookmark.setValue(bookmarkInfo);
                    Toast.makeText(context, "북마크에 등록하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    BookmarkInfo bookmarkInfo = new BookmarkInfo();
                    //게시물을 데이터를 생성 및 엑티비티 종료
                    bookmark.setValue(bookmarkInfo);
                    Toast.makeText(context, "북마크에 등록하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Comment_btn  = (Button) holder.itemView.findViewById(R.id.Comment_btn);
        Comment_btn.findViewById(R.id.Comment_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(context, Comment.class);

                intent.putExtra("nickName", arrayList.get(position).getNickName());
                intent.putExtra("uid", arrayList.get(position).getUid());
                intent.putExtra("content", arrayList.get(position).getContent());

                context.startActivity(intent);
            }
        });

        onPopupButton = (Button) holder.itemView.findViewById(R.id.onPopupButton);
        onPopupButton.setOnClickListener(new View.OnClickListener(){
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

        final String[] profileImg = new String[1];
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(arrayList.get(position).getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            profileImg[0] = document.getData().get("profileImg").toString();
                            if(profileImg[0].length() > 0){
                                ImageView profileImage = (ImageView) holder.itemView.findViewById(R.id.Profile_image);
                                Glide.with(context).load(profileImg[0]).centerCrop().override(500).into(profileImage);
                            }
                        } else {
                            //Log.d("###", "No such document");
                        }
                    }
                } else {
                    //Log.d("###", "get failed with ", task.getException());
                }
            }
        });


        wormDotsIndicator  = (WormDotsIndicator) holder.itemView.findViewById(R.id.worm_dots_indicator);
        wormDotsIndicator .setViewPager(viewPager);
    }

    ViewPageAdapter viewPageAdapter;
    ViewPager viewPager;
    WormDotsIndicator wormDotsIndicator;
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView nickName;
        ImageView profileImage;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content = itemView.findViewById(R.id.main_textView);
            this.nickName = itemView.findViewById(R.id.Profile_Name);
            this.profileImage = itemView.findViewById(R.id.Profile_image);
        }
    }
}