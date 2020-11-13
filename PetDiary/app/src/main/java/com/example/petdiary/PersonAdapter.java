package com.example.petdiary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.activity.ChatActivity;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> implements ItemTouchHelperListener {
    private ArrayList<Chat> mDataset;

    String stMyEmail;

    ArrayList<Person> items = new ArrayList<Person>();
    private OnItemClickListener mListener = null;
    private Context mContext;

    public PersonAdapter(Context mContext){
        this.mContext = mContext;
    }
    public PersonAdapter(ArrayList<Chat> myDataset, String stEmail) {
        mDataset = myDataset;
        this.stMyEmail = stEmail;
    }

    public void setOnitemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button chat;
        TextView nick;


        public ViewHolder(final View itemView){
            super(itemView);

            nick = itemView.findViewById(R.id.textView);
            textView = itemView.findViewById(R.id.tvChat);
            chat = itemView.findViewById(R.id.btn_chat);

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    //intent.putExtra("email",stMyEmail );
                    mContext.startActivity(intent);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Person item = items.get(pos);
                        if(mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
        public void setItem(Person item){
            nick.setText(item.getNickname());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.person_item, viewGroup, false);

        return new ViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder , int position){

        Person item = items.get(position);
        viewHolder.setItem(item);
        //viewHolder.nick.setText(mDataset.get(position).getEmail());

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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    public boolean onItemMove(int from_position, int to_position) {
        //이동할 객체 저장
        Person person = items.get(from_position);
        //이동할 객체 삭제
        items.remove(from_position); //이동하고 싶은 position에 추가
        items.add(to_position,person);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position,to_position);
        return true;
    }

    public void onItemSwipe(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView list_name,list_age;
        ImageView list_image;
        Button list_button;
        public ItemViewHolder(View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.textView);
            list_image = itemView.findViewById(R.id.imageView);
            list_button = itemView.findViewById(R.id.btn_chat);
        }
        public void onBind(Person person) {
            list_name.setText(person.getNickname());
//            list_age.setText(String.valueOf(person.getMobile()));

        }
    }

}
