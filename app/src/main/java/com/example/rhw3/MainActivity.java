package com.example.rhw3;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    GridGameAdapter mObjGridGameAdapter;
    RecyclerView objRecyclerView;
    transient Board board = new Board();
    private boolean aiPlaying;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch (id) {
            case R.id.ai_playing:
            case R.id.human_playing:
                if (item.isChecked())

                    item.setChecked(false);
                else
                    item.setChecked(true);

                aiPlaying = id ==R.id.ai_playing;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("adapter",mObjGridGameAdapter);
        outState.putSerializable("board",board);//todo saved twice?
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        mObjGridGameAdapter = (GridGameAdapter) savedInstanceState.getSerializable("adapter");
        mObjGridGameAdapter.overWriteBoard(savedInstanceState);
        objRecyclerView.setAdapter(mObjGridGameAdapter);
        board = mObjGridGameAdapter.board;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    public void setUpBoard() {
        objRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        objRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager objLayoutManager = new GridLayoutManager(this, 8); // cols/rows
        mObjGridGameAdapter = new GridGameAdapter(board);

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

        if(board.CanPlacePiece(Integer.parseInt(view.getTag().toString())/8,Integer.parseInt(view.getTag().toString())%8,board.turn))
        {
            board.PlacePiece(Integer.parseInt(view.getTag().toString())/8,Integer.parseInt(view.getTag().toString())%8);

            if(aiPlaying)
            {
                Toast.makeText(getApplicationContext(),"aiIsPlaying", Toast.LENGTH_SHORT).show();
                OthelloAI ai = new OthelloAI(Piece.BLACK);
                ai.MakeMove(board.PossibleMoves());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),String.format("Not a valid move, fat fingers. You clicked %d, %d",Integer.parseInt(view.getTag().toString())/8,Integer.parseInt(view.getTag().toString())%8), Toast.LENGTH_SHORT).show();
        }
        mObjGridGameAdapter.notifyDataSetChanged();
    }


    public void newGame(MenuItem item) {
        board = new Board();
        mObjGridGameAdapter.setBoard(board);
        mObjGridGameAdapter.notifyDataSetChanged();
    }

    public void showAbout(MenuItem item) {

        showInfoDialog (this, "Othello", "Hello this is Bradley and Judah");
    }
    public static void showInfoDialog (Context context, String strTitle, String strMsg)
    {
        // create the listener for the dialog
        final DialogInterface.OnClickListener emptyOnClickListener = getNewEmptyOnClickListener ();

        // Create the AlertDialog.Builder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (context);

        // Use the AlertDialog's Builder Class methods to set the title, icon, message, et al.
        // These could all be chained as one long statement, if desired
        alertDialogBuilder.setTitle (strTitle);
        alertDialogBuilder.setIcon (android.R.drawable.ic_dialog_info);
        alertDialogBuilder.setMessage (strMsg);
        alertDialogBuilder.setCancelable (true);
        alertDialogBuilder.setNeutralButton (context.getString (android.R.string.ok),
                emptyOnClickListener);

        // Create and Show the Dialog
        alertDialogBuilder.show ();
    }
    @NonNull
    public static DialogInterface.OnClickListener getNewEmptyOnClickListener ()
    {
        return new DialogInterface.OnClickListener ()
        {
            @Override public void onClick (DialogInterface dialog, int which)
            {

            }
        };
    }

}
enum Piece {

    EMPTY,WHITE,BLACK
}
abstract class Player
{
    public abstract Point MakeMove(Iterable<Spot> possibleMoves);
}
class OthelloAI extends Player {
    private Piece color;

    public OthelloAI(Piece color) {
        this.color = color;
    }

    @Override
    public Point MakeMove(Iterable<Spot> possibleMoves) {

        int maxMoves = 0;
        Spot p = new Spot(-1, -1, null, null);
        for (Spot spot : possibleMoves) {
            if (spot.SpotsToTurn(color).size() > maxMoves) {
                maxMoves = spot.SpotsToTurn(color).size();
                p = spot;
            }
        }

        return p.point;
    }


}


class Human extends Player {
    public Point MakeMove(Iterable<Spot> possibleMoves) {
        return null;
    }
}

//class Program
//{
//    static void Main(String[] args)
//    {
//        Console.WriteLine("Enter 1 for user vs pc, 2 for user vs user, and 3 for pc vs pc");
//        int choice = Convert.ToInt32(Console.ReadLine());
//        Player p1 = new OthelloAI(Piece.BLACK);
//        Player p2 = new OthelloAI(Piece.WHITE);
//
//        switch (choice)
//        {
//            case 1:
//                p1 = new Human();
//                p2 = new OthelloAI(Piece.WHITE);
//                break;
//            case 2:
//                p1 = new Human();
//                p2 = new Human();
//                break;
//        }
//
//        Board board = new Board();
//
//        bool winner;
//        while (!board.GameOver(out winner))
//        {
//            bool color = board.Turn;
//            DisplayBoard(board);
//            Console.Write(color ? "White" : "Black");
//            Console.WriteLine("'s turn. Pick a spot(v,h)");
//            Player currentTurn = color ? p2 : p1;
//            Point p = currentTurn.MakeMove(board.PossibleMoves());
//            if (board.PossibleMoves().Select(s=>s.Point).Contains(p))
//            {
//                board.PlacePiece(p.X, p.Y, color);
//            }
//                else
//            Console.WriteLine("Not a valid move.");
//        }
//        DisplayBoard(board);
//        Console.Write(winner ? "White" : "Black");
//        Console.WriteLine(" is the winner! Enter to exit.");
//        Console.ReadLine();
//
//    }
//
//
//
//    private static void DisplayBoard(Board board)
//    {
//        bool?[,] nullableArray = board.BoardState();
//        Console.Write("| |");
//        for (int index = 0; index < 8; ++index)
//            Console.Write("|" + (object) index + "|");
//        Console.WriteLine();
//        for (int index1 = 0; index1 < nullableArray.GetLength(0); ++index1)
//        {
//            Console.Write("|" + (object) index1 + "|");
//            for (int index2 = 0; index2 < nullableArray.GetLength(1); ++index2)
//            {
//                Console.Write('|');
//                bool? nullable = nullableArray[index1, index2];
//                bool flag1 = true;
//                if (nullable.GetValueOrDefault() == flag1 && nullable.HasValue)
//                {
//                    Console.Write('X');
//                }
//                else
//                {
//                    nullable = nullableArray[index1, index2];
//                    bool flag2 = false;
//                    if (nullable.GetValueOrDefault() == flag2 && nullable.HasValue)
//                        Console.Write('O');
//                    else
//                        Console.Write(' ');
//                }
//
//                Console.Write('|');
//            }
//
//            Console.WriteLine();
//        }
//    }
//
//
//


