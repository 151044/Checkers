package com.colin.games.checkers.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {
    private List<List<Tile>> tiles = new ArrayList<>();
    private int size = Environment.getConfig().asTypeFrom("boardSize", Integer::parseInt);
    public Board() {
        for (int i = 0; i < size; i++) {
            List<Tile> toAdd = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                toAdd.add(new Tile(new Point(i, j)));
            }
            tiles.add(toAdd);
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 3; j++) {
                if ((j % 2 == 0 && (i % 2 == 0)) || (j % 2 == 1 && i % 2 == 1)) {
                    tiles.get(j).get(i).putPiece(new Piece(Piece.Color.BROWN));
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = size - 3; j < size; j++) {
                if ((j % 2 == 0 && (i % 2 == 0)) || (j % 2 == 1 && i % 2 == 1)) {
                    tiles.get(j).get(i).putPiece(new Piece(Piece.Color.WHITE));
                }
            }
        }
    }
    public String boardDump(){
        return tiles.toString();
    }
    public String gridDump(){
        StringBuilder build = new StringBuilder();
        for(int i = size - 1; i >= 0; i--){
            build.append(tiles.get(i).stream().map(t -> t.isEmpty() ? "N" : t.get().getColor().toString()).collect(Collectors.joining(","))).append("\n");
        }
        return build.toString();
    }
    public Piece getPieceAt(Point p){
        return tiles.get(p.getY()).get(p.getX()).get();
    }
    public Piece getPieceAt(int x,int y){
        return tiles.get(y).get(x).get();
    }
    public Optional<Piece> tryGetPieceAt(Point p){
        return tiles.get(p.getY()).get(p.getX()).getSafely();
    }
    public Optional<Piece> tryGetPieceAt(int x,int y){
        return tiles.get(y).get(x).getSafely();
    }
    public boolean hasPiece(Point p){
        return !tiles.get(p.getY()).get(p.getX()).isEmpty();
    }
    public boolean hasPiece(int x, int y){
        return !tiles.get(y).get(x).isEmpty();
    }
    public void movePieceTo(Point orig, Point toMove){

    }
    public void movePieceTo(int origX, int origY, int desX, int desY){

    }
    public List<Tile> allMoves(Tile toMove){
        return recurse(toMove);
    }
    public List<Tile> immediateMoves(Tile toMove){
        List<Tile> tiles = actualJumpSquares(toMove.getLocation());
        tiles.addAll(moveSquares(toMove.getLocation()));
        return tiles;
    }
    private List<Tile> recurse(Tile pos){
        List<Tile> possible = immediateMoves(pos);
        if(possible.isEmpty()){
            return List.of(pos);
        }else{
            List<Tile> next = possible.stream().flatMap(tile -> recurse(tile).stream()).collect(Collectors.toList());
            next.add(pos);
            return next;
        }
    }
    public boolean canMove(Tile toTest,Point moveTo){
        if(moveTo.getX() < 0 || moveTo.getY() < 0 || moveTo.getX() > size || moveTo.getY() > size){
            return false;
        }
        return canMove(toTest, moveTo.getX(), moveTo.getY());
    }
    public boolean canMove(Tile toTest,int xPos,int yPos){
        if(xPos < 0 || yPos < 0 || xPos > size || yPos > size){
            return false;
        }
        if(actualJumpSquares(new Point(xPos,yPos)).contains(toTest)){
            return true;
        }
        return Math.abs(toTest.getLocation().getX() - xPos) > 1 || Math.abs(toTest.getLocation().getY() - yPos) > 1;
    }
    private List<Tile> jumpSquares(Point pos){
        List<Tile> ans = new ArrayList<>();
        if(!(pos.getY() + 2 > size || pos.getX() + 2 > size)){
            ans.add(getTileAt(pos.getX() + 2, pos.getY() + 2));
        }
        if(!(pos.getY() + 2 > size || pos.getX() - 2 < 0)){
            ans.add(getTileAt(pos.getX() - 2, pos.getY() + 2));
        }
        if(!(pos.getY() - 2 < 0 || pos.getX() + 2 > size)){
            ans.add(getTileAt(pos.getX() + 2, pos.getY() - 2));
        }
        if(!(pos.getY() - 2 < 0 || pos.getX() - 2 < 0)){
            ans.add(getTileAt(pos.getX() - 2, pos.getY() - 2));
        }
        return ans;
    }
    private List<Tile> actualJumpSquares(Point pos){
        List<Tile> ans = new ArrayList<>();
        for(Tile t : jumpSquares(pos)){
            Point tPos = t.getLocation();
            Tile mid = getTileAt(pos.getX() - (tPos.getX() - pos.getX()),pos.getY() - (tPos.getY() - pos.getY()));
            if(mid.isEmpty()){
                continue;
            }
            if(mid.get().getColor().equals(getPieceAt(pos).getColor())){
                ans.add(t);
            }
        }
        return ans;
    }
    private List<Tile> moveSquares(Point pos){
        List<Tile> ans = new ArrayList<>();
        if(!(pos.getY() + 1 > size || pos.getX() + 1 > size)){
            ans.add(getTileAt(pos.getX() + 1, pos.getY() + 1));
        }
        if(!(pos.getY() + 1 > size || pos.getX() - 1 < 0)){
            ans.add(getTileAt(pos.getX() - 1, pos.getY() + 1));
        }
        if(!(pos.getY() - 1 < 0 || pos.getX() + 1 > size)){
            ans.add(getTileAt(pos.getX() + 1, pos.getY() - 1));
        }
        if(!(pos.getY() - 1 < 0 || pos.getX() - 1 < 0)){
            ans.add(getTileAt(pos.getX() - 1, pos.getY() - 1));
        }
        return ans;
    }
    public Tile getTileAt(Point pt){
        return tiles.get(pt.getY()).get(pt.getX());
    }
    public Tile getTileAt(int x, int y){
        return tiles.get(y).get(y);
    }
}
