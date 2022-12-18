package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

        //chess engine API
        ChessAPIAsyncTask chessAPIAsyncTask = new ChessAPIAsyncTask();
        chessAPIAsyncTask.execute("queryall","8/6pk/8/1R5p/3K3P/8/6r1/8 b - - 0 42");


    }
    class ChessAPIAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String action=strings[0];
            String FEN = strings[1];
            String urlstr="https://www.chessdb.cn/cdb.php?action="+action+"&board="+FEN;
            try {
                URL url = new URL(urlstr);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                InputStream inputStream=connection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String reponse=bufferedReader.readLine();
                Log.d("chess","reponse recue:"+reponse);
                return reponse;
            }catch (Exception e){
                Log.d("error","problème chez start engine: "+e);
            }
            return "";
        }
    }


}