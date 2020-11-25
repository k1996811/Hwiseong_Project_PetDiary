package com.example.petdiary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.petdiary.CustomAdapter;
import com.example.petdiary.Data;
import com.example.petdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentMain extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Data> arrayList;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        setInfo();

        adapter = new CustomAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        //새로고침
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                setInfo();
                // 동글동글 도는거 사라짐
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void setInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //db.collection("post").whereEqualTo("uid", "Wvc9odc25MYAkejxIgUPTAr4e322")
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data dataList = new Data();
                                dataList.setUid(document.getData().get("uid").toString());
                                dataList.setContent(document.getData().get("content").toString());
                                dataList.setImageUrl1(document.getData().get("imageUrl1").toString());
                                dataList.setImageUrl2(document.getData().get("imageUrl2").toString());
                                dataList.setImageUrl3(document.getData().get("imageUrl3").toString());
                                dataList.setImageUrl4(document.getData().get("imageUrl4").toString());
                                dataList.setImageUrl5(document.getData().get("imageUrl5").toString());
                                dataList.setNickName(document.getData().get("nickName").toString());
                                arrayList.add(0, dataList);
//                                if(document.getData().get("uid").toString().equals("muyMpkUCtxfScQQAZoVBv2USE3m1") || document.getData().get("uid").toString().equals("Wvc9odc25MYAkejxIgUPTAr4e322")){
//                                    dataList.setUid(document.getData().get("uid").toString());
//                                    dataList.setContent(document.getData().get("content").toString());
//                                    dataList.setImageUrl1(document.getData().get("imageUrl1").toString());
//                                    dataList.setImageUrl2(document.getData().get("imageUrl2").toString());
//                                    dataList.setImageUrl3(document.getData().get("imageUrl3").toString());
//                                    dataList.setImageUrl4(document.getData().get("imageUrl4").toString());
//                                    dataList.setImageUrl5(document.getData().get("imageUrl5").toString());
//                                    dataList.setNickName(document.getData().get("nickName").toString());
//                                    arrayList.add(0, dataList);
//                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("###", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