//}

class Spot implements Serializable
{
    private Board board;//todo make transient?
    public Point point;
    public Piece piece; //null for empty spot


    public Spot(int x, int y, Piece p, Board board)
    {
        piece = p;
        point = new Point(x, y);
        this.board = board;
    }

    private boolean WithinRange(int x, int y)
    {
        return x <8 && x >= 0 && y < 8 && y >= 0;
    }

    private Set<Spot> CheckSpots(int i, int j, Piece color)
    {
        Set<Spot> spots = new HashSet<Spot>();
        Spot nextSpot;
        int x = i, y = j;
        do
        {
            if (!WithinRange(point.x + x, point.y + y))
            {
                spots.clear();
                return spots;
            }

            nextSpot = board.spots[point.x + x][ point.y + y];
            x += i;
            y += j;
            if (nextSpot.piece != color&&nextSpot.piece != Piece.EMPTY)
            {
                spots.add(nextSpot);
            }
        } while (nextSpot.piece != color&&nextSpot.piece != Piece.EMPTY);

        if (nextSpot.piece == Piece.EMPTY)
        {
            spots.clear();
        }

        return spots;


    }

    public Set<Spot> SpotsToTurn(Piece color)
    {
        Set<Spot> spots = new HashSet<Spot>();
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                if (!(i == 0 && j == 0))
                    spots.addAll(CheckSpots(i, j, color));
            }
        }

        return spots;
    }

    public int TurnPieces()
    {
        Set<Spot> spots = SpotsToTurn(piece);
        for (Spot spot : spots)
        {
            spot.piece = spot.piece==Piece.WHITE? Piece.BLACK: Piece.WHITE;
        }

        return spots.size();
    }
}

class Board implements Serializable
{
    public Spot[][] spots = new Spot[8][8];
    public Piece turn = Piece.BLACK;

    public Board()
    {
        for (int i = 0; i < spots.length; i++)
        {
            for (int j = 0; j < spots[i].length; j++)
            {
                spots[i][j] = new Spot(i, j, Piece.EMPTY, this);
            }
        }

        spots[3][3].piece = Piece.WHITE;
        spots[4][4].piece = Piece.WHITE;
        spots[3][4].piece = Piece.BLACK;
        spots[4][3].piece = Piece.BLACK;
    }

    public Set<Spot> PossibleMoves()
    {
        Set<Spot> pointSet = new HashSet<Spot>();

        for(int i = 0; i < 8 ; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(spots[i][j].piece == Piece.EMPTY && CanPlacePiece(spots[i][j].point.x, spots[i][j].point.y, turn)) {
                    pointSet.add(spots[i][j]);
                }

            }
        }

        return pointSet;
    }

//    public int Score()//TODO eh?
//    {
//        return Spots.Cast<Spot>().Count(s => s.Color == Piece.WHITE) -
//            Spots.Cast<Spot>().Count(s => s.Color == Piece.BLACK);
//    }


    public int GameOver( )
    {
        return -1;
//        if (Spots.Cast<Spot>().Count(s => s.Color == Piece.WHITE)==0)
//        {
//            winner = Piece.BLACK;
//            return true;
//        }
//        if (Spots.Cast<Spot>().Count(s => s.Color == Piece.BLACK) == 0)
//        {
//            winner = Piece.WHITE;
//            return true;
//        }
//
//        if (Spots.Cast<Spot>().Count(s => s.Color == null) == 0)
//        {
//            winner = Spots.Cast<Spot>().Count(s => s.Color == Piece.WHITE) >
//            Spots.Cast<Spot>().Count(s => s.Color == Piece.BLACK);
//            return true;
//        }
//
//        winner = false;
//        return false;
    }



    public Piece[][] BoardState() {
        Piece[][] _spots = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                _spots[i][j] = spots[i][j].piece;
        }

        return _spots;
    }

    public boolean CanPlacePiece(int x, int y, Piece color)
    {
        return spots[x][y].piece == Piece.EMPTY && spots[x][y].SpotsToTurn(color).size() != 0;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="x"></param>
    /// <param name="y"></param>
    /// <param name="color"></param>
    /// <returns>Number of pieces turned</returns>
    public int PlacePiece(int x, int y)
    {
        spots[x][ y].piece = turn;
        int ret = spots[x][y].TurnPieces();
        turn = turn == Piece.BLACK? Piece.WHITE: Piece.BLACK;
        if (PossibleMoves().size() == 0)
        {//Not having a move forfeits your turn
            turn = turn == Piece.BLACK? Piece.WHITE: Piece.BLACK;
        }
        return ret;
    }


}