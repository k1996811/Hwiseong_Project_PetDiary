package com.example.petdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.petdiary.activity.*;
import com.bumptech.glide.Glide;
import com.example.petdiary.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentMy extends Fragment {

    private static final String TAG = "MyPage_Fragment";

    TextView profileName;
    TextView profileMemo;
    String profileImgName;   // 프로필이 로딩되면 null값이 아니게 됨

    Map<String, String> userInfo = new HashMap<>();



    ImageView profileEditImg;
    int[] imgs = {
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,

            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.dog,
    };


    //    TextView emailTextView;
//    TextView nickNameTextView;
//    TextView toolbarNickName;
//    ImageView user_profileImage_ImageView;
    ViewGroup viewGroup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_kon, container, false);

        profileEditImg = viewGroup.findViewById(R.id.profile_image);
        profileName = viewGroup.findViewById(R.id.profile_name);
        profileMemo = viewGroup.findViewById(R.id.profile_memo);

        //  유저
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        // 프로필 이미지, 닉네임, 메모 가져오기,
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //userInfo.put("email",document.getString("email"));
                    userInfo.put("nickName",document.getString("nickName"));
                    //userInfo.put("password",document.getString("password"));
                    userInfo.put("profileImg",document.getString("profileImg"));
                    userInfo.put("memo",document.getString("memo"));


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


        ////////////// irang Start
        GridView gridView = viewGroup.findViewById(R.id.gridView);
        GridListAdapter adapter = new GridListAdapter();
        gridView.setAdapter(adapter);

        for (int i = 0; i < imgs.length; ++i) {
            adapter.addItem(new MypageGridItem(imgs[i]));
        }


        ImageView addPetBtn = viewGroup.findViewById(R.id.profile_petAddBtn);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), position + "번째 아이템이 클릭되었다!!", Toast.LENGTH_SHORT).show();
            }
        });


        // 프로필뷰 관련코드.
        ImageView petAddBtn = viewGroup.findViewById(R.id.profile_petAddBtn);
        final ImageView profileImage = viewGroup.findViewById(R.id.profile_image);
        final TextView userName = viewGroup.findViewById(R.id.profile_name);
        final TextView userMemo = viewGroup.findViewById(R.id.profile_memo);


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
                intent.putExtra("userName",   profileName.getText().toString());// userInfo.get("nickName"));//userName.getText().toString());
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

    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.memberInfoInitButton:
                    startToast("회원정보수정");
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                   // userInfo.put("nickName", data.getStringExtra("nickName"));
                  //  userInfo.put("memo",data.getStringExtra("memo"));
                  //userInfo.put("profileImg", data.getStringExtra("profileImg"));

                    setProfileImg(data.getStringExtra("profileImg"));
                    profileName.setText( data.getStringExtra("nickName"));
                    profileMemo.setText(  data.getStringExtra("memo"));

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


    //irang temp -------------------------------------------------------

    public class MypageGridItem {
        int _imgName;

        public MypageGridItem(int imgName) {
            _imgName = imgName;
            Log.d("test", "imgName :  " + imgName);
        }
    }

    class GridListAdapter extends BaseAdapter {
        Context context;
        ArrayList<MypageGridItem> items = new ArrayList<MypageGridItem>();

        public void addItem(MypageGridItem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            context = parent.getContext();
            MypageGridItem listItem = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_mypage_temp, parent, false);
            }

            //xml의 ImageView 참조
            ImageView image = convertView.findViewById(R.id.mypage_image);

            image.setImageResource(listItem._imgName);

            return convertView;

        }
    }
}