package com.example.petdiary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petdiary.R;

public class ImageChoicePopupActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_choice_popup);

    }

    public void goCamera(View v){
        Intent intent = new Intent(this, CameraAppActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goGallery(View v){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            } else {
                //startToast("권한을 허용해 주세요.");
            }
        } else {
            myStartActivity(GalleryActivity.class);
        }
    }

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

    private String postImgPath;

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:

                if(resultCode == RESULT_OK){
                    postImgPath = data.getStringExtra("postImgPath");
                    Log.d("abcde3",postImgPath+"");

                    Intent resultIntent2 = new Intent();
                    resultIntent2.putExtra("postImgPath", postImgPath);
                    setResult(Activity.RESULT_OK, resultIntent2);
                    finish();
                } else if(resultCode == 999){
                    finish();
                } else if(resultCode == RESULT_CANCELED){
                    finish();
                } else {
                    //Log.e("postImgPath", "실패!");
                }

                break;
        }
    }

    private void myStartActivity(Class c){

        //Intent intent = new Intent(this, c);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }




}


