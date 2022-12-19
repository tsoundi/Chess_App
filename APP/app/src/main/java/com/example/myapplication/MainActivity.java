package com.example.myapplication;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import no.bakkenbaeck.chessboardeditor.view.board.ChessBoardView;


public class MainActivity extends AppCompatActivity {


    ChessBoardView chessBoardView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recuperer une reference sur l'objet graphique du board creer a traver le XML
        chessBoardView = (ChessBoardView) findViewById((R.id.chess_board_editor_view));

        String fen="r1bqkbnr/ppp2ppp/2np4/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4";
        chessBoardView.setFen(fen);



        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                result.launch(i);
            }
        });



    }
    ActivityResultLauncher<Intent> result =
            registerForActivityResult(new
                            ActivityResultContracts.StartActivityForResult(),
                    (result) -> {
                        Intent intent = result.getData();
                        if (intent.hasExtra("FENCode")){
                            TextView text = findViewById(R.id.textView2);
                            text.setText(intent.getStringExtra("FENCode"));


                            chessBoardView.setFen(intent.getStringExtra("FENCode"));
                            return;
                        }
                    }
            );

    public void startBestMove(View view){
        Intent intent=new Intent(this,BestMoveActivity.class);
        String fen=chessBoardView.getFen();
        intent.putExtra("fen",fen);
        startActivity(intent);
    }
}