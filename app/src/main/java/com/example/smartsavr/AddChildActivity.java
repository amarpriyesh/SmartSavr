package com.example.smartsavr;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.smartsavr.databinding.FragmentAddChildBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class AddChildActivity extends AppCompatActivity {

    private FragmentAddChildBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    String fk,;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentAddChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Random random = new Random();



        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_add_child);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        String p_userid = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(p_userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fk = documentSnapshot.getString("userid");

            }
        });




        //setClickListeners();

        binding.saveChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String child_name = binding.nameFieldEditText.getText().toString();
                Editable weekly_allowance = binding.weeklyAllowanceFieldEditText.getText();
                int temp = 0+ random.nextInt(100);
                String child_id = child_name + temp;



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
