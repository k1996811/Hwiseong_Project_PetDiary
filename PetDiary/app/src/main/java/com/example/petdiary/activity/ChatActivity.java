package com.example.petdiary.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.R;
import com.example.petdiary.Chat;
import com.example.petdiary.PersonAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatMain";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    EditText etText;
    Button btnSend;
    String stEmail;
    ArrayList<Chat> chatArrayList;
    PersonAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        chatArrayList = new ArrayList<>();
        Log.d("bbb","cccc");

        //stEmail = getIntent().getStringExtra("email");
        FirebaseUser user =mAuth.getCurrentUser();
        stEmail =  user.getEmail();
        btnSend = findViewById(R.id.btn_send);
        etText = findViewById(R.id.chat);

        RecyclerView recyclerView = findViewById(R.id.room_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PersonAdapter(chatArrayList, stEmail);
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
                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Chat chat = dataSnapshot.getValue(Chat.class);

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

                String stText = etText.getText().toString();
                Toast.makeText(ChatActivity.this, "MSG : "+stText, Toast.LENGTH_SHORT).show();
                // Write a message to the database
                database = FirebaseDatabase.getInstance();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String datetime = dateformat.format(c.getTime());
                System.out.println(datetime);
                DatabaseReference myRef = database.getReference("message").child(datetime);

                Hashtable<String, String> numbers
                        = new Hashtable<String, String>();
                numbers.put("email", stEmail);
                numbers.put("text", stText);
                myRef.setValue(numbers);

            }
        });




    }


}