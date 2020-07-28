package com.colin.games.checkers.common;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private List<List<Tile>> tiles = new ArrayList<>();
    private int size = Environment.getConfig().asTypeFrom("boardSize", Integer::parseInt);
    private int rowPieces = Environment.getConfig().asTypeFrom("rowSize",Integer::parseInt);
    public Board() {
        for (int i = 0; i < size; i++) {
            List<Tile> toAdd = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                toAdd.add(new Tile(new Point(j,i)));
            }
            tiles.add(toAdd);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < rowPieces; j++) {
                if ((j % 2 == 0 && (i % 2 == 0)) || (j % 2 == 1 && i % 2 == 1)) {
                    tiles.get(j).get(i).putPiece(new Piece(Piece.Color.BROWN));
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = size - rowPieces; j < size; j++) {
                if ((j % 2 == 0 && (i % 2 == 0)) || (j % 2 == 1 && i % 2 == 1)) {
                    tiles.get(j).get(i).putPiece(new Piece(Piece.Color.WHITE));
                }
            }
        }
    }
    private Board(List<List<Tile>> internal){
        for (int i = 0; i < size; i++) {
            List<Tile> toAdd = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                toAdd.add(new Tile(internal.get(i).get(j).getSafely(),new Point(j,i)));
            }
            tiles.add(toAdd);
        }
    }
    public String gridDump(){
        StringBuilder build = new StringBuilder();
        for(int i = size - 1; i >= 0; i--){
            build.append(tiles.get(i).stream().map(t -> t.isEmpty() ? "N" : t.get().getColor().toString()).collect(Collectors.joining(","))).append("\n");
        }
        return build.toString();
    }
    public Tile getTileAt(Point p){
        return tiles.get(p.getY()).get(p.getX());
    }
    public Piece getPiece(Point p){
        return getTileAt(p).get();
    }
    public Optional<Piece> tryGet(Point p){
        return getTileAt(p).getSafely();
    }
    private List<Tile> moveSquares(Point p){
        return squaresAwayFrom(p,1);
    }
    private List<Tile> actualMoveSquares(Point p, Piece.Color c,boolean isKing){
        return moveSquares(p).stream().filter(t -> t.isEmpty() && (isKing || (c.equals(Piece.Color.BROWN) ? t.getLocation().getY() - p.getY() > 0 : t.getLocation().getY() - p.getY() < 0))).collect(Collectors.toList());
    }
    private List<Tile> squaresAwayFrom(Point p, int distance){
        if(distance > size){
            throw new IllegalArgumentException("Distance in squaresAwayFrom() > board size?");
        }
        List<Tile> ans = new ArrayList<>();
        if(!(p.getY() + distance >= size || p.getX() + distance >= size)){
            ans.add(getTileAt(p.move(distance,distance)));
        }
        if(!(p.getY() + distance >= size || p.getX() - distance < 0)){
            ans.add(getTileAt(p.move(-distance,distance)));
        }
        if(!(p.getY() - distance < 0 || p.getX() + distance >= size)){
            ans.add(getTileAt(p.move(distance,-distance)));
        }
        if(!(p.getY() - distance < 0 || p.getX() - distance < 0)){
            ans.add(getTileAt(p.move(-distance,-distance)));
        }
        return ans;
    }
    private List<Tile> jumpSquares(Point p){
        return squaresAwayFrom(p,2);
    }
    private List<Tile> actualJumpSquares(Point p, Piece.Color c,boolean isKing){
        List<Tile> ans = new ArrayList<>();
        for(Tile t : jumpSquares(p)){
            Tile mid = getTileAt(p.midPoint(t.getLocation()));
            if(!mid.isEmpty() && t.isEmpty() && !c.equals(mid.get().getColor())){
                if(c.equals(Piece.Color.BROWN)){
                    if(t.getLocation().getY() - p.getY() > 0 || isKing){
                        ans.add(t);
                    }
                }else{
                    if(t.getLocation().getY() - p.getY() < 0 || isKing){
                        ans.add(t);
                    }
                }
            }
        }
        return ans;
    }
    public List<Tile> immediateMoves(Point p,Piece.Color c,boolean isKing){
        List<Tile> ans = actualMoveSquares(p,c,isKing);
        ans.addAll((actualJumpSquares(p,c,isKing)));
        return ans;
    }
    public List<Tile> allMoves(Point p, Piece.Color c,boolean isKing){
        return recurse(p,c,true,isKing,p,copy());
    }
    public Tile bestMove(Point p, Piece.Color c, boolean isKing){
        Map<Tile,Double> map = recurse(p,c,true,isKing,p,copy(),0.0);
        return map.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).map(Map.Entry::getKey).orElse(getTileAt(p));
    }
    private List<Tile> recurse(Point p, Piece.Color c, boolean isFirst,boolean isKing,Point prev,Board copy){
        return new ArrayList<>(recurse(p, c, isFirst, isKing, prev, copy,0.0).keySet());
    }
    private Map<Tile,Double> recurse(Point p, Piece.Color c, boolean isFirst, boolean isKing, Point prev, Board copy, double curScore){
        if(!isFirst){
            copy.removePieceAt(p.midPoint(prev));
        }
        List<Tile> add = new ArrayList<>(copy.actualJumpSquares(p, c, isKing));
        if(add.isEmpty()){
            return Map.of(getTileAt(p),curScore);
        }else{
            Map<Tile,Double> map = add.stream().flatMap(t -> recurse(t.getLocation(),c,false,isKing,p,copy.copy(),curScore + 2.0).entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if(!isFirst) {
                map.put(copy.getTileAt(p),curScore);
            }else{
                copy.actualMoveSquares(p,c,isKing).forEach(tile -> map.put(tile,0.5));
            }
            return map;
        }
    }
    public Optional<Piece> removePieceAt(Point p){
        return getTileAt(p).removeSafely();
    }
    public Optional<Piece> setPieceAt(Point p, Piece toSet){
        Tile t = getTileAt(p);
        Optional<Piece> get = t.removeSafely();
        t.putPiece(toSet);
        return get;
    }
    public boolean canMove(Point src,Point dest,Piece.Color color,boolean isKing){
        if(!getTileAt(dest).isEmpty()){
            return false;
        }
        return immediateMoves(src,color,isKing).stream().anyMatch(tile -> tile.getLocation().equals(dest));
    }
    public Board copy(){
        return new Board(tiles);
    }
}
