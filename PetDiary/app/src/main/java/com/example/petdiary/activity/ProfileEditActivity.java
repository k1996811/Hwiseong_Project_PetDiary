

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);



        userImage.setOnClickListener(onClickListener);

        // 수정버튼
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editIcon.isClickable()) {
                    isEdit = true;

                    preName = userName.getText().toString();
                    preMemo = userMemo.getText().toString();


                    setEditIcon(false);
                    setEditMode(true);

                }

            }
        });

        // 취소버튼
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfileImg(preImage);
                userName.setText(preName);
                userMemo.setText(preMemo);


                setEditIcon(true);
                setEditMode(false);
            }
        });

        // 저장버튼
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 변경은 이미 된 상태이기에 하지 않음
                setEditIcon(true);
                setEditMode(false);


//                if(isImageEdit)
//                {
//
//                    final String[] profileImg = new String[1];
//                    // 파이어베이스 스토리지에 이미지 저장
//                    FirebaseStorage storage = FirebaseStorage.getInstance();
//                    final StorageReference storageRef = storage.getReference();
//                    final UploadTask[] uploadTask = new UploadTask[1];
//
//                    final Uri file = Uri.fromFile(new File(postImgPath));
//                    StorageReference riversRef = storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "_profileImage.jpg");
//                    uploadTask[0] = riversRef.putFile(file);
//
//                    uploadTask[0].addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            // 파이어베이스의 스토리지에 저장한 이미지의 다운로드 경로를 가져옴
//                            final StorageReference ref = storageRef.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() + "_profileImage.jpg");
//                            uploadTask[0] = ref.putFile(file);
//
//                            Task<Uri> urlTask = uploadTask[0].continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                @Override
//                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                    if (!task.isSuccessful()) {
//                                        throw task.getException();
//                                    }
//                                    return ref.getDownloadUrl();
//                                }
//                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    if (task.isSuccessful()) {
//                                        Uri downloadUri = task.getResult();
//                                        profileImg[0] = downloadUri.toString();
//
//                                        // 클라우드 파이어스토어의 users에 프로필 이미지 주소 저장
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                        DocumentReference washingtonRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                        washingtonRef
//                                                .update("profileImg", profileImg[0])
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        Log.d("ProfileEditActivity", "DocumentSnapshot successfully updated!");
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Log.w("ProfileEditActivity", "Error updating document", e);
//                                                    }
//                                                });
//                                    } else {
//                                    }
//                                }
//                            });
//
//                        }
//                    });

                    setProfileImg(postImgPath);
            //    }
            }
        });


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
    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId())
            {
                case R.id.userPage_Image:
                    if(isEdit)
                        startPopupActivity();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("postImgPath",postImgPath);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setEditIcon(boolean isShown) {
        if (isShown)
            editIcon.setVisibility(View.VISIBLE);
        else
            editIcon.setVisibility(View.INVISIBLE);

        editIcon.setClickable(isShown);

    }


    private void setEditMode(boolean isEditMode) {
        if (isEditMode) {
            isEdit= true;
            userName.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userName.setFocusableInTouchMode(true);


            userMemo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userMemo.setFocusableInTouchMode(true);

            saveBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else {
            isEdit=false;
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



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    isImageEdit = true;
                    postImgPath = data.getStringExtra("postImgPath");
                    setProfileImg(postImgPath);
                }
                break;
        }
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

    private void setProfileImg(String profileImg) {
        Log.d("setProfileImg!!!!!", "setProfileImg: " + profileImg);
           Glide.with(this).load(profileImg).centerCrop().override(500).into(userImage);


    }


    private void startPopupActivity() {
        Intent intent = new Intent(getApplicationContext(), ImageChoicePopupActivity.class);
        startActivityForResult(intent, 0);
    }



}