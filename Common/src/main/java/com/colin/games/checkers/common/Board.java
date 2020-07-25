package com.colin.games.checkers.common;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }
}
