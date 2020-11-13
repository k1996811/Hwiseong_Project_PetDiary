package com.example.petdiary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.activity.ChatActivity;

import java.util.ArrayList;

public class PersonAdapter2 extends RecyclerView.Adapter<PersonAdapter2.ViewHolder> implements ItemTouchHelperListener {

    ArrayList<Person> items = new ArrayList<Person>();
    private OnItemClickListener mListener = null ;

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
        TextView textView;

        public ViewHolder(final View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
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
                    //intent.putExtra("email",stMyEmail );
                    mContext.startActivity(intent);
                }
            });
            textView = itemView.findViewById(R.id.textView);
        }
        public void setItem(Person item){
            textView.setText(item.getNickname());
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
