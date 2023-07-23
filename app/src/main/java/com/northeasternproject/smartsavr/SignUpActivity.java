package com.northeasternproject.smartsavr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.northeasternproject.smartsavr.R;
import com.northeasternproject.smartsavr.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userid;
    int temp;



    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random random = new Random();
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.sign_up);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView login = findViewById(R.id.gotologin);
        login.setTooltipText("Already have an account ? Click here to sign in ");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.username.getText().toString();
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Enter Password ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(SignUpActivity.this, ParentLoginActivity.class));
                                    progressDialog.cancel();

                                    firebaseFirestore.collection("User")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .set(new UserModel(name, email));

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.cancel();

                                }
                            });

                }


            }
        });


        binding.gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ParentLoginActivity.class));

            }
        });
    }
}
