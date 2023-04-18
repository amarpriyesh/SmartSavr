package com.example.smartsavr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildViewHolder> {
    private final List<Child> childList;
    private final Context context;

    public ChildAdapter(List<Child> childList, Context context) {
        this.childList = childList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(context).inflate(R.layout.child_card_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        holder.name.setText(childList.get(position).getName());
        holder.bindThisData(childList.get(position));
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }
}
