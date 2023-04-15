package com.example.smartsavr;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChoresViewHolder extends RecyclerView.ViewHolder {

    public TextView taskName;
    public TextView deadline;
    public TextView rewardCents;

    public ImageView imageCircle;
    public ImageView done;

    public TextView doneText;

    public ImageView delete;
    public ImageView edit;
    public ImageView taskCompleted;

    public ChoresViewHolder(@NonNull View itemView) {
        super(itemView);
        this.taskName = itemView.findViewById(R.id.activityName);
        this.rewardCents = itemView.findViewById(R.id.reward);
        this.deadline = itemView.findViewById(R.id.dueDateText);
        this.imageCircle = itemView.findViewById(R.id.circle);
        this.done = itemView.findViewById(R.id.done);
        this.doneText = itemView.findViewById(R.id.textDone);
        this.delete = itemView.findViewById(R.id.delete);
        this.edit = itemView.findViewById(R.id.edit);
        this.taskCompleted = itemView.findViewById(R.id.taskDone);

    }
}
