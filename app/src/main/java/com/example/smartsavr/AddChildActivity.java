package com.example.smartsavr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsavr.databinding.FragmentAddChildBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class AddChildActivity extends AppCompatActivity {

    private FragmentAddChildBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    List<String> cnames = new ArrayList<>();
    boolean flag = false;
    String parentID;
    ProgressDialog progressDialog;

    ProfilePictureAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        initializeProfilePicturesRecyclerView();


        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_add_child);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.saveChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String child_name = binding.nameFieldEditText.getText().toString();
                int weekly_allowance = Integer.parseInt(binding.weeklyAllowanceFieldEditText.getText().toString());
                String username = binding.usernameFieldEditText.getText().toString();
                String password = binding.passwordFieldEditText.getText().toString();

                int profilePictureID = adapter.getSelectedItem();
                //Log the selected item
                Log.d("TAG", "Selected item: " + profilePictureID);

                int account_bal = 0;

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                  parentID =user.getEmail();
                }


            // CHECK FROM HERE ------------------------------------------
            // get the child name into an string list and check before registering new children whether username is unique

               //CollectionReference childrenref = firebaseFirestore.collection("children");

                firebaseFirestore.collection("children").whereNotEqualTo("username",null).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {

                                    for(QueryDocumentSnapshot document : task.getResult())
                                    {

                                        //Log.d("Tag", document.getId() + " => " + document.getData());
                                        cnames.add(document.getData().get("username").toString());

                                    }
                                    flag = true;
                                }

                            }
                        });

                // to do -- > toast message not visible when duplicate usernames
                if(flag)
                {
                    if(cnames.contains(username))
                    {
                        Log.d("Tag",cnames.toString());

                        Toast.makeText(AddChildActivity.this,"UserName exists already", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        Child child = new Child(child_name, parentID,weekly_allowance,username,password,account_bal, profilePictureID);

                        //  firebaseFirestore.collection("Children").document(doc_id).set(child);

                        firebaseFirestore.collection("children")
                                .add(child)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(AddChildActivity.this,"Child Added Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddChildActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }

                }
            }
        });
    }

    private void setClickListeners() {
        // todo: move this functionality to the child chore management screen when it's implemented
        binding.saveChildButton.setOnClickListener(view -> {
            ChoreBottomSheetDialog bottomSheet = new ChoreBottomSheetDialog();
            bottomSheet.show(getSupportFragmentManager(), ChoreBottomSheetDialog.TAG);
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








}
