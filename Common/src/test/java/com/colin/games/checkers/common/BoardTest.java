package com.colin.games.checkers.common;

import java.io.IOException;
import java.nio.file.Path;

public class BoardTest {
    public static void main(String[] args) throws IOException {
        Environment.setConfig(Config.read(Path.of("config/Checkers.cfg")));
        Board board = new Board();
        System.out.println(board.gridDump());
        System.out.println(board.copy().gridDump());
        System.out.println(board.copy().gridDump().equals(board.gridDump()));
        System.out.println(board.canMove(new Point(0,0),new Point(1,1), Piece.Color.BROWN,false));
        System.out.println(board.canMove(new Point(0,2),new Point(1,3),Piece.Color.BROWN,false));
        System.out.println(board.canMove(new Point(7,5),new Point(6,4),Piece.Color.WHITE,false));
        board.setPieceAt(new Point(1,3),new Piece(Piece.Color.WHITE));
        board.setPieceAt(new Point(3,5),new Piece(Piece.Color.WHITE));
        board.removePieceAt(new Point(4,6));
        System.out.println(board.gridDump());
        System.out.println(board.allMoves(new Point(0,2), Piece.Color.BROWN,false));
        System.out.println(board.allMoves(new Point(0,2), Piece.Color.BROWN,true));
        System.out.println(board.gridDump());
        System.out.println(board.bestMove(new Point(0,2),Piece.Color.BROWN,true));
        System.out.println(board.bestMove(new Point(0,2),Piece.Color.BROWN,false));
        board.removePieceAt(new Point(2,6));
        System.out.println(board.gridDump());
        System.out.println(board.allMoves(new Point(2,2),Piece.Color.BROWN,false));
        System.out.println(board.bestMove(new Point(2,2), Piece.Color.BROWN,false));
        System.out.println(board.allMoves(new Point(2,2),Piece.Color.BROWN,true));
        System.out.println(board.bestMove(new Point(2,2), Piece.Color.BROWN,true));
    }
}
