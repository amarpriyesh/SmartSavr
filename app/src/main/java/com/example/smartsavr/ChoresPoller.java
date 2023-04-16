package com.example.smartsavr;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Handler;

public class ChoresPoller implements Runnable{
    RecyclerView.Adapter<ChoresViewHolder> adapter;
    boolean exit;
    Handler handler;

    public ChoresPoller(RecyclerView.Adapter<ChoresViewHolder> adapter, boolean exit, Handler handler) {
        this.exit = exit;
        this.adapter = adapter;
        this.handler = handler;
    }



    @Override
    public void run() {

        while(!exit) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            handler.post(new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();



                }
            });
        }


    }

    void setExit(){
        exit = true;
    }
}
