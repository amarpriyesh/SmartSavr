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
import com.northeasternproject.smartsavr.databinding.ActivityParentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ParentLoginActivity extends AppCompatActivity {

    ActivityParentLoginBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.log_in);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView pass = findViewById(R.id.resetpassword);
        pass.setTooltipText("Click to Receive a Reset Link on registered Email");

        TextView txt = findViewById(R.id.gotosignup);
        txt.setTooltipText("New user ? Click here to sign up ");

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString();


                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(ParentLoginActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(ParentLoginActivity.this,"Enter Password ",Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.cancel();
                                    startActivity(new Intent(ParentLoginActivity.this, ParentHomeActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(ParentLoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            });


                }






                binding.resetpassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String email = binding.email.getText().toString();

                        progressDialog.setTitle("Sending Reset-Email");
                        progressDialog.show();
                        firebaseAuth.sendPasswordResetEmail(email)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.cancel();
                                        Toast.makeText(ParentLoginActivity.this,"Email Sent",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.cancel();
                                        Toast.makeText(ParentLoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

            }
        });

        binding.gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentLoginActivity.this, SignUpActivity.class));

            }
        });
    }
}