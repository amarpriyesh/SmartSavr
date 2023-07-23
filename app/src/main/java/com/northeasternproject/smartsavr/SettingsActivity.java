package com.northeasternproject.smartsavr;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.northeasternproject.smartsavr.R;
import com.northeasternproject.smartsavr.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    public static final String IS_CHILD_USER = "isChildUser";

    private ActivitySettingsBinding binding;
    private boolean isChildUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        isChildUser = intent.getBooleanExtra(IS_CHILD_USER, false);
        binding.addAChildButton.setVisibility(isChildUser ? View.GONE : View.VISIBLE);

        // up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_settings);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setClickListeners();
    }

    private void setClickListeners() {
        binding.addAChildButton.setOnClickListener(view -> startActivity(new Intent(this, AddChildActivity.class)));
        binding.logOutButton.setOnClickListener(view -> startActivity(new Intent(this, isChildUser ? ChildLoginActivity.class : ParentLoginActivity.class)));
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
