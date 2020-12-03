package com.example.petdiary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petdiary.Chat;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Chat> mDataset;
    String stMyEmail = "";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public TextView nickChat;
        public ImageView chatProfile;

        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tvChat);
            nickChat = v.findViewById(R.id.nickChat);
            chatProfile = v.findViewById(R.id.chatProfile);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        return 0;
    }

    public CommentAdapter(ArrayList<Chat> myDataset, String stEmail, Context context) {
        mDataset = myDataset;
        this.stMyEmail = stEmail;
        this.context = context;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("email",mDataset.get(position).getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                holder.nickChat.setText(document.getData().get("nickName").toString());
                                if(document.getData().get("profileImg").toString().length() > 0){
                                    Glide.with(context).load(document.getData().get("profileImg").toString()).centerCrop().override(500).into(holder.chatProfile);
                                } else {
                                    holder.chatProfile.setImageResource(R.drawable.icon_person);
                                }

                                break;
                            }
                        } else {
                            Log.d("###", "Error getting documents: ", task.getException());
                        }
                    }
                });

        holder.textView.setText(mDataset.get(position).getText());
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
