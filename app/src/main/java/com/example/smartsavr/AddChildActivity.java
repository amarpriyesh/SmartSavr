package com.example.smartsavr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.smartsavr.databinding.ActivityAddChildBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AddChildActivity extends AppCompatActivity {

    private static final int MAX_PROFILE_PICTURE_ID = 15;

    private ActivityAddChildBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String parentID;

    ProfilePictureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Child child = (Child) intent.getSerializableExtra(Utils.CHILD);

        int selectedProfilePictureId = 0;

        if (child != null) {
            binding.nameFieldEditText.setText(child.getName());
            binding.weeklyAllowanceFieldEditText.setText(Utils.centsToDollarString(child.getWeeklyAllowanceCents(), false));
            binding.usernameFieldEditText.setText(child.getUsername());
            //binding.passwordFieldEditText.setText(child.getPassword());
            if (child.getProfilePicture() >= 0 && child.getProfilePicture() <= MAX_PROFILE_PICTURE_ID) {
                selectedProfilePictureId = child.getProfilePicture();
            }
        }

        initializeProfilePicturesRecyclerView(selectedProfilePictureId);

        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (child == null) {
                actionBar.setTitle(R.string.activity_add_child);
            } else {
                actionBar.setTitle(getResources().getString(R.string.activity_edit_profile, child.getName()));
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.saveChildButton.setOnClickListener(saveChildClickListener(child));
    }

    private View.OnClickListener saveChildClickListener(Child prevChild) {
        return v -> {
            String childName = binding.nameFieldEditText.getText().toString();
            int weeklyAllowanceCents = Utils.dollarStringToCents(binding.weeklyAllowanceFieldEditText.getText().toString());
            String username = binding.usernameFieldEditText.getText().toString();
            String password = binding.passwordFieldEditText.getText().toString();

            int profilePicture = adapter.getLastCheckedPos();
            //Log the selected item
            Log.d("TAG", "Selected item: " + profilePicture);


            if(TextUtils.isEmpty(childName))
            {
                Toast.makeText(AddChildActivity.this,"Enter childname",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(username))
            {
                Toast.makeText(AddChildActivity.this,"Enter child username ",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(password))
            {
                Toast.makeText(AddChildActivity.this,"Create a password ",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(TextUtils.isEmpty(username))
            {
                Toast.makeText(AddChildActivity.this,"Enter child username ",Toast.LENGTH_SHORT).show();
                return;
            }
            else if(weeklyAllowanceCents <= 0 || TextUtils.isEmpty(Integer.toString(weeklyAllowanceCents)))
            {
                Toast.makeText(AddChildActivity.this,"Enter Valid Allowance ",Toast.LENGTH_SHORT).show();
                return;
            }
            else {

                int pcode = password.hashCode();
                String hpass = Integer.toString(pcode);


                int accountBalanceCents = 0;
                int choresCompleted = 0;

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    parentID = user.getEmail();
                }


                // get the child name into an string list and check before registering new children whether username is unique

                //CollectionReference childrenref = firebaseFirestore.collection("children");

                // todo: make this check for duplicate usernames more efficient
                firebaseFirestore.collection("children").whereNotEqualTo("username", null).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Child child = new Child(childName, parentID, weeklyAllowanceCents, username, hpass, accountBalanceCents, profilePicture, choresCompleted);
                                if (prevChild == null) {
                                    boolean containsUsername = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Object otherUsername = document.getData().get("username");
                                        if (otherUsername != null && otherUsername.toString().equals(username)) {
                                            containsUsername = true;
                                        }
                                    }
                                    if (containsUsername) {
                                        Log.d("Tag", String.format("Database already contains other child with username %s", username));

                                        Toast.makeText(AddChildActivity.this, "Username exists already", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //  firebaseFirestore.collection("children").document(doc_id).set(child);

                                        firebaseFirestore.collection("children")
                                                .add(child)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(AddChildActivity.this, "Child added successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(this, ParentHomeActivity.class));
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(AddChildActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }
                                } else {
                                    String childId = prevChild.getId();
                                    if (childId == null) {
                                        Toast.makeText(this, "There was an error editing the child's profile", Toast.LENGTH_SHORT).show();
                                    } else {
                                        boolean containsUsername = false;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String otherId = document.getId();
                                            Object otherUsername = document.getData().get("username");
                                            if (otherUsername != null && otherUsername.toString().equals(username) && !otherId.equals(prevChild.getId())) {
                                                containsUsername = true;
                                            }
                                        }

                                        if (containsUsername) {
                                            Log.d("Tag", String.format("Database already contains other child with username %s", username));

                                            Toast.makeText(AddChildActivity.this, "Username exists already", Toast.LENGTH_SHORT).show();
                                        } else {
                                            firebaseFirestore.collection("children").document(childId).set(child)
                                                    .addOnSuccessListener(documentReference -> {
                                                        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(this, ParentHomeActivity.class));
                                                    })
                                                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                    }
                                }
                            }

                        });


                // end here
            }
        };
    }

    private void initializeProfilePicturesRecyclerView(int selectedProfilePictureId) {
        adapter = new ProfilePictureAdapter(selectedProfilePictureId);
        binding.profilePictureRecyclerView.setAdapter(adapter);
        binding.profilePictureRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        List<ProfilePictureItem> profilePictures = Arrays.asList(
                new ProfilePictureItem(R.drawable.asset_1),
                new ProfilePictureItem(R.drawable.asset_2),
                new ProfilePictureItem(R.drawable.asset_3),
                new ProfilePictureItem(R.drawable.asset_4),
                new ProfilePictureItem(R.drawable.asset_5),
                new ProfilePictureItem(R.drawable.asset_6),
                new ProfilePictureItem(R.drawable.asset_7),
                new ProfilePictureItem(R.drawable.asset_8),
                new ProfilePictureItem(R.drawable.asset_9),
                new ProfilePictureItem(R.drawable.asset_10),
                new ProfilePictureItem(R.drawable.asset_11),
                new ProfilePictureItem(R.drawable.asset_12),
                new ProfilePictureItem(R.drawable.asset_13),
                new ProfilePictureItem(R.drawable.asset_14),
                new ProfilePictureItem(R.drawable.asset_15),
                new ProfilePictureItem(R.drawable.asset_16));

        adapter.submitList(profilePictures);
        adapter.notifyItemRangeInserted(0, profilePictures.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
