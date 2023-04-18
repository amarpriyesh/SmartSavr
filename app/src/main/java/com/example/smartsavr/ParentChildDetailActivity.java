package com.example.smartsavr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ParentChildDetailActivity extends AppCompatActivity {

    static DBReference childDBReference;
    FirebaseFirestore firebaseFirestore;
    static CollectionReference collectionReference;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_child_detail);

        Intent intent = getIntent();
        Child child = (Child) intent.getSerializableExtra("child");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // TODO: Deal with null name (upon navigating via up button)
            actionBar.setTitle(child.getName() + "'s Chores");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("children");
        childDBReference = new DBReference(collectionReference,firebaseFirestore);

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(child.getProfilePicture());

        //Having an issue with string resources here
        TextView choresCompletedTV = findViewById(R.id.chores_completed);
        choresCompletedTV.setText("Chores Completed: "  + child.getChoresCompleted());

        TextView currentBalanceTV = findViewById(R.id.current_balance);
        currentBalanceTV.setText(("Account Balance: $" + child.getAccountBalanceCents()));

        TextView allowanceTV = findViewById(R.id.allowance);
        allowanceTV.setText("Allowance: $" + child.getWeeklyAllowanceCents() + " per week");

        TextView nameTV = findViewById(R.id.child_name);
        nameTV.setText(child.getName());

        setClickListeners(child);
    }

    private void setClickListeners(Child child) {
        Button modifyBalance = findViewById(R.id.modify_balance);
        modifyBalance.setOnClickListener(view -> {
            ModifyAllowanceBottomSheetDialog bottomSheet = new ModifyAllowanceBottomSheetDialog(child);
            bottomSheet.show(getSupportFragmentManager(), ModifyAllowanceBottomSheetDialog.TAG);
        });

        Button manageChores = findViewById(R.id.manage);
        manageChores.setOnClickListener(view -> {
            Intent intent = new Intent(this, ParentChildChoresActivity.class);
            intent.putExtra("child", child);
            startActivity(intent);
        });

        Button allowanceSummary = findViewById(R.id.allowance_summary);
        allowanceSummary.setOnClickListener(view -> {
            //TODO: Navigate to allowance summary page
        });
    }
}