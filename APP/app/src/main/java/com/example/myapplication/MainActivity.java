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
import android.widget.Toast;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import no.bakkenbaeck.chessboardeditor.view.board.ChessBoardView;


public class MainActivity extends AppCompatActivity {


    ChessBoardView chessBoardView;
    TextView score_label;
    ChessAPIAsyncTask score;
    String fen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score_label = (TextView)findViewById(R.id.score_label);
        score = new ChessAPIAsyncTask(this);


        // recuperer une reference sur l'objet graphique du board creer a traver le XML
        chessBoardView = (ChessBoardView) findViewById((R.id.chess_board_editor_view));

        fen="r1bqkbnr/ppp2ppp/2n5/3pp3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq d6 0 4";



        chessBoardView.setFen(fen);
        Button resetBtn = (Button)findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chessBoardView.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                try{
                    score.execute("queryscore", "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Already reset", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button scoreBtn = (Button)findViewById(R.id.score);
        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    score.execute("queryscore", fen);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Score already asked", Toast.LENGTH_SHORT).show();
                }

            }
        });

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


    public void receivescore(String reponse){
        try {
            score_label.post(new Runnable() {
                @Override
                public void run() {
                    score_label.setText("The score is : " + reponse);
               }
          });
        }catch (Exception e){
            Log.d("error", "Incorrect move:<"+e+">");
        }
    }
}