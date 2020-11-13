package com.example.petdiary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.Comment;
import com.example.petdiary.Data;
import com.example.petdiary.Expand_ImageView;
import com.example.petdiary.R;
import com.example.petdiary.adapter.recyclerAdapter;

import java.util.Arrays;
import java.util.List;

public class FragmentMain extends Fragment {

    ViewGroup viewGroup;
    private recyclerAdapter adapter;

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        init();
        getData();

        return viewGroup;
    }

//    View.OnClickListener onClickListener = new View.OnClickListener(){
////
////        @Override
////        public void onClick(View v) {
////            switch(v.getId()){
////                case R.id.main_image:
////                    myStartActivity(Expand_ImageView.class);
////                    break;
////                case R.id.button2:
////                    myStartActivity(Comment.class);
////                    break;
////                case R.id.onPopupButton:
////                    PopupMenu popup = new PopupMenu(getContext(), viewGroup);
////
////                    //설정한 popup XML을 inflate.
////                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
////
////                    //팝업메뉴 클릭 시 이벤트
////                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
////                        public boolean onMenuItemClick(MenuItem item) {
////                            switch (item.getItemId()) {
////                                case R.id.edit:
////                                    /* Search를 선택했을 때 이벤트 실행 코드 작성 */
////                                    break;
////
////                                case R.id.Delete:
////                                    /* Add를 선택했을 때 이벤트 실행 코드 작성 */
////                                    break;
////
////                                case R.id.share:
////                                    Intent msg = new Intent(Intent.ACTION_SEND);
////                                    msg.addCategory(Intent.CATEGORY_DEFAULT);
////                                    msg.putExtra(Intent.EXTRA_SUBJECT, "주제");
////                                    msg.putExtra(Intent.EXTRA_TEXT, "내용");
////                                    msg.putExtra(Intent.EXTRA_TITLE, "제목");
////                                    msg.setType("text/plain");
////                                    startActivity(Intent.createChooser(msg, "공유"));
////                                    break;
////                            }
////                            /* Share를 선택했을 때 이벤트 실행 코드 작성 */
////                            return true;
////                        }
////                    });
////                    popup.show();
////                    break;
////            }
////        }
////    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new recyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("댕댕이", "네로", "냐옹", "알파카", "코알라" ,"휘리다"
        );
        List<String> listContent = Arrays.asList(
                "댕댕이 너무 즐거워요 ㅎㅎㅎ #댕댕이 #산책",
                "저는 검은고양이 네로입니다 #검은고양이 #네로",
                "냐옹 냐옹 나랑 놀아달라! #냥집사 #냥냥",
                "알파카 처음보냐?! #침뱉기 #알파카",
                "이 동물은 코알라입니다.",
                "이것은 등대입니다."

        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.dog,
                R.drawable.cat1,
                R.drawable.cat2,
                R.drawable.alphaka,
                R.drawable.dog,
                R.drawable.dog


        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}
