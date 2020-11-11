package com.example.petdiary.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petdiary.ItemTouchHelperCallback;
import com.example.petdiary.Person;
import com.example.petdiary.PersonAdapter;
import com.example.petdiary.R;

public class FriendsListFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        RecyclerView recyclerView = root.findViewById(R.id.fri_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        PersonAdapter adapter = new PersonAdapter(getContext());
        adapter.addItem(new Person("휘"));

        final Button chat = root.findViewById(R.id.btn_chat);

        adapter.setOnitemClickListener(new PersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                    Toast.makeText(getContext(), "Authentication failed.",
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

        final Button cb = getActivity().findViewById(R.id.CB);
        final Button fb = getActivity().findViewById(R.id.FB);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cb.setBackgroundResource(R.drawable.button_on);
                fb.setBackgroundResource(R.drawable.button);
                NavHostFragment.findNavController(FriendsListFragment.this).navigate(R.id.friends_to_chat);

            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}