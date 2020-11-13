package com.example.petdiary.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.R;
import com.example.petdiary.ItemTouchHelperCallback;
import com.example.petdiary.Person;
import com.example.petdiary.PersonAdapter2;
import com.example.petdiary.ui.friends.FriendsListFragment;

public class ChatListFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentManager fragmentManager;
    private Fragment fragmentA, fragmentB;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chat, container, false);



        final RecyclerView recyclerView = root.findViewById(R.id.chat_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        final PersonAdapter2 adapter = new PersonAdapter2(getContext());

        adapter.addItem(new Person("성"));

        adapter.setOnitemClickListener(new PersonAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(getContext(), "aaa",
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(recyclerView);

        return root;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        final Button fb = getActivity().findViewById(R.id.FB);
        final Button cb = getActivity().findViewById(R.id.CB);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fb.setBackgroundResource(R.drawable.button_on);
                cb.setBackgroundResource(R.drawable.button);
                NavHostFragment.findNavController(ChatListFragment.this).navigate(R.id.chat_to_friends);


            }
        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}