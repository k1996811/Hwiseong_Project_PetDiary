

package com.example.petdiary.activity;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ProfileEditActivity extends AppCompatActivity {
    ImageView editIcon;
    ImageView userImage;
    EditText userName;
    EditText userMemo;
    Button saveBtn;
    Button cancelBtn;


    String userId;
    String targetId;

    //State
    boolean isEdit = false;
    String preImage;
    String preName;
    String preMemo;


    boolean isImageEdit = false;
    String postImgPath;  // 갤러리 눌러서 가져온 파일 이름

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        editIcon = findViewById(R.id.userPage_editIcon);
        userImage = findViewById(R.id.userPage_Image);
        userName = findViewById(R.id.userPage_name);
        userMemo = findViewById(R.id.userPage_memo);
        saveBtn = findViewById(R.id.userPage_save);
        cancelBtn = findViewById(R.id.userPage_cancel);


        //데이터 수신
        Intent intent = getIntent();
        userId = intent.getExtras().getString("userId");
        targetId = intent.getExtras().getString("targetId");
        userName.setText(intent.getExtras().getString("userName"));
        userMemo.setText(intent.getExtras().getString("userMemo"));
        //userImage.setImageResource(intent.getExtras().getInt("userImage"));

        preImage = intent.getExtras().getString("userImage");
        setProfileImg(preImage);

        userImage.setOnClickListener(onClickListener);
        editIcon.setOnClickListener(onClickListener);
        cancelBtn.setOnClickListener(onClickListener);
        saveBtn.setOnClickListener(onClickListener);

        if (userId.equals(targetId)) {
            setEditIcon(true);
        } else {
            setEditIcon(false);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //myStartActivity(SignUpActivity.class);
        } else {
        }

    }

    // 이벤트 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.userPage_editIcon:  // 편집 버튼 선택
                    if (editIcon.isClickable()) {
                        isEdit = true;
                        preName = userName.getText().toString();
                        preMemo = userMemo.getText().toString();
                        setEditIcon(false);
                        setEditMode(true);
                    }
                    break;
                case R.id.userPage_Image:   // 프로필 사진 선택
                    if (isEdit)
                        startPopupActivity();
                    break;
                case R.id.userPage_save:    // 저장 버튼 선택
                    setEditIcon(true);
                    setEditMode(false);
                    setProfileImg(postImgPath);
                    break;
                case R.id.userPage_cancel:  // 취소 버튼 선택
                    isImageEdit = false;
                    setProfileImg(preImage);
                    userName.setText(preName);
                    userMemo.setText(preMemo);
                    setEditIcon(true);
                    setEditMode(false);
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: // 갤러리에서 이미지 선택시
                if (resultCode == RESULT_OK) {
                    isImageEdit = true;
                    postImgPath = data.getStringExtra("postImgPath");
                    setProfileImg(postImgPath);
                }
                break;
        }
    }


    // 편집버튼 상태 변경 on/off
    private void setEditIcon(boolean isShown) {
        if (isShown)
            editIcon.setVisibility(View.VISIBLE);
        else
            editIcon.setVisibility(View.INVISIBLE);

        editIcon.setClickable(isShown);
    }

    // 편집 버튼 상태에 따른 이름, 메모, 저장, 취소버튼 상태 변경
    private void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            isEdit = true;
            userName.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userName.setFocusableInTouchMode(true);

            userMemo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userMemo.setFocusableInTouchMode(true);

            saveBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else {
            isEdit = false;
            userName.setBackground(null);
            userName.setFocusableInTouchMode(false);
            userName.setFocusable(false);

            userMemo.setBackground(null);
            userMemo.setFocusableInTouchMode(false);
            userMemo.setFocusable(false);

            saveBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setVisibility(View.INVISIBLE);
        }
    }


    // 프로필 이미지 변경 함수
    private void setProfileImg(String profileImg) {
        Log.d("IR", "setProfileImg: " + profileImg);
        Glide.with(this).load(profileImg).centerCrop().override(500).into(userImage);
    }


    // 갤러리 열기 위한 팝업생성 함수 
    private void startPopupActivity() {
        Intent intent = new Intent(getApplicationContext(), ImageChoicePopupActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onBackPressed() {
        if (isImageEdit) {
            Intent intent = new Intent();
            intent.putExtra("postImgPath", postImgPath);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        super.onBackPressed();
    }


    private void setImg() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "_profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String profileImg = uri.toString();
//                while(profileImg.length() == 0){
//                    continue;
//                }
                //Log.e("@@@!", profileImg);
                setProfileImg(profileImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}