package com.github.byskyxie.eluyouni;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {



    static class ChatHolder extends RecyclerView.ViewHolder{
        public ChatHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
