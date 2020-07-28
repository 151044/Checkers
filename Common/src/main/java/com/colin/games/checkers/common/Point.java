package com.colin.games.checkers.common;

import java.util.Objects;

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

    public Point midPoint(Point other){
        return new Point(x + (int) Math.round((other.x - x) / 2.0), y + (int) Math.round((other.y - y) / 2.0));
    }
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
