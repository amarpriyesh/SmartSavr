package com.example.smartsavr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChildProfile_parentlogin extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile_parentlogin);
        Intent intent = getIntent();
        Child child = (Child) intent.getSerializableExtra("child");

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(child.getProfilePicture());

        //Having an issue with string resources here
        TextView choresCompletedTV = findViewById(R.id.chores_completed);
        choresCompletedTV.setText("Chores Completed: "  + child.getChoresCompleted());

        TextView currentBalanceTV = findViewById(R.id.current_balance);
        currentBalanceTV.setText(("Account Balance: $" + child.getAccountBalance()));

        TextView allowanceTV = findViewById(R.id.allowance);
        allowanceTV.setText("Allowance: $" + child.getWeeklyAllowance() + " per week");

        TextView nameTV = findViewById(R.id.child_name);
        nameTV.setText(child.getName());

        Button modifyBalance = findViewById(R.id.modify_balance);
        modifyBalance.setOnClickListener(view -> {
            //TODO: Bring up bottom sheet to modify balance
        });

        Button manageChores = findViewById(R.id.manage);
        manageChores.setOnClickListener(view -> {
            //TODO: Navigate to manage chores page
        });

        Button allowanceSummary = findViewById(R.id.allowance_summary);
        allowanceSummary.setOnClickListener(view -> {
            //TODO: Navigate to allowance summary page
        });

    }


}