package com.example.petdiary;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Comment extends AppCompatActivity {
    ImageView user_profileImage_ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        user_profileImage_ImageView = findViewById(R.id.bottom_Profile_Image);
        setImg();

    }

    private void setImg(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() +"_profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    private void setProfileImg(String profileImg){
        Glide.with(this).load(profileImg).centerCrop().override(500).into(user_profileImage_ImageView);
    }





}