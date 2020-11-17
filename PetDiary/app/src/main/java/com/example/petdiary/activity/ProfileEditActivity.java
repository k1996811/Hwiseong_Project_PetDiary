

package com.example.petdiary.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.petdiary.R;

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
    String preName;
    String preMemo;


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
        userImage.setImageResource(intent.getExtras().getInt("userImage"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);


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
                // 서버에 변경된 데이터 보내는 코드 필요
                setEditIcon(true);
                setEditMode(false);
            }
        });


        if (userId.equals(targetId)) {
            setEditIcon(true);
        } else {
            setEditIcon(false);
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            userName.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userName.setFocusableInTouchMode(true);


            userMemo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.colorAccent));
            userMemo.setFocusableInTouchMode(true);

            saveBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        } else {
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


}