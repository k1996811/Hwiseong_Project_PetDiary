package com.example.petdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.Data;
import com.example.petdiary.Kon_MypageAdapter;
import com.example.petdiary.RecyclerDecoration;
import com.example.petdiary.activity.*;
import com.bumptech.glide.Glide;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentMy extends Fragment {

    private static final String TAG = "MyPage_Fragment";

    TextView profileName;
    TextView profileMemo;
    String profileImgName;

    Map<String, String> userInfo = new HashMap<>();
    ArrayList<Data> postList = new ArrayList<Data>();

    ImageView profileEditImg;
    ViewGroup viewGroup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 리사이클뷰에 필요한거
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.kon_fragment_mypage, container, false);
        profileEditImg = viewGroup.findViewById(R.id.profile_image);
        profileName = viewGroup.findViewById(R.id.profile_name);
        profileMemo = viewGroup.findViewById(R.id.profile_memo);

        ImageView petAddBtn = viewGroup.findViewById(R.id.profile_petAddBtn);
        final ImageView profileImage = viewGroup.findViewById(R.id.profile_image);


        //  유저
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //////////////////////////////////// 프로필 이미지, 닉네임, 메모 가져오기,
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //userInfo.put("email",document.getString("email"));
                    userInfo.put("nickName", document.getString("nickName"));
                    //userInfo.put("password",document.getString("password"));
                    userInfo.put("profileImg", document.getString("profileImg"));
                    userInfo.put("memo", document.getString("memo"));


                    profileName.setText(userInfo.get(("nickName")));
                    profileMemo.setText(userInfo.get(("memo")));
                    profileImgName = document.getString("profileImg");
                    setProfileImg(profileImgName);
                    //setImg();
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        //////////////////////////////////// 게시물 로드
        loadPosts();

        //////////////////////////////////// 리사이클러뷰 setting
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화

        adapter = new Kon_MypageAdapter(postList, getContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        // 리사이클러뷰 간격추가
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10,20);
        recyclerView.addItemDecoration(spaceDecoration);



        // 프로필 이미지 수정
        profileImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //  setImg();
                setProfileImg(profileImgName);
                String userId = "IAmTarget";//"IAmUser"
                String targetId = "IAmTarget";
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                intent.putExtra("targetId", targetId);
                intent.putExtra("userId", userId);
                intent.putExtra("userImage", profileImgName);// userInfo.get("profileImg")); // 임시로 넣은 이미지
                intent.putExtra("userName", profileName.getText().toString());// userInfo.get("nickName"));//userName.getText().toString());
                intent.putExtra("userMemo", profileMemo.getText().toString());//userInfo.get("memo"));//userMemo.getText().toString());

                startActivityForResult(intent, 0);
                return true;
            }
        });


        // 펫 추가 버튼
        petAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "이제 당신이 해야 할 일은 버튼 기능을 추가하는 것 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return viewGroup;
    }


    // 개인 게시물 로드
    private void loadPosts()
    {
        //  유저
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("post").whereEqualTo("uid", uid);
        //query.get
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Data dataList = new Data();
                                dataList.setUid(document.getData().get("uid").toString());
                                dataList.setContent(document.getData().get("content").toString());
                                dataList.setImageUrl1(document.getData().get("imageUrl1").toString());
                                dataList.setImageUrl2(document.getData().get("imageUrl2").toString());
                                dataList.setImageUrl3(document.getData().get("imageUrl3").toString());
                                dataList.setImageUrl4(document.getData().get("imageUrl4").toString());
                                dataList.setImageUrl5(document.getData().get("imageUrl5").toString());
                                dataList.setNickName(document.getData().get("nickName").toString());
                                postList.add(0, dataList);
                                Log.d("태그 확인", document.getId() + "dataList => " + dataList.getImageUrl1());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // userInfo.put("nickName", data.getStringExtra("nickName"));
                    //  userInfo.put("memo",data.getStringExtra("memo"));
                    //userInfo.put("profileImg", data.getStringExtra("profileImg"));

                    setProfileImg(data.getStringExtra("profileImg"));
                    profileName.setText(data.getStringExtra("nickName"));
                    profileMemo.setText(data.getStringExtra("memo"));

                } else {
                }
                break;
        }
    }

    // 첫 초기화
    private void setImg() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "_profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String profileImg = uri.toString();
                setProfileImg(profileImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void setProfileImg(String profileImg) {
        Glide.with(this).load(profileImg).centerCrop().override(500).into(profileEditImg);
    }

}