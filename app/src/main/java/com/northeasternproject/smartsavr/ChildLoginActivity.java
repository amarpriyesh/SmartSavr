package com.northeasternproject.smartsavr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.northeasternproject.smartsavr.R;
import com.northeasternproject.smartsavr.databinding.ActivityChildLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChildLoginActivity extends AppCompatActivity {

    ActivityChildLoginBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

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

        binding.login.setOnClickListener(v -> {
            String username = binding.childUsername.getText().toString();
            String password = binding.password.getText().toString();

            if(TextUtils.isEmpty(username))
            {
                Toast.makeText(ChildLoginActivity.this,"Enter Username",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(ChildLoginActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                return;
            }
            else {

                int p_code = password.hashCode();
                String p_check = Integer.toString(p_code);


                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection("children").whereNotEqualTo("username", null).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, ChildUser> mp = new HashMap<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mp.put(Objects.requireNonNull(document.getData().get("username")).toString(), new ChildUser(document.getId(), Objects.requireNonNull(document.getData().get("password")).toString()));
                        }
                        ChildUser myUser = mp.get(username);
                        if (myUser != null && Objects.equals(myUser.getPassword(), p_check)) {
                            // initiate child home activity
                            Toast.makeText(ChildLoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, ChildHomeActivity.class);
                            intent.putExtra(Utils.CHILD_ID, myUser.getChildId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChildLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // end here
            }
        });
    }
}
