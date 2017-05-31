package com.aaron.rvitemdecorationdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/5/26.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.TextVH> {

    @Override
    public TextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TextVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class TextVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Adapter adapter;

        public TextVH(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
