package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class FENResponse {
    @SerializedName("FENCode")
    private String fen;


    public String getFENCode() {
        return fen;
    }

    public FENResponse(String fen) {
        this.fen = fen;
    }
}
