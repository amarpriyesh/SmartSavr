package com.example.smartsavr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parent_HomeScreen extends AppCompatActivity {

    RecyclerView childrenRecyclerView;
    ChildAdapter childAdapter;
    List<Child> childList;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_home);
        }

        findViewById(R.id.add_a_child_text_view).setOnClickListener(view -> startActivity(new Intent(this, AddChildActivity.class)));

        childList = new ArrayList<>();
        childrenRecyclerView = findViewById(R.id.view_added_children);

        childrenRecyclerView.setHasFixedSize(true);
        childrenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childAdapter = new ChildAdapter(childList, this);
        childrenRecyclerView.setAdapter(childAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String parentID = user.getEmail();

        final Button allowanceSummaryButton = findViewById(R.id.allowance_summary);
        allowanceSummaryButton.setOnClickListener(v -> {
            //TODO: link to Lily's allowance summary page
        });

        firebaseFirestore.collection("children").whereEqualTo("parent_id", parentID).addSnapshotListener(this, (value, error) -> {
            if (error != null) {
                Log.w("TAG", "Listen failed.", error);
                return;
            }
            if (value != null) {
                for (DocumentChange dc : value.getDocumentChanges()) {
                    String username = dc.getDocument().get("username", String.class);
                    String name = dc.getDocument().get("name", String.class);
                    String password = dc.getDocument().get("password", String.class);

                    int weekly_allowance = 0;
                    if (dc.getDocument().getLong("weekly_allowance") != null) {
                        weekly_allowance = Objects.requireNonNull(dc.getDocument().getLong("weekly_allowance")).intValue();
                    }

                    int account_bal = 0;
                    if (dc.getDocument().getLong("account_balance") != null) {
                        account_bal = Objects.requireNonNull(dc.getDocument().getLong("account_balance")).intValue();
                    }

                    int profilePictureID = 2131165306;
                    if (dc.getDocument().getLong("profilePicture") != null) {
                        profilePictureID = Objects.requireNonNull(dc.getDocument().getLong("profilePicture")).intValue();
                    }

                    String parent_id = dc.getDocument().get("parent_id", String.class);

                    int choresCompleted = 0;
                    if (dc.getDocument().getLong("choresCompleted") != null) {
                        choresCompleted = Objects.requireNonNull(dc.getDocument().getLong("choresCompleted")).intValue();
                    }

                    Child child = new Child(name, parent_id, weekly_allowance, username, password, account_bal, profilePictureID, choresCompleted);
                    switch (dc.getType()) {
                        case ADDED:
                            childList.add(child);
                            //TODO: needs to update after adding a child
                            childAdapter.notifyItemChanged(childList.size() - 1);
                            break;
                        case MODIFIED:
                            //TODO: add logic for updating child info
                            break;
                        case REMOVED:
                            childList.remove(child);
                            childAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
            setVisibility();
        });
        setVisibility();
    }

    private void setVisibility() {
        if (childList.size() == 0) {
            findViewById(R.id.add_a_child_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.view_added_children).setVisibility(View.GONE);
        } else {
            findViewById(R.id.add_a_child_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.view_added_children).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
