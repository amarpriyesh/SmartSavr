package com.example.smartsavr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ParentChildDetailActivity extends AppCompatActivity {

    private Child child;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_child_detail);

        Intent intent = getIntent();
        child = (Child) intent.getSerializableExtra(Utils.CHILD);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(child.getName() + "'s Profile");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(Utils.getImageResource(child.getProfilePicture()));

        //Having an issue with string resources here
        TextView choresCompletedTV = findViewById(R.id.chores_completed);
        choresCompletedTV.setText("Chores Completed: "  + child.getChoresCompleted());

        TextView currentBalanceTV = findViewById(R.id.current_balance);
        currentBalanceTV.setText(("Account Balance: " + Utils.centsToDollarString(child.getAccountBalanceCents())));

        TextView allowanceTV = findViewById(R.id.allowance);
        allowanceTV.setText("Allowance: " + Utils.centsToDollarString(child.getWeeklyAllowanceCents()) + " per week");

        TextView nameTV = findViewById(R.id.child_name);
        nameTV.setText(child.getName());

        setClickListeners(child);
    }

    private void setClickListeners(Child child) {
        Button modifyBalance = findViewById(R.id.modify_balance);
        modifyBalance.setOnClickListener(view -> {
            //TODO: Set values in bottom sheet appropriately
            ModifyAllowanceBottomSheetDialog bottomSheet = new ModifyAllowanceBottomSheetDialog();
            bottomSheet.show(getSupportFragmentManager(), ModifyAllowanceBottomSheetDialog.TAG);
        });

        Button manageChores = findViewById(R.id.manage);
        manageChores.setOnClickListener(view -> {
            Intent intent = new Intent(this, ParentChildChoresActivity.class);
            intent.putExtra(Utils.CHILD, child);
            startActivity(intent);
        });

        Button allowanceSummary = findViewById(R.id.allowance_summary);
        allowanceSummary.setOnClickListener(view -> {
            //TODO: Navigate to allowance summary page
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_edit_profile) {
            Intent myIntent = new Intent(this, AddChildActivity.class);
            myIntent.putExtra(Utils.CHILD, child);
            startActivity(myIntent);
            return true;
        } else if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}