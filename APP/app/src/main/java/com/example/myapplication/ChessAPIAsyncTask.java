package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

class ChessAPIAsyncTask extends AsyncTask<String,Void,String> {
    BestMoveActivity bestMoveActivity;
    MainActivity mainActicity;
    public ChessAPIAsyncTask(BestMoveActivity bestMoveActivity){
        this.bestMoveActivity=bestMoveActivity;
    }
    public ChessAPIAsyncTask(MainActivity mainActicity){
        this.mainActicity=mainActicity;
    }
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
            reponse=reponse.split(":")[1];
            reponse=reponse.substring(0,reponse.length()-1);

            Log.d("chess","reponse recue:"+reponse);
            try{
                bestMoveActivity.receiveResponce(reponse);
            }catch(Exception e){
                Log.d("error","n'arrive pas à recevoir le best move: "+e);
            }
            try{
                mainActicity.receivescore(reponse);
            }catch (Exception e){
                Log.d("error","n'arrive pas à recevoir le score: "+e);
            }

            return reponse;
        }catch (Exception e){
            Log.d("error","problème chez start engine: "+e);
        }
        return "";
    }
}