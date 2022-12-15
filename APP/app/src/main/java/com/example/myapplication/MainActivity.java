package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import no.bakkenbaeck.chessboardeditor.view.board.ChessBoardView;


public class MainActivity extends AppCompatActivity {


    ChessBoardView chessBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recuperer une reference sur l'objet graphique du board creer a traver le XML
        chessBoardView = (ChessBoardView) findViewById((R.id.chess_board_editor_view));

        chessBoardView.setFen("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3");
        //testStockFish();

    }


}