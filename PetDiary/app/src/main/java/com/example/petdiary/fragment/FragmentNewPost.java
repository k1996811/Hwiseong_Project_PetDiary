package com.example.petdiary.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.petdiary.ContentDTO;
import com.example.petdiary.activity.*;
import com.example.petdiary.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class FragmentNewPost extends Fragment {

    private static final String TAG = "NewPostActivity";

    ImageView[] postImg = new ImageView[5];
    int[] postImgCheck = new int[5];
    int choiceNum;
    RelativeLayout loaderLayout;
    Spinner spinner;
    DatabaseReference images;

    ViewGroup viewGroup;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_newpost, container, false);

        String[] items = {"강아지", "고양이", "앵무새", "두더지", "물고기"};
        spinner = (Spinner)viewGroup.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "";
            }
        });

        setEmail();

        images = FirebaseDatabase.getInstance().getReference().child("images");
        images.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewGroup.findViewById(R.id.postButton).setOnClickListener(onClickListener);

        postImg[0] = viewGroup.findViewById(R.id.postImg1);
        postImg[0].setOnClickListener(onClickListener);
        postImg[1] = viewGroup.findViewById(R.id.postImg2);
        postImg[1].setOnClickListener(onClickListener);
        postImg[2] = viewGroup.findViewById(R.id.postImg3);
        postImg[2].setOnClickListener(onClickListener);
        postImg[3] = viewGroup.findViewById(R.id.postImg4);
        postImg[3].setOnClickListener(onClickListener);
        postImg[4] = viewGroup.findViewById(R.id.postImg5);
        postImg[4].setOnClickListener(onClickListener);

        for(int i=0; i<5; i++){
            imgUri[i] = "";
        }

        return viewGroup;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.postButton:
                    loaderLayout = viewGroup.findViewById(R.id.loaderLayout);
                    loaderLayout.setVisibility(View.VISIBLE);
                    post();
                    break;
                case R.id.postImg1:
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                        } else {
                            //startToast("권한을 허용해 주세요.");
                        }
                    } else {
                        myStartActivity(GalleryActivity.class);
                    }
                    choiceNum = 0;
                    break;
                case R.id.postImg2:
                    myStartActivity(GalleryActivity.class);
                    choiceNum = 1;
                    break;
                case R.id.postImg3:
                    myStartActivity(GalleryActivity.class);
                    choiceNum = 2;
                    break;
                case R.id.postImg4:
                    myStartActivity(GalleryActivity.class);
                    choiceNum = 3;
                    break;
                case R.id.postImg5:
                    myStartActivity(GalleryActivity.class);
                    choiceNum = 4;
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("권한을 허용해 주세요.");
                }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    String postImgPath = data.getStringExtra("postImgPath");
                    //Log.e("postImgPath", "postImgPath : " + postImgPath);
                    setPostImg(postImgPath);
                    //Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    //user_profileImage_ImageView.setImageBitmap(bmp);
                } else {
                    Log.e("postImgPath", "실패!");
                }
                break;
        }
    }

    private void setPostImg(String postImgPath){
        if(postImgCheck[0] == 0){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[0]);
            img[0] = postImgPath;
            postImgCheck[0] = 1;
            postNumCheck = 1;
        } else if(postImgCheck[1] == 0){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[1]);
            img[1] = postImgPath;
            postImgCheck[1] = 1;
            postNumCheck = 2;
        } else if(postImgCheck[2] == 0){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[2]);
            img[2] = postImgPath;
            postImgCheck[2] = 1;
            postNumCheck = 3;
        } else if(postImgCheck[3] == 0){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[3]);
            img[3] = postImgPath;
            postImgCheck[3] = 1;
            postNumCheck = 4;
        } else if(postImgCheck[4] == 0){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[4]);
            img[4] = postImgPath;
            postImgCheck[4] = 1;
            postNumCheck = 5;
        } else if(postImgCheck[0] == 1 && postImgCheck[1] == 1 && postImgCheck[2] == 1 && postImgCheck[3] == 1 && postImgCheck[4] == 1){
            Glide.with(this).load(postImgPath).centerCrop().override(500).into(postImg[choiceNum]);
            img[choiceNum] = postImgPath;
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ){
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int num;
    private String email;
    private String category;
    private String[] img = new String[5];
    private String[] imgUri = new String[5];
    private String content;
    private String tag;
    private String date;
    private String date2;

    private ArrayList<String> hashTag;

    private void post(){
        num = 0;
        //category = spinner.getSelectedItem().toString();
        content = ((EditText) viewGroup.findViewById(R.id.contents)).getText().toString();
        tag = "";
        long now = System.currentTimeMillis();
        Date nowdate = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdfNow2 = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        date = sdfNow.format(nowdate);
        date2 = sdfNow2.format(nowdate);

        hashTag = new ArrayList<String>();
        if(content.length() > 0){
            String[] word = content.split(" ");

            for(int i=0; i<word.length; i++){
                if(word[i].charAt(0) == '#'){
                    hashTag.add(word[i]);
                }
            }
        }
        saveImage();
    }

    private void setEmail(){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Log.d("@@@", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            email = document.getData().get("email").toString();
                            //Log.e("@@@", "email : " + email);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    int postNum = 0;
    int postNumCheck = 0;

    private void saveImage(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final UploadTask[] uploadTask = new UploadTask[1];

        for(int i=0; i<5; i++){
            if(postImgCheck[i] == 1){
                final Uri file = Uri.fromFile(new File(img[i]));
                StorageReference riversRef = storageRef.child("images/"+date2+"postImg_"+i);
                uploadTask[0] = riversRef.putFile(file);

                final int finalI = i;
                final int finalI1 = i;
                uploadTask[0].addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final StorageReference ref = storageRef.child("images/"+date2+"postImg_"+ finalI);
                        uploadTask[0] = ref.putFile(file);

                        Task<Uri> urlTask = uploadTask[0].continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    imgUri[finalI1] = downloadUri.toString();
                                    postNum++;
                                    Log.e("@@@", imgUri[0]);
                                    if(postNum == postNumCheck){
                                        postData();
                                    }
                                } else {
                                }
                            }
                        });
                    }
                });
            }
        }

    }

    long maxid = 0;
    private FirebaseDatabase firebaseDatabase;
    FragmentMain fragmentMain;
    BottomNavigationView bottomNavigationView;
    Menu menu;

    private void postData(){
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference images = firebaseDatabase.getReference().child("images").push();

        ContentDTO contentDTO = new ContentDTO();
        Log.e("@@@", String.valueOf(maxid));
        contentDTO.setNum((int)maxid+1);

        contentDTO.setImageUrl1(imgUri[0]);
        contentDTO.setImageUrl2(imgUri[1]);
        contentDTO.setImageUrl3(imgUri[2]);
        contentDTO.setImageUrl4(imgUri[3]);
        contentDTO.setImageUrl5(imgUri[4]);
        contentDTO.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        contentDTO.setContent(content);
        contentDTO.setEmail(email);
        contentDTO.setCategory(category);
        contentDTO.setDate(date);
        contentDTO.setHashTag(hashTag);

        //게시물을 데이터를 생성 및 엑티비티 종료
        images.setValue(contentDTO);

        getActivity().setResult(RESULT_OK);
        startToast("게시글을 등록하였습니다.");
        loaderLayout.setVisibility(View.INVISIBLE);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.tab3).setChecked(false);
        menu.findItem(R.id.tab1).setChecked(true);

        /////메인 타임라인으로 프래그먼트 이동
        ((MainActivity)getActivity()).replaceFragment(fragmentMain.newInstance());
    }

    private void startToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(getContext(), c);
        startActivityForResult(intent, 0);
    }


}

