package com.example.petdiary.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petdiary.R;
import com.example.petdiary.fragment.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String password = "";

    TextView toolbarNickName;
    BottomNavigationView bottomNavigationView;
    FragmentMain fragmentMain;
    FragmentSub fragmentSub;
    FragmentNewPost fragmentNewPost;
    FragmentMy fragmentMy;
    FragmentContentMain fragmentContentMain;

//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            Log.e("name not found", e.toString());
//        }
//    }

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getAppKeyHash();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);
        toolbarNickName = findViewById(R.id.toolbar_nickName);

        if (user == null) {
            myStartActivity(LoginActivity.class);
            finish();
        } else {
            checkPassword();
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 햄버거메뉴

    public void blockFriendOnClick(View view){
        startToast("차단친구 메뉴");
    }

    public void noticeOnClick(View view){
        startToast("알림 설정 메뉴");
    }

    public void passwordSetOnClick(View view){
        startToast("비밀번호 변경 메뉴");
    }

    public void customerCenterOnClick(View view){
        startToast("고객센터 메뉴");
    }

    public void unRegisterOnClick(View view){
        startToast("회원탈퇴 메뉴");
    }

    public void AppInfoOnClick(View view){
        startToast("앱 정보 메뉴");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                drawerLayout.openDrawer(drawerView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setFirst() {
        fragmentMain = new FragmentMain();
        fragmentSub = new FragmentSub();
        fragmentNewPost = new FragmentNewPost();
        fragmentMy = new FragmentMy();
        fragmentContentMain = new FragmentContentMain();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentMain).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentMain).commitAllowingStateLoss();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentSub).commitAllowingStateLoss();
                        return true;
                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentNewPost).commitAllowingStateLoss();
                        return true;
                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentMy).commitAllowingStateLoss();
                        return true;
                    case R.id.tab5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentContentMain).commitAllowingStateLoss();
                        return true;
                    default: return false;

                }
            }
        });
    }


    public static boolean isValidPassword(String password) {
        boolean err = false;
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void checkPassword() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //Log.d("@@@", FirebaseAuth.getInstance().getCurrentUser().getUid()+"");
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            password = document.getData().get("password").toString();
                            toolbarNickName.setText(document.getData().get("nickName").toString() + " 님");
                            Log.d("###1", password);
                            if (isValidPassword(password)) {
                                setFirst();
                            } else {
                                myStartActivity(SetPasswordActivity.class);
                                finish();
                            }
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

}