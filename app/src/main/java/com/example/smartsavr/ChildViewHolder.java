package com.example.smartsavr;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public Child child;
    public TextView name;

    public ChildViewHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.childName);
        itemView.setOnClickListener(this);
    }

    public void bindThisData(Child child) {
        this.name.setText(child.getName());
    }

    @Override
    public void onClick(View view) {
        //TODO: Navigate to child's page
    }
}
