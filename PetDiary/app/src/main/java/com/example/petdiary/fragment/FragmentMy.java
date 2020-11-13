package com.example.petdiary.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.example.petdiary.activity.*;
import com.bumptech.glide.Glide;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class FragmentMy extends Fragment {

    private static final String TAG = "MyPageActivity";
    private String profilePath;

    TextView emailTextView;
    TextView nickNameTextView;
    TextView toolbarNickName;
    ImageView user_profileImage_ImageView;

    ViewGroup viewGroup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            myStartActivity(SignUpActivity.class);
        } else {

        }

        viewGroup.findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        viewGroup.findViewById(R.id.resetPasswordButton).setOnClickListener(onClickListener);
        viewGroup.findViewById(R.id.memberInfoInitButton).setOnClickListener(onClickListener);
        viewGroup.findViewById(R.id.setProfileImg).setOnClickListener(onClickListener);

        emailTextView = viewGroup.findViewById(R.id.emailEditText);
        nickNameTextView = viewGroup.findViewById(R.id.nickNameEditText);
        toolbarNickName = viewGroup.findViewById(R.id.toolbar_nickName);

        user_profileImage_ImageView = viewGroup.findViewById(R.id.user_profileImage_ImageView);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            setImg();
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            emailTextView.setText(document.getData().get("email").toString());
                            nickNameTextView.setText(document.getData().get("nickName").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return viewGroup;
    }

    private void startToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(LoginActivity.class);
                    getActivity().finish();
                    break;
                case R.id.resetPasswordButton:
                    myStartActivity2(SetPasswordTwoActivity.class);
                    break;
                case R.id.memberInfoInitButton:
                    //myStartActivity(MemberInfoEditActivity.class);
                    startToast("회원정보수정");
                    break;
                case R.id.setProfileImg:
                    //myStartActivity2(ImageChoicePopupActivity.class);
                    startPopupActivity();
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(getContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    String postImgPath = data.getStringExtra("postImgPath");

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageRef = storage.getReference();
                    final UploadTask[] uploadTask = new UploadTask[1];

                    final Uri file = Uri.fromFile(new File(postImgPath));
                    StorageReference riversRef = storageRef.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid() +"_profileImage.jpg");
                    uploadTask[0] = riversRef.putFile(file);

                    uploadTask[0].addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
                    setProfileImg(postImgPath);
                } else {
                }
                break;
        }
    }

    private void myStartActivity2(Class c){
        Intent intent = new Intent(getContext(), c);
        startActivityForResult(intent, 0);
    }

    private void startPopupActivity(){
        Intent intent = new Intent(getContext(), ImageChoicePopupActivity.class);
        startActivityForResult(intent, 0);
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

