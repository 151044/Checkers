package com.colin.games.checkers.common;

public final class Point {
    private final int x;
    private final int y;
    public Point(){
        x = 0;
        y = 0;
    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Point move(int xChange, int yChange){
        return new Point(x + xChange,y + yChange);
    }
    public Point moveDiagonal(boolean xForward,boolean yForward){
        return new Point(xForward ? (x + 1) : (x - 1),yForward ? (y + 1) : (y - 1));
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Point copy(){
        return new Point(x,y);
    }
}
