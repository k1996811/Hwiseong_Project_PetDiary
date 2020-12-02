package com.example.petdiary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.activity.ChatActivity;
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
>>>>>>> 89370bafe96a75337cf2c320e042f572c6c5ff13

import java.util.ArrayList;
import java.util.Arrays;

public class PersonAdapter2 extends RecyclerView.Adapter<PersonAdapter2.ViewHolder> implements ItemTouchHelperListener {

    ArrayList<Person> items = new ArrayList<Person>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private OnItemClickListener mListener = null ;

    private DatabaseReference mDatabase;

    private Context mContext;

    public PersonAdapter2(Context mContext){
        this.mContext = mContext;
    }

    public void setOnitemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nick;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        public ViewHolder(final View itemView){
            super(itemView);

            nick = itemView.findViewById(R.id.textView);

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Person item = items.get(pos);
                        if (mListener != null) {

                            mListener.onItemClick(v, pos);
                        }
                    }
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    intent.putExtra("nickName",nick.getText());
                    intent.putExtra("my",nn[0]);
                    mContext.startActivity(intent);
                }
            });
            nick = itemView.findViewById(R.id.textView);
        }
        public void setItem(Person item){
            nick.setText(item.getNickname());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.person_item2, viewGroup, false);

        return new ViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder , int position){
        Person item = items.get(position);
        viewHolder.setItem(item);
    }
    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        String nn[] = new String[2];
        nn[0] = "";
        nn[1] = "";
        nn[0] = FirebaseAuth.getInstance().getCurrentUser().getUid();
        nn[1] = items.get(position).getUid();
        Log.e("###", nn[0] + " /// " + nn[1]);
        //Arrays.sort(nn);

        mDatabase = FirebaseDatabase.getInstance().getReference("chat/"+ nn[0] + "&" + nn[1]);
        Log.e("PersonAdapter2", mDatabase.toString());
        //mDatabase.removeValue();
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    public void addItem(Person item){
        items.add(item);
    }
    public void setItems(ArrayList<Person> items){
        this.items  = items;
    }
    public Person getItem(int position){
        return items.get(position);
    }
    public void setItem(int position, Person item){
        items.set(position,item);
    }


}
