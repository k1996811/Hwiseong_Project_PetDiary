package com.example.petdiary.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.petdiary.CustomAdapterSub;
import com.example.petdiary.Data;
import com.example.petdiary.R;
import com.example.petdiary.RecyclerDecorationWidth;
import com.example.petdiary.RecyclerViewDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentSub extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Data> arrayList;
    private View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SearchView searchView;
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sub, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);

        moveTop();

        searchView = view.findViewById(R.id.search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override // 검색 버튼 눌렀을 때
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getContext(), "검색!!!", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override // 글자 변경시
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(5));
        //recyclerView.addItemDecoration(new RecyclerDecorationWidth(30));
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        setInfo();

        adapter = new CustomAdapterSub(arrayList, getContext());
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        } else {
            moveTop();
        }
    }

    private void moveTop(){
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.tab2){
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    private void setInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data dataList = new Data();
                                dataList.setPostID(document.getId());
                                dataList.setUid(document.getData().get("uid").toString());
                                dataList.setContent(document.getData().get("content").toString());
                                dataList.setImageUrl1(document.getData().get("imageUrl1").toString());
                                dataList.setImageUrl2(document.getData().get("imageUrl2").toString());
                                dataList.setImageUrl3(document.getData().get("imageUrl3").toString());
                                dataList.setImageUrl4(document.getData().get("imageUrl4").toString());
                                dataList.setImageUrl5(document.getData().get("imageUrl5").toString());
                                dataList.setNickName(document.getData().get("nickName").toString());
                                dataList.setDate(document.getData().get("date").toString());
                                dataList.setCategory(document.getData().get("category").toString());
                                dataList.setEmail(document.getData().get("email").toString());
                                dataList.setFavoriteCount(Integer.parseInt(document.getData().get("favoriteCount").toString()));
                                arrayList.add(0, dataList);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("###", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}