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

public class ParentHomeActivity extends AppCompatActivity {

    private static final String USERNAME = "username";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String WEEKLY_ALLOWANCE = "weeklyAllowance";
    private static final String ACCOUNT_BALANCE = "accountBalance";
    private static final String PROFILE_PICTURE = "profilePicture";
    private static final String PARENT_ID = "parentId";
    private static final String CHORES_COMPLETED = "choresCompleted";

    private static final String TAG = "ParentHomeActivity";

    RecyclerView childrenRecyclerView;
    ChildAdapter childAdapter;
    List<Child> childList;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

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

        Log.d(TAG, String.format("Adding Firebase snapshot listener to collection `children` where parent ID is equal to %s", parentID));

        firebaseFirestore.collection("children").whereEqualTo(PARENT_ID, parentID).addSnapshotListener(this, (value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            if (value != null) {
                Log.d(TAG, String.format("Received value: %s. There are %d document changes.", value, value.getDocumentChanges().size()));

                for (DocumentChange dc : value.getDocumentChanges()) {
                    String username = dc.getDocument().get(USERNAME, String.class);
                    String name = dc.getDocument().get(NAME, String.class);
                    String password = dc.getDocument().get(PASSWORD, String.class);

                    int weeklyAllowance = 0;
                    if (dc.getDocument().getLong(WEEKLY_ALLOWANCE) != null) {
                        weeklyAllowance = Objects.requireNonNull(dc.getDocument().getLong(WEEKLY_ALLOWANCE)).intValue();
                    }

                    int accountBalance = 0;
                    if (dc.getDocument().getLong(ACCOUNT_BALANCE) != null) {
                        accountBalance = Objects.requireNonNull(dc.getDocument().getLong(ACCOUNT_BALANCE)).intValue();
                    }

                    int profilePicture = 2131165306;
                    if (dc.getDocument().getLong(PROFILE_PICTURE) != null) {
                        profilePicture = Objects.requireNonNull(dc.getDocument().getLong(PROFILE_PICTURE)).intValue();
                    }

                    String parent_id = dc.getDocument().get(PARENT_ID, String.class);

                    int choresCompleted = 0;
                    if (dc.getDocument().getLong(CHORES_COMPLETED) != null) {
                        choresCompleted = Objects.requireNonNull(dc.getDocument().getLong(CHORES_COMPLETED)).intValue();
                    }

                    Child child = new Child(name, parent_id, weeklyAllowance, username, password, accountBalance, profilePicture, choresCompleted);

                    Log.d(TAG, String.format("Child data: %s", child));

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
        if (childList.isEmpty()) {
            findViewById(R.id.add_a_child_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.view_added_children).setVisibility(View.GONE);
        } else {
            findViewById(R.id.add_a_child_text_view).setVisibility(View.GONE);
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
        if (item.getItemId() == R.id.action_settings) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
