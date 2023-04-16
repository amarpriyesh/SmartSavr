package com.example.smartsavr;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class ChoresAdapter extends RecyclerView.Adapter<ChoresViewHolder> {

    Context context;

    List<Chore> chores;

    String user;

    DBReference dbReference;




    public ChoresAdapter(List<Chore> chores, Context context, String user, DBReference dbReference) {
        this.context = context;
        this.chores = chores;
        this.user = user;
        this.dbReference = dbReference;
    }

    @NonNull
    @Override
    public ChoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChoresViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChoresViewHolder holder, int position) {
        Chore chore = chores.get(holder.getAdapterPosition());
        holder.taskName.setText(chore.getTaskName());
        holder.imageCircle.setImageResource(R.drawable.circle);
        holder.rewardCents.setText(Integer.toString(chore.getRewardCents()));


        if (user.equals("child")){

        if(!chore.isComplete()){

            holder.dateText.setText(String.format("Due in %s",getDateToStringProcessor(chore.getAssignedTimestamp())));
            holder.done.setImageResource(R.drawable.tick);
            holder.done.setVisibility(View.VISIBLE);
            holder.doneText.setVisibility(View.VISIBLE);
            holder.doneText.setText("Mark Done!");

        }

        else{
            if(!chore.isApproved() ) {
                holder.dateText.setText(String.format("Completed %s back", getDateToStringProcessor(chore.getCompletedTimestamp())));
                holder.done.setImageResource(R.drawable.greentick);
                holder.done.setVisibility(View.VISIBLE);
                holder.doneText.setVisibility(View.VISIBLE);
                holder.doneText.setText("Tap to Undo");
                    }
            else{
                holder.dateText.setText(String.format("Approved %s back", getDateToStringProcessor(chore.getApprovedTimestamp())));
                holder.done.setVisibility(View.INVISIBLE);
                holder.doneText.setVisibility(View.INVISIBLE);
                holder.taskCardview.setCardBackgroundColor(Color.LTGRAY);
            }
        }

    holder.done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(!chore.isComplete()) {

                chore.setComplete(true);
                chore.setCompletedTimestamp(System.currentTimeMillis());
                dbReference.getCollectionReference().document(chore.getId()).set(chore);
            }
            else{
                chore.setComplete(false);
                chore.setCompletedTimestamp(Integer.MAX_VALUE);
                dbReference.getCollectionReference().document(chore.getId()).set(chore);
            }
            }
    });

    holder.edit.setVisibility(View.GONE);
    holder.delete.setVisibility(View.GONE);
    holder.taskCompleted.setVisibility(View.GONE);
    holder.undo.setVisibility(View.GONE);

}
///Parent starts

        else{
            holder.done.setVisibility(View.INVISIBLE);
            holder.doneText.setVisibility(View.INVISIBLE);
            holder.delete.setImageResource(R.drawable.thrash);
            holder.edit.setImageResource(R.drawable.pencil);
            holder.undo.setImageResource(R.drawable.undo);
            holder.taskCompleted.setImageResource(R.drawable.check);
            if(!chore.isComplete()){
                holder.dateText.setText(String.format("Due in %s",getDateToStringProcessor(chore.getDeadline())));
                holder.delete.setVisibility(View.VISIBLE);
                holder.edit.setVisibility(View.VISIBLE);
                holder.taskCompleted.setVisibility(View.INVISIBLE);
                holder.undo.setVisibility(View.INVISIBLE);

                Log.d("VISIBILITY",Integer.toString(holder.delete.getVisibility()));


                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbReference.getCollectionReference().document(chore.getId()).delete();
                    }
                });

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO
                    }
                });

            }
            else{
                holder.taskCompleted.setVisibility(View.VISIBLE);
                if(!chore.isApproved()){
                    holder.dateText.setText(String.format("Completed %s back, please approve",getDateToStringProcessor(chore.getCompletedTimestamp())));
                    holder.delete.setVisibility(View.INVISIBLE);
                    holder.edit.setVisibility(View.INVISIBLE);
                    holder.taskCompleted.setVisibility((View.VISIBLE));
                    holder.undo.setVisibility(View.VISIBLE);
                    holder.taskCompleted.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chore.setApproved(true);
                            chore.setApprovedTimestamp(System.currentTimeMillis());
                            dbReference.getCollectionReference().document(chore.getId()).set(chore);
                        }
                    });

                    holder.undo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chore.setComplete(false);
                            chore.setCompletedTimestamp(Integer.MAX_VALUE);
                            dbReference.getCollectionReference().document(chore.getId()).set(chore);
                        }
                    });


                }
                else{
                    holder.dateText.setText(String.format("Approved %s back",getDateToStringProcessor(chore.getApprovedTimestamp())));
                    holder.delete.setVisibility(View.INVISIBLE);
                    holder.edit.setVisibility(View.INVISIBLE);
                    holder.taskCompleted.setVisibility(View.INVISIBLE);
                    holder.taskCardview.setCardBackgroundColor(Color.LTGRAY);
                    holder.undo.setVisibility(View.INVISIBLE);

                }
            }


        }



    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

    public String getDateToStringProcessor(long rawTime){
        String[] formatDate = new String[]{"Month", "Day", "Hour", "Minute","Second"};
        String[] formatDatePlural = new String[]{"Months", "Days", "Hours", "Minutes","Seconds"};
        SimpleDateFormat formatter = new SimpleDateFormat("MM:dd:HH:mm:ss", Locale.US);
        String[] dueDateFormat = formatter.format(new Date(rawTime)).split(":");
        String[] currentDateFormat = formatter.format(new Date(System.currentTimeMillis())).split(":");


        for (int i = 0; i < dueDateFormat.length; i++) {
            int time = Math.abs(Integer.parseInt(dueDateFormat[i]) - Integer.parseInt(currentDateFormat[i]));

            if (time == 1) {
                return (String.format("%s %s", time, formatDate[i]));

            } else if (time > 1) {

                return (String.format("%s %s", time, formatDatePlural[i]));

            }

        }
        return "few seconds";
    }

}
