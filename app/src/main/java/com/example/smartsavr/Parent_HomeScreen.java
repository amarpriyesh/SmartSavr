package com.example.smartsavr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Parent_HomeScreen extends AppCompatActivity {

    RecyclerView childrenRecyclerView;
    ChildAdapter childAdapter;
    List<Child> childList;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home_screen);

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

        firebaseFirestore.collection("children").whereEqualTo("parent_id", parentID).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    String username = dc.getDocument().get("username", String.class);
                    String name = dc.getDocument().get("name", String.class);
                    String password = dc.getDocument().get("password", String.class);
                    int weekly_allowance = dc.getDocument().getLong("weekly_allowance").intValue();
                    int account_bal = dc.getDocument().getLong("account_balance").intValue();
                    //int profilePictureID = dc.getDocument().getLong("profilePictureID").intValue();
                    String parent_id = dc.getDocument().get("parent_id", String.class);
                    Child child = new Child(name, parent_id, weekly_allowance, username, password, account_bal, 1);
                    switch (dc.getType()) {
                        case ADDED:
                            childList.add(child);
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
                setVisibility();
            }
        });
        setVisibility();
    }

    private void setVisibility() {
        if (childList.size() == 0) {
            findViewById(R.id.no_children_message).setVisibility(View.VISIBLE);
            findViewById(R.id.add_a_child_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.view_added_children).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_children_message).setVisibility(View.GONE);
            findViewById(R.id.add_a_child_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.view_added_children).setVisibility(View.VISIBLE);
        }
    }
}