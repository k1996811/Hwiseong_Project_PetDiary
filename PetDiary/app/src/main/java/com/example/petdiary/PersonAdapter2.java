package com.example.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PersonAdapter2 extends RecyclerView.Adapter<PersonAdapter2.ViewHolder> implements ItemTouchHelperListener {

    ArrayList<Person> items = new ArrayList<Person>();
    private OnItemClickListener mListener = null ;


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

        public ViewHolder(View itemView){
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
