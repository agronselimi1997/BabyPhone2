package com.example.babyphone2.fragments;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.babyphone2.R;
import com.example.babyphone2.dao.FirebaseWorkerImpl;
import com.example.babyphone2.databinding.FragmentSecondBinding;
import com.example.babyphone2.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentSecondBinding binding;
    private String email,password, name, surname, retypePassword;
    private EditText emailEtx, passwordEtx, nameEtx, surnameEtx,rPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseWorkerImpl firebaseWorker;
    private String id;
    TelephonyManager telephonyManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseWorker = new FirebaseWorkerImpl();
        mAuth = firebaseWorker.getAuth();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEtx = binding.registerEmailEtx;
        passwordEtx = binding.signUpPasswordEtx;
        nameEtx = binding.registerNameEtx;
        surnameEtx = binding.registerSurnameEtx;
        rPassword = binding.retyppePasswordEtx;


        binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.registerEmailEtx.getText().toString();
                name = binding.registerNameEtx.getText().toString();
                surname = binding.registerSurnameEtx.getText().toString();
                password= binding.signUpPasswordEtx.getText().toString();
                retypePassword = binding.retyppePasswordEtx.getText().toString();
                register(name,surname,email,password,retypePassword);
            }
        });

        binding.backToLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Register.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    private void register(String name, String surname, String email, String password, String retypePassword) {
        if(name.isEmpty()){
            nameEtx.setError("Please enter a name");
            nameEtx.requestFocus();
            return;
        }
        if(surname.isEmpty()){
            surnameEtx.setError("Please enter a surname");
            surnameEtx.requestFocus();
            return;
        }
        if(email.isEmpty()){
            emailEtx.setError("Email is required");
            emailEtx.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEtx.setError("Email is not valid");
            emailEtx.requestFocus();
            return;
        }
        if(password.length() < 6 || !Objects.equals(password,retypePassword)){
                passwordEtx.setError("Invalid Passwords");
                passwordEtx.setText(null);
                rPassword.setText(null);
                passwordEtx.requestFocus();
                return;

        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            User user = new User(email,name,surname,password);
                            FirebaseDatabase data1 = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = data1.getReference("Users");
                            myRef.child(Objects.requireNonNull(currentUser).getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        NavHostFragment.findNavController(Register.this)
                                                .navigate(R.id.action_SecondFragment_to_FirstFragment);
                                        Log.d("sucess", "sucess add user");
                                    }else {
                                        Log.d("FailUser" ,"Fail User ");
                                    }
                                }
                            });
/*
                            if(firebaseWorker.addUser(user)){
                                Log.d("Sucess", "Success added user");
                            }else {
                                Log.d("Add User Error", task.getException().getMessage().toString());
                            }
                            */
                        }else{
                            Log.d("Error", task.getException().getMessage());
                        }
                    }
                });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}