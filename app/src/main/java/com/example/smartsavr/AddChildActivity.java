package com.example.smartsavr;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

    private ActivityAddChildBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    List<String> cnames = new ArrayList<>();
    boolean flag = false;
    String parentID;

    ProfilePictureAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeProfilePicturesRecyclerView();


        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_add_child);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.saveChildButton.setOnClickListener(v -> {
            String childName = binding.nameFieldEditText.getText().toString();
            int weeklyAllowance = Integer.parseInt(binding.weeklyAllowanceFieldEditText.getText().toString());
            String username = binding.usernameFieldEditText.getText().toString();
            String password = binding.passwordFieldEditText.getText().toString();

            int profilePicture = adapter.getSelectedItem();
            //Log the selected item
            Log.d("TAG", "Selected item: " + profilePicture);

            int accountBalance = 0;
            int choresCompleted = 0;

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null)
            {
              parentID =user.getEmail();
            }


            // get the child name into an string list and check before registering new children whether username is unique

           //CollectionReference childrenref = firebaseFirestore.collection("children");

            firebaseFirestore.collection("children").whereNotEqualTo("username",null).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {

                            for(QueryDocumentSnapshot document : task.getResult())
                            {

                                //Log.d("Tag", document.getId() + " => " + document.getData());
                                cnames.add(Objects.requireNonNull(document.getData().get("username")).toString());

                            }
                            flag = true;
                        }

                    });

            // to do -- > toast message not visible when duplicate usernames
            if(flag)
            {
                if(cnames.contains(username))
                {
                    Log.d("Tag",cnames.toString());

                    Toast.makeText(AddChildActivity.this,"UserName exists already", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Child child = new Child(childName, parentID, weeklyAllowance, username, password, accountBalance, profilePicture, choresCompleted);

                    //  firebaseFirestore.collection("Children").document(doc_id).set(child);

                    firebaseFirestore.collection("children")
                            .add(child)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(AddChildActivity.this,"Child Added Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddChildActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show());

                }

            }
        });
    }

    private void initializeProfilePicturesRecyclerView() {
        adapter = new ProfilePictureAdapter();
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
