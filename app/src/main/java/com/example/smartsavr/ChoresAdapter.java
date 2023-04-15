package com.example.smartsavr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChoresAdapter extends RecyclerView.Adapter<ChoresViewHolder> {

    Context context;

    List<Chore> chores;

    String activity;


    public ChoresAdapter(List<Chore> chores, Context context, String activity) {
        this.context = context;
        this.chores = chores;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ChoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChoresViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ChoresViewHolder holder, int position) {
        holder.taskName.setText(chores.get(holder.getAdapterPosition()).getTaskName());
        holder.imageCircle.setImageResource(R.drawable.circle);
        holder.rewardCents.setText(Integer.toString(chores.get(holder.getAdapterPosition()).getRewardCents()));
        holder.deadline.setText(Long.toString(chores.get(holder.getAdapterPosition()).getDeadline()));

        if (activity.equals("child"))

    {
if (chores.get(position).isComplete()) {

    chores.remove(holder.getAdapterPosition());
   /* holder.done.setVisibility(View.GONE);
    holder.doneText.setVisibility(View.GONE);
    */

}
else{
    holder.done.setImageResource(R.drawable.tick);
    holder.done.setVisibility(View.VISIBLE);
    holder.done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chores.get(holder.getAdapterPosition()).setComplete(true);
            ChoresAdapter.super.notifyDataSetChanged();
        }
    });
    holder.doneText.setVisibility(View.VISIBLE);
    holder.edit.setVisibility(View.GONE);
    holder.delete.setVisibility(View.GONE);
    holder.taskCompleted.setVisibility(View.GONE);

}

    }
        else{
            holder.taskCompleted.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

}
