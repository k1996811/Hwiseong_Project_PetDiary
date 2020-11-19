package com.example.petdiary.activity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petdiary.MyAdapter;
import com.example.petdiary.R;
import com.example.petdiary.Chat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatMain";
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    FirebaseDatabase database;
    EditText etText;
    Button btnSend, picture;
    String stEmail;

    ArrayList<Chat> chatArrayList;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        chatArrayList = new ArrayList<>();
        //stEmail = getIntent().getStringExtra("email");
        FirebaseUser user = mAuth.getCurrentUser();
        stEmail = user.getEmail();

        btnSend = findViewById(R.id.btn_send);
        etText = findViewById(R.id.chat);

        recyclerView = findViewById(R.id.room_recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.d("xxx", "cccc1" + stEmail);
        mAdapter = new MyAdapter(chatArrayList, stEmail);
        recyclerView.setAdapter(mAdapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();
                String stEmail = chat.getEmail();
                String stText = chat.getText();
                Log.d(TAG, "stEmail:" + stEmail);
                Log.d(TAG, "stText:" + stText);
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    }
                });
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                //Chat chat = dataSnapshot.getValue(Chat.class);

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("message");
        ref.addChildEventListener(childEventListener);


        //보내기Send

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etText.getText().toString().length() > 0) {
                    String stText = etText.getText().toString();

                    Toast.makeText(ChatActivity.this, "MSG : " + stText, Toast.LENGTH_SHORT).show();
                    etText.getText().clear();

                    // Write a message to the database
                    database = FirebaseDatabase.getInstance();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
                    String datetime = dateformat.format(c.getTime());

                    DatabaseReference myRef = database.getReference("message").child(datetime);

                    Hashtable<String, String> numbers
                            = new Hashtable<String, String>();
                    numbers.put("email", stEmail);
                    numbers.put("text", stText);
                    myRef.setValue(numbers);

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                        }
                    });

                }
            }
        });


        picture = findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ImageChoicePopupActivity2.class);
                startActivityForResult(intent,0);
            }
        });

    }

    String [] sImg;
    String [] bit;
    ImageView iv;
    String ca;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sImg = new String[9];
        bit = new String[9];
        ca = new String();
        iv= findViewById(R.id.ivChat);
        switch(requestCode){
            case 0:
                if(resultCode == RESULT_OK){

                    for(int i = 0; i < 9; i++) {
                        sImg[i] = data.getStringExtra("postImgPath" + i + "");
                        bit[i] = data.getStringExtra("bit" + i + "");


                        if (sImg[i] != null) {

                            FirebaseStorage storage = FirebaseStorage.getInstance("gs://petdiary-794c6.appspot.com");
                            final StorageReference storageRef = storage.getReference();
                            setImage(bit[i]);

                            database = FirebaseDatabase.getInstance();

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
                            String datetime = dateformat.format(c.getTime());

                            DatabaseReference myRef = database.getReference("message").child(datetime);

                            Hashtable<String, String> numbers
                                    = new Hashtable<String, String>();
                            numbers.put("email", stEmail);
                            numbers.put("image", bit[i]+"");
                            myRef.setValue(numbers);

                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                                }
                            });
                        }

                    }


                } else {
                    ca = data.getStringExtra("camera");
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://petdiary-794c6.appspot.com");
                    final StorageReference storageRef = storage.getReference();
                    setImage(ca);

                    database = FirebaseDatabase.getInstance();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
                    String datetime = dateformat.format(c.getTime());

                    DatabaseReference myRef = database.getReference("message").child(datetime);

                    Hashtable<String, String> numbers
                            = new Hashtable<String, String>();
                    numbers.put("email", stEmail);
                    numbers.put("image", ca);
                    myRef.setValue(numbers);

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                        }
                    });

                }
                break;

        }
    }
    private void setImage(String bit){
        Glide.with(this).load(bit).into(iv);
    }


}