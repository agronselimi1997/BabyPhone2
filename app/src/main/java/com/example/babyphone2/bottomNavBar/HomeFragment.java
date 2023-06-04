package com.example.babyphone2.bottomNavBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.babyphone2.adapters.PhoneListViewAdapter;
import com.example.babyphone2.databinding.FragmentHomeBinding;
import com.example.babyphone2.models.Phone;
import com.example.babyphone2.viewModels.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
     private FragmentHomeBinding binding;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ValueEventListener valueEventListener;
    private List<Phone> phones;
    private ListView phonesListView;
    private PhoneListViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        phonesListView = binding.phoneList;
        phones = new ArrayList<Phone>();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef= database.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Phones");

        valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                Phone phone = dataSnapshot.getValue(Phone.class);
                phones.add(phone);
                    Log.d("Snapshot", "Test");
                }
                adapter = new PhoneListViewAdapter(getActivity(),phones);
                phonesListView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addValueEventListener(valueEventListener);


        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}