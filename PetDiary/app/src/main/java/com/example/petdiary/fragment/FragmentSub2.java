package com.example.petdiary.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.petdiary.Comment;
import com.example.petdiary.CustomAdapter;
import com.example.petdiary.CustomAdapterSub;
import com.example.petdiary.Data;
import com.example.petdiary.DataSub;
import com.example.petdiary.Expand_ImageView;
import com.example.petdiary.R;
import com.example.petdiary.adapter.ViewPageAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class FragmentSub2 extends Fragment {

    ViewGroup viewGroup;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<DataSub> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_sub, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)viewGroup.findViewById(R.id.swipe_layout);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        loding();

        mSwipeRefreshLayout.setDistanceToTriggerSync(400);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loding();

                // 동글동글 도는거 사라짐
                mSwipeRefreshLayout.setRefreshing(false);

                // TODO : input your code
            }
        });

        return viewGroup;
    }

    private void loding(){
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("images"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    DataSub Datalist = snapshot.getValue(DataSub.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(0,Datalist); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                MyAdapter2 adapter = new MyAdapter2 (
                        getActivity().getApplicationContext(),
                        R.layout.row,       // GridView 항목의 레이아웃 row.xml
                        arrayList);    // 데이터

                GridView gv = (GridView) viewGroup.findViewById(R.id.gridView1);
                gv.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("FragmentSub", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }
}

class MyAdapter2 extends BaseAdapter {

    private ArrayList<DataSub> arrayList;
    private Context context;
    int layout;
    LayoutInflater inf;

    public MyAdapter2(Context context, int layout, ArrayList<DataSub> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        if(arrayList.get(position).getImageUrl1().length() > 0){
            Glide.with(context).load(arrayList.get(position).getImageUrl1()).centerCrop().override(500).into(iv);
        } else {
            //Glide.with(context).load(R.drawable.ic_baseline_photo_24).fitCenter().override(500).into(iv);
        }
        return convertView;
    }
}
