package com.example.rhw3;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int mTurnsTaken;
    GridGameAdapter mObjGridGameAdapter;

    public void setUpBoard() {
        RecyclerView objRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        objRecyclerView.setHasFixedSize(true);
        int squares = 16;
        int rows = (int) Math.sqrt(squares);
        RecyclerView.LayoutManager objLayoutManager = new GridLayoutManager(this, rows); // cols/rows
        mObjGridGameAdapter = new GridGameAdapter();

        objRecyclerView.setLayoutManager(objLayoutManager);
        objRecyclerView.setAdapter(mObjGridGameAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBoard();
    }

    public void buttonHandler(View view) {
        inrementGuesses();
        showGuessResults((Button) view);
    }

    private void inrementGuesses() {

        TextView tvStatus = (TextView) findViewById(R.id.status_bar);
        tvStatus.setText("Guesses Taken: " + ++mTurnsTaken);
    }

    private void showGuessResults(Button view) {
        View sbContainer = findViewById(R.id.activity_main);
        Button currentButton = view;
        int numGuessed = Integer.parseInt(currentButton.getText().toString());
        String msg = "You clicked on " + currentButton.getText().toString();
        msg += mObjGridGameAdapter.isWinningNumber(numGuessed) ? "Winner!" : "Loser!";
        Snackbar.make(sbContainer, msg, Snackbar.LENGTH_SHORT).show();
    }
}
