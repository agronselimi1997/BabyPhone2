package com.example.babyphone2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.babyphone2.Home;
import com.example.babyphone2.R;
import com.example.babyphone2.Strings;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.databinding.FragmentFirstBinding;
import com.example.babyphone2.models.Phone;
import com.example.babyphone2.Strings;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.models.Phone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LogIn extends Fragment {
    private FragmentFirstBinding binding;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private EditText emailEtx, passwordEtx;
    private final FirebaseWorkerImpl firebaseWorker = new FirebaseWorkerImpl();
    private ProgressBar progressBar;
    private Strings values;
    private String phoneName;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String currentUserId;
    private ArrayList<Phone> phones;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        values = Strings.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference("Users").child("Phones");
        phones = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState

    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = binding.logInProgressBar;
        progressBar.setVisibility(View.GONE);
        emailEtx = binding.emailEditText;
        passwordEtx = binding.passwordEditText;

        binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.emailEditText.getText().toString();
                password = binding.passwordEditText.getText().toString();
                passwordEtx.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                || i == EditorInfo.IME_ACTION_DONE) {
                            // Do your action
                            // return true;
                            if(validate(email,password)){
                                logIn(email,password);
                            }
                        }
                        return false;
                    }
                });
                if (validate(email, password)){
                    logIn(email,password);
                }
            }
        });

    }

    private boolean validate(String email, String password) {
        if(email.isEmpty()){
            emailEtx.setError("Email is required");
            emailEtx.requestFocus();
            return false;
        }else
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEtx.setError("Email is not valid");
            emailEtx.requestFocus();
            return false;
        }else
        if(password.length() < 6){
            return false;
        }else
            return  true;

    }

    private void createAccount() {
        NavHostFragment.findNavController(LogIn.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }
    private void logIn(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    currentUserId = mAuth.getCurrentUser().getUid().toString();
                    phoneName = values.getPhoneName();
                    if(phoneName == null){
                        NavHostFragment.findNavController(LogIn.this)
                                .navigate(R.id.action_FirstFragment_to_childFather);
                    }else if(phones.contains(firebaseWorker.getPhone(phoneName))){
                        Intent intent = new Intent(getContext(), Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else {
                        NavHostFragment.findNavController(LogIn.this)
                                .navigate(R.id.action_FirstFragment_to_childFather);
                    }
                }else{
                   emailEtx.setError("Invalid password or email");
                   emailEtx.requestFocus();
                   passwordEtx.setText(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("LogIn","Failed to log in");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}