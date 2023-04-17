package com.example.smartsavr;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name;
    public ImageView image;
    public Child child;

    public ChildViewHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.childName);
        this.image = itemView.findViewById(R.id.childImage);
        itemView.setOnClickListener(this);
    }

    public void bindThisData(Child child) {
        this.child = child;
        this.name.setText(child.getName());
        this.image.setImageResource(child.getProfilePicture());
    }

    @Override
    public void onClick(View view) {
        //TODO: Navigate to child's page
        Intent intent = new Intent(view.getContext(), ChildProfile_parentlogin.class);

        intent.putExtra("child", child);

        startActivity(view.getContext(), intent, null);

        Log.d("TAG", "onClick: " + child.getParentId() + " " + child.getUsername());
    }
}
