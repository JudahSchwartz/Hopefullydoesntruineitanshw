package com.example.rhw3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;


public class GridGameAdapter extends RecyclerView.Adapter<GridGameAdapter.ViewHolder> {
    public static final int DEFAULT_ELEMENTS = 16;
    private int mElts;
    private int mWinner;
    private Random mGenerator;
    private boolean [] mSquares;
    public GridGameAdapter()
    {
        this(DEFAULT_ELEMENTS);

    }
    public GridGameAdapter(int elts)
    {
        mElts = elts;
        if(elts % Math.sqrt(elts)==0)
            mSquares = new boolean[elts];
        else
            throw new IllegalArgumentException("Number must be a square");
        mGenerator  = new Random();
    }

    private void startGame()
    {

        mWinner = mGenerator.nextInt(mElts);
        mSquares[mWinner] = true;
    }

    private void startNewGame() {
        mSquares[mWinner] = false;
        startGame();
    }

    public int getWinningNumber()
    {
        return mWinner;
    }
    public boolean isWinningNumber(int num)
    {
        return mSquares[num];
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new ViewHolder(ItemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mButton.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount() {
        return mSquares.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById (R.id.button);
        }
    }
}