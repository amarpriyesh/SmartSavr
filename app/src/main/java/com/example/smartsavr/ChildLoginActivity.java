package com.example.smartsavr;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsavr.databinding.ActivityChildLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class ChildLoginActivity extends AppCompatActivity {

    ActivityChildLoginBinding binding;


    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    HashMap<String, String> mp = new HashMap<>();
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.log_in);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.childUsername.getText().toString();
                String password = binding.password.getText().toString();

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();


                firebaseFirestore.collection("children").whereNotEqualTo("username",null).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {

                                    for(QueryDocumentSnapshot document : task.getResult())
                                    {

                                        //Log.d("Tag", document.getId() + " => " + document.getData());
                                        mp.put(document.getData().get("username").toString(),
                                                document.getData().get("password").toString());








                                    }
                                    flag = true;
                                }

                            }
                        });

                if(flag == true)
                {
                    if (mp.keySet().contains(username)) {
                        if (mp.get(username).equals(password)) {
                            // initiate child home activity
                            Toast.makeText(ChildLoginActivity.this, "Logged in ", Toast.LENGTH_SHORT).show();
                        }



                    }
                    else
                    {
                        Toast.makeText(ChildLoginActivity.this, "Invalid Creds ", Toast.LENGTH_SHORT).show();
                    }
                }






















            }
        });
    }
}



