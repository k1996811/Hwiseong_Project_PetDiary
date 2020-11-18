package com.example.petdiary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraAppActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
            //Log.e("@@@", photoFile.toString());
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }


    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PetDiary" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("postImgPath", imageFilePath);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else if(resultCode == RESULT_CANCELED){
            finish();
        }
    }

//    PermissionListener permissionListener = new PermissionListener() {
//        @Override
//        public void onPermissionGranted() {
//            //Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
//        }`
//
//        @Override
//        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            //Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
//        }
//    };


//    private long backKeyPressedTime = 0;
//    private Toast toast;
//
//    public void onBackPressed(){
//        //super.onBackPressed();
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
//        // 2000 milliseconds = 2 seconds
//        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//            backKeyPressedTime = System.currentTimeMillis();
//            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 카메라가 종료됩니다.", Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
//        // 현재 표시된 Toast 취소
//        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
//
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("exit", "exit");
//            setResult(999, resultIntent);
//
//            finish();
//        }
//    }

}