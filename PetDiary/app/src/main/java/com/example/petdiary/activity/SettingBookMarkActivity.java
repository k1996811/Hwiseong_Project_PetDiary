package com.example.petdiary.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.BookmarkInfo;
import com.example.petdiary.CustomAdapterSub;
import com.example.petdiary.Data;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SettingBookMarkActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Data> arrayList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ArrayList<String> bookmarkPost = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_bookmark);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("bookmark/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    BookmarkInfo bookmark = messageData.getValue(BookmarkInfo.class);
                    String postId = bookmark.getPostID();
                    bookmarkPost.add(postId);
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("post")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        boolean check = false;
                                        for(int i=0; i<bookmarkPost.size(); i++){
                                            if(document.getId().equals(bookmarkPost.get(i))){
                                                check = true;
                                                break;
                                            }
                                        }
                                        if(check){
                                            Data dataList = new Data();
                                            dataList.setPostID(document.getId());
                                            dataList.setUid(document.getData().get("uid").toString());
                                            dataList.setContent(document.getData().get("content").toString());
                                            dataList.setImageUrl1(document.getData().get("imageUrl1").toString());
                                            dataList.setImageUrl2(document.getData().get("imageUrl2").toString());
                                            dataList.setImageUrl3(document.getData().get("imageUrl3").toString());
                                            dataList.setImageUrl4(document.getData().get("imageUrl4").toString());
                                            dataList.setImageUrl5(document.getData().get("imageUrl5").toString());
                                            dataList.setNickName(document.getData().get("nickName").toString());
                                            dataList.setDate(document.getData().get("date").toString());
                                            dataList.setCategory(document.getData().get("category").toString());
                                            dataList.setEmail(document.getData().get("email").toString());
                                            dataList.setFavoriteCount(Integer.parseInt(document.getData().get("favoriteCount").toString()));
                                            arrayList.add(0, dataList);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("###", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new CustomAdapterSub(arrayList, getApplicationContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

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