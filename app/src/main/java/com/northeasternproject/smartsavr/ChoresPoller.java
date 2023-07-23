package com.northeasternproject.smartsavr;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;

public class
ChoresPoller implements Runnable {
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
        while (!exit) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            handler.post(() -> adapter.notifyDataSetChanged());
        }
    }
}
