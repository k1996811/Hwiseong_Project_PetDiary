package com.example.petdiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petdiary.Comment;
import com.example.petdiary.Expand_ImageView;
import com.example.petdiary.R;

public class FragmentMain extends Fragment {
    ViewGroup viewGroup;

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

        viewGroup.findViewById(R.id.main_image).setOnClickListener(onClickListener);
        viewGroup.findViewById(R.id.button2).setOnClickListener(onClickListener);
        viewGroup.findViewById(R.id.onPopupButton).setOnClickListener(onClickListener);

        return viewGroup;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.main_image:
                    myStartActivity(Expand_ImageView.class);
                    break;
                case R.id.button2:
                    myStartActivity(Comment.class);
                    break;
                case R.id.onPopupButton:
                    PopupMenu popup = new PopupMenu(getActivity(), viewGroup);

                    //설정한 popup XML을 inflate.
                    popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                    //팝업메뉴 클릭 시 이벤트
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.search:
                                    /* Search를 선택했을 때 이벤트 실행 코드 작성 */
                                    break;

                                case R.id.add:
                                    /* Add를 선택했을 때 이벤트 실행 코드 작성 */
                                    break;

                                case R.id.edit:
                                    /* Edit를 선택했을 때 이벤트 실행 코드 작성 */
                                    break;

                                case R.id.share:
                                    /* Share를 선택했을 때 이벤트 실행 코드 작성 */
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }

}
