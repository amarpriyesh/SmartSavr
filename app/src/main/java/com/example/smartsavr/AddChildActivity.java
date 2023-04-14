package com.example.smartsavr;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.smartsavr.databinding.FragmentAddChildBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class AddChildActivity extends AppCompatActivity {

    private FragmentAddChildBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    List<String>names = new ArrayList<String>();





    String fk;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Random random = new Random();
        progressDialog = new ProgressDialog(this);



        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_add_child);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }






        //setClickListeners();

        binding.saveChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String child_name = binding.nameFieldEditText.getText().toString();
                int weekly_allowance = Integer.parseInt(binding.weeklyAllowanceFieldEditText.getText().toString());

                Random random = new Random();
                int temp = 0 + random.nextInt(100);
                String doc_id = child_name + temp;


                firebaseAuth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                  fk=user.getEmail();
                }






            // CHECK FROM HERE ------------------------------------------
            // get the child name into an string list and check before registering new children whether name is unique

               //CollectionReference childrenref = firebaseFirestore.collection("children");

                firebaseFirestore.collection("Children").whereNotEqualTo("name",null).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {

                                    for(QueryDocumentSnapshot document : task.getResult())
                                    {

                                        Log.d("Tag", document.getId() + " => " + document.getData());





                                    }
                                }

                            }
                        });








                Child child = new Child(child_name,fk,weekly_allowance);

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
        });



        initializeProfilePicturesRecyclerView();
    }

    private void setClickListeners() {
        // todo: move this functionality to the child chore management screen when it's implemented
        binding.saveChildButton.setOnClickListener(view -> {
            ChoreBottomSheetDialog bottomSheet = new ChoreBottomSheetDialog();
            bottomSheet.show(getSupportFragmentManager(), ChoreBottomSheetDialog.TAG);
        });
    }

    private void initializeProfilePicturesRecyclerView() {
        ProfilePictureAdapter adapter = new ProfilePictureAdapter();
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
                new ProfilePictureItem(R.drawable.asset_8));
        adapter.submitList(profilePictures);
        adapter.notifyItemRangeInserted(0, profilePictures.size());
    }








}
