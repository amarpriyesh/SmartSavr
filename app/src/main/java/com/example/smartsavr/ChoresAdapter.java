package com.example.smartsavr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChoresAdapter extends RecyclerView.Adapter<ChoresViewHolder> {

    Context context;

    List<Chore> chores;


    public ChoresAdapter(List<Chore> chores, Context context) {
        this.context = context;
        this.chores = chores;
    }

    @NonNull
    @Override
    public ChoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChoresViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ChoresViewHolder holder, int position) {
        holder.taskName.setText(chores.get(position).getTaskName());
        holder.imageCircle.setImageResource(R.drawable.circle);
        holder.done.setImageResource(R.drawable.tick);
        holder.rewardCents.setText(Integer.toString(chores.get(position).getRewardCents()));
        holder.deadline.setText(Long.toString(chores.get(position).getDeadline()));


    }

    @Override
    public int getItemCount() {
        return chores.size();
    }
}
