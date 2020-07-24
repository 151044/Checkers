package com.colin.games.checkers.common;

public class Piece {
    private Color color;
    private boolean isKing = false;
    public Piece(Color toSet){
        color = toSet;
    }
    public void setKing(boolean toSet){
        isKing = toSet;
    }
    public boolean isKing(){
        return isKing;
    }
    public Color getColor(){
        return color;
    }
    public enum Color{
        BROWN,WHITE
    }
}
