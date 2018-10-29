package com.example.kazukihayashi.androidsync;

public class MovieModel {

    private int mLength;
    private String mMessage;
    private int mBgColor;

    public MovieModel(int length, String message, int color) {
        this.mLength = length;
        this.mMessage = message;
        this.mBgColor = color;
    }

    public int getLength() {
        return mLength;
    }

    public String getMessage() {
        return mMessage;
    }

    public int getBgColor() {
        return mBgColor;
    }
}
