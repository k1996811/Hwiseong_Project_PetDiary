package com.example.petdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.petdiary.adapter.ViewPageAdapter;
import com.example.petdiary.adapter.ViewPageAdapterDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class Expand_ImageView extends AppCompatActivity {

    private String uid;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private String imageUrl5;
    private String content;
    private ArrayList<String> hashTag = new ArrayList<String>();
    private String date;
    private String nickName;
    private int favoriteCount;

    ViewPageAdapterDetail viewPageAdapter;
    ViewPager viewPager;
    WormDotsIndicator wormDotsIndicator;

    TextView post_nickName;
    TextView post_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand__image_view2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        nickName = intent.getStringExtra("nickName");
        date = intent.getStringExtra("date");
        content = intent.getStringExtra("content");
        imageUrl1 = intent.getStringExtra("imageUrl1");
        imageUrl2 = intent.getStringExtra("imageUrl2");
        imageUrl3 = intent.getStringExtra("imageUrl3");
        imageUrl4 = intent.getStringExtra("imageUrl4");
        imageUrl5 = intent.getStringExtra("imageUrl5");
        favoriteCount = intent.getIntExtra("favoriteCount", 0);

        final String[] profileImg = new String[1];
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            profileImg[0] = document.getData().get("profileImg").toString();
                            if(profileImg[0].length() > 0){
                                ImageView profileImage = (ImageView) findViewById(R.id.Profile_image);
                                Glide.with(getApplicationContext()).load(profileImg[0]).centerCrop().override(500).into(profileImage);
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

        viewPager = (ViewPager) findViewById(R.id.main_image);
        viewPageAdapter = new ViewPageAdapterDetail(imageUrl1, imageUrl2, imageUrl3, imageUrl4, imageUrl5, getApplicationContext());
        viewPager.setAdapter(viewPageAdapter);

        wormDotsIndicator  = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);
        wormDotsIndicator .setViewPager(viewPager);

        post_nickName = findViewById(R.id.Profile_Name);
        post_content = findViewById(R.id.main_textView);

        post_nickName.setText(nickName);
        post_content.setText(content);
        if(content.length() == 0){
            post_content.setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.onPopupButton).setOnClickListener(new View.OnClickListener(){
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}