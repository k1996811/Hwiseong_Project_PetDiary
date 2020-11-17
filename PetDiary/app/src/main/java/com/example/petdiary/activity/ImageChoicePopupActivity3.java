package com.example.petdiary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.petdiary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ImageChoicePopupActivity3 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_choice_popup);
    }

    public void goCamera(View v) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                //startToast("권한을 허용하셨습니다.");
            } else {
                //startToast("권한을 허용해 주세요.");
            }
        } else {
            Intent intent = new Intent(this, CameraAppActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    public void goGallery(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                //startToast("권한을 허용해 주세요.");
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity();
                } else {
                    startToast("권한을 허용해 주세요.");
                }
        }
    }

    private String postImgPath;
    String[] sImg;
    int PICK_IMAGE_MULTIPLE = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Get a non-default Storage bucket
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://petdiary-794c6.appspot.com");
        StorageReference storageRef = storage.getReference();
        Intent resultIntent2 = new Intent();

        super.onActivityResult(requestCode, resultCode, data);
        ClipData clipData = data.getClipData();

        if (requestCode == PICK_IMAGE_MULTIPLE) {

            if (data.getClipData() == null) {
                Toast.makeText(this, "다중선택이 불가한 기기입니다", Toast.LENGTH_LONG).show();
            } else {
                if (clipData.getItemCount() > 9) {
                    Toast.makeText(this, "사진은 9장까지만 선택 가능합니다", Toast.LENGTH_LONG).show();
                } else {

                    postImgPath = data.getStringExtra("postImgPath");
                    sImg = new String[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {

                        Uri file = Uri.fromFile(new File(getPath(clipData.getItemAt(i).getUri())));
                        //bitmap[i] = StringToBitmap(clipData.getItemAt(i).toString());
                        sImg[i] = clipData.getItemAt(i).toString();
                        Log.d("vcxz",sImg[i]);
                        resultIntent2.putExtra("postImgPath" + i, clipData.getItemAt(i).getUri());


                        StorageReference riversRef = storageRef.child("chatImage/" + file.getLastPathSegment());
                        UploadTask uploadTask = riversRef.putFile(file);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                            }
                        });
                    }
                }
            }

            setResult(Activity.RESULT_OK, resultIntent2);
            finish();
        }
    }

    private void myStartActivity() {
        //Intent intent = new Intent(this, c);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }


}