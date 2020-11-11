package com.example.petdiary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petdiary.MemberInfo;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPasswordTwoActivity extends AppCompatActivity {

    private static final String TAG = "SetPasswordActivity";
    private FirebaseAuth mAuth;

    private String email;
    private String nickName;
    private String password;
    private String currentPassword;
    private String profilePath;

    private boolean checkCurrentPassword = false;

    EditText currentPasswordEditText;
    EditText passwordEditText;
    EditText passwordCheckEditText;

    ImageView currentPasswordCheckedImg;
    ImageView currentPasswordUnCheckedImg;
    ImageView passwordCheckedImg;
    ImageView passwordUnCheckedImg;
    ImageView passwordCheckCheckedImg;
    ImageView passwordCheckUnCheckedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_two);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.okButton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelButton).setOnClickListener(onClickListener);

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);

        currentPasswordEditText.addTextChangedListener(textWatcherCurrentPassword);
        passwordEditText.addTextChangedListener(textWatcherPassword);
        passwordCheckEditText.addTextChangedListener(textWatcherPasswordCheck);

        currentPasswordCheckedImg = findViewById(R.id.currentPasswordCheckedImg);
        currentPasswordUnCheckedImg = findViewById(R.id.currentPasswordUnCheckedImg);
        passwordCheckedImg = findViewById(R.id.passwordCheckedImg);
        passwordUnCheckedImg = findViewById(R.id.passwordUnCheckedImg);
        passwordCheckCheckedImg = findViewById(R.id.passwordCheckCheckedImg);
        passwordCheckUnCheckedImg = findViewById(R.id.passwordCheckUnCheckedImg);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.okButton:
                    setInfo();
                    setPassword();
                    break;
                case R.id.cancelButton:
                    //startToast("취소버튼 클릭");
                    finish();
                    break;
            }
        }
    };

    public static boolean isValidPassword(String password){
        boolean err = false;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if(m.matches()){
            err = true;
        }
        return err;
    }

    private void setInfo(){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("@@@", FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            email = document.getData().get("email").toString();
                            nickName = document.getData().get("nickName").toString();
                            currentPassword = document.getData().get("password").toString();
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

    private void setPassword(){
        password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(checkCurrentPassword){
            if(isValidPassword(password)){
                if(password.equals(passwordCheck)){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = password;

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("@@@1", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    if (task.isSuccessful()) {
                                        Log.d("@@@2", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        startToast("비밀번호가 변경되었습니다.");
                                        Log.d(TAG, "User password updated.");

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        Log.d("@@@" , email + " / " + password + " / " + nickName);

                                        login();

                                        MemberInfo memberInfo = new MemberInfo(email, password, nickName);
                                        db.collection("users").document(user.getUid()).set(memberInfo)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startMainActivity();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        startToast("입력 정보를 확인해주세요.");
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });

                                    } else {
                                        startToast("비밀번호 변경에 실패하였습니다.");
                                        Log.d("TAG", task.getException().toString());
                                    }
                                }
                            });
                } else {
                    startToast("비밀번호가 일치하지 않습니다.");
                }
            } else {
                startToast("비밀번호를 확인해 주세요.(8~20 글자, 영문, 숫자, 특수문자 1자리 필수)");
            }
        } else {
            startToast("현재 비밀번호를 확인해주세요.");
        }


    }

    private void login(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startToast("로그인에 성공하였습니다.");
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if(task.getException() != null){
                                Log.d(TAG, task.getException().toString());
                                startToast("로그인에 실패하였습니다.");
                            }
                        }
                    }
                });
    }


    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private final TextWatcher textWatcherCurrentPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setInfo();
            if(currentPasswordEditText.getText().toString().equals(currentPassword)){
                checkCurrentPassword = true;
                currentPasswordUnCheckedImg.setVisibility(View.INVISIBLE);
                currentPasswordCheckedImg.setVisibility(View.VISIBLE);
            } else {
                checkCurrentPassword = false;
                currentPasswordCheckedImg.setVisibility(View.INVISIBLE);
                currentPasswordUnCheckedImg.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher textWatcherPassword = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(isValidPassword(passwordEditText.getText().toString())){
                passwordUnCheckedImg.setVisibility(View.INVISIBLE);
                passwordCheckedImg.setVisibility(View.VISIBLE);
            } else {
                passwordCheckedImg.setVisibility(View.INVISIBLE);
                passwordUnCheckedImg.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private final TextWatcher textWatcherPasswordCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){
                passwordCheckUnCheckedImg.setVisibility(View.INVISIBLE);
                passwordCheckCheckedImg.setVisibility(View.VISIBLE);
            } else {
                passwordCheckCheckedImg.setVisibility(View.INVISIBLE);
                passwordCheckUnCheckedImg.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
