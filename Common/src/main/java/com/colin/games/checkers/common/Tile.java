package com.colin.games.checkers.common;

import java.util.NoSuchElementException;
import java.util.Optional;

public class Tile {
    private Optional<Piece> piece;
    private final Point p;
    public Tile(Point p){
        piece = Optional.empty();
        this.p = p;
    }
    public Tile(Piece toSet,Point p){
        piece = Optional.of(toSet);
        this.p = p;
    }
    public Tile(Optional<Piece> toSet, Point p){
        this.p = p;
        piece = toSet;
    }
    public boolean isEmpty(){
        return piece.isEmpty();
    }
    public Piece get(){
        return piece.orElseThrow(() -> new NoSuchElementException("No piece exists in this tile " + p.getX() + "," + p.getY()));
    }
    public Optional<Piece> getSafely(){
        return piece;
    }
    public Piece getSafely(Piece backup){
        return piece.orElse(backup);
    }
    public void putPiece(Piece toPut){
        piece = Optional.of(toPut);
    }
    public Piece removePiece(){
        Piece ret = piece.orElseThrow(() -> new NoSuchElementException("No piece exists in this tile " + p.getX() + "," + p.getY()));
        piece = Optional.empty();
        return ret;
    }
    public Optional<Piece> removeSafely(){
        Optional<Piece> ret = piece.isEmpty() ? Optional.empty() : piece;
        piece = Optional.empty();
        return ret;
    }
    public Point getLocation(){
        return p;
    }

    @Override
    public String toString() {
        return "(" + p.getX() + "," + p.getY() + ")" + (piece.isEmpty() ? "N" : piece.get().toString());
    }
}
