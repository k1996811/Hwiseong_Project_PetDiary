package com.example.petdiary.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.Chat;
import com.example.petdiary.ItemTouchHelperCallback;
import com.example.petdiary.Person;
import com.example.petdiary.PersonAdapter;
import com.example.petdiary.R;
import com.example.petdiary.activity.ChatActivity;
import com.example.petdiary.ui.chat.ChatListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private static final String TAG = "FriendsListFragment";
    ArrayList<Person> personArrayList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.fri_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        final PersonAdapter adapter = new PersonAdapter(getContext());


        database = FirebaseDatabase.getInstance();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        personArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String[] nn = new String[1];
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("asdf", document.getId() + " => " + document.getData());

                                if(document.get("email").toString().equals(user.getEmail())){

                                    nn[0] = document.get("nickName").toString();
                                }
                            }
                        } else {
                        }
                    }
                });

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
                System.out.println(commentKey+"aass");

                Person person = dataSnapshot.getValue(Person.class);
                personArrayList.add(person);

                adapter.addItem(new Person(commentKey));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());

            }
        };

        System.out.println(user.getEmail() +"ehlfkwha");
        DatabaseReference ref = database.getReference("friend").child("vwxyz");
        ref.addChildEventListener(childEventListener);
        System.out.println(nn[0]+"qqq");

        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(recyclerView);

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button cb = getActivity().findViewById(R.id.CB);
        final Button fb = getActivity().findViewById(R.id.FB);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cb.setBackgroundResource(R.drawable.button_on);
                fb.setBackgroundResource(R.drawable.button);
                NavHostFragment.findNavController(FriendsListFragment.this).navigate(R.id.friends_to_chat);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}