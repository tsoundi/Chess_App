package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;

import no.bakkenbaeck.chessboardeditor.view.board.ChessBoardView;

public class BestMoveActivity extends AppCompatActivity{
    ChessBoardView chessBoardView_old, chessBoardView_new;
    Board board_new;
    TextView bestMove_label;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_move);
        chessBoardView_old = (ChessBoardView) findViewById((R.id.chess_board_old));
        chessBoardView_new = (ChessBoardView) findViewById((R.id.chess_board_new));
        bestMove_label = (TextView)findViewById(R.id.bestMove_label);


        String fen=getIntent().getStringExtra("fen");
        chessBoardView_old.setFen(fen);
        chessBoardView_new.setFen(fen);
        board_new=new Board();
        board_new.loadFromFen(fen);

        ChessAPIAsyncTask chessAPIAsyncTask=new ChessAPIAsyncTask(this);
        chessAPIAsyncTask.execute("querybest",fen);

    }

    public void receiveResponce(String reponse){
        try {

            bestMove_label.post(new Runnable() {
                @Override
                public void run() {
                    bestMove_label.setText("The best move is : " + reponse);
                }
            });
            board_new.doMove(reponse);
            chessBoardView_new.setFen(board_new.getFen());
        }catch (Exception e){
            Log.d("error", "Incorrect move:<"+e+">");
        }
    }
}