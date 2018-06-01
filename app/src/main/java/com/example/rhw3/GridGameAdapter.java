package com.example.rhw3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;
import java.util.Random;


public class GridGameAdapter extends RecyclerView.Adapter<GridGameAdapter.ViewHolder> implements Serializable{
    public static final int SQUARES = 64;

    public Board board;


    public void overWriteBoard(Bundle savedInstanceState)
    {
       board = (Board) savedInstanceState.getSerializable("board");
       notifyDataSetChanged();
    }
    public GridGameAdapter(Board board)
    {

       this.board = board;

    }

    public void setBoard(Board board)
    {
        this.board = board;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new ViewHolder(ItemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //throw new RuntimeException("Howdy");
         holder.mButton.setTag(Integer.toString(position));
         switch (board.BoardState()[position/8][position%8])
        {
            case BLACK:
                holder.mButton.setText("\u2022");
                break;
            case WHITE:
                holder.mButton.setText("\u25E6");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 64;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById (R.id.button);
        }
    }
}