package com.colin.games.checkers.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return moveSquares(p).stream().filter(t -> t.isEmpty() && (isKing || (c.equals(Piece.Color.BROWN) ? t.getLocation().getY() - p.getY() > 0 : t.getLocation().getY() - t.getLocation().getY() < 0))).collect(Collectors.toList());
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
            Tile mid = getTileAt(p.move((int) Math.round((t.getLocation().getX() - p.getX()) / 2.0),(int) Math.round((t.getLocation().getY() - p.getY()) / 2.0)));
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
    private List<Tile> recurse(Point p, Piece.Color c, boolean isFirst,boolean isKing,Point prev,Board copy){
        List<Tile> add = new ArrayList<>();
        if(!isFirst){
            copy.removePieceAt(p.midPoint(prev));
        }
        copy.gridDump();
        add.addAll(copy.actualJumpSquares(p,c,isKing));
        if(add.isEmpty()){
            return List.of(copy.getTileAt(p));
        }else{
            List<Tile> res = add.stream().flatMap(t -> recurse(t.getLocation(),c,false,isKing,p,copy.copy()).stream()).collect(Collectors.toList());
            if(!isFirst) {
                res.add(copy.getTileAt(p));
            }else{
                res.addAll(copy.actualMoveSquares(p,c,isKing));
            }
            return res;
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
