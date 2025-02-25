package com.shiroyama.chess2.arena.model;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public class PieceMovementHandler {

    private static final int MAX_X = 30;
    private static final int MAX_Y = 30;

    public void moveUp(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() < MAX_Y){
            piece.getPosition().setY(piece.getPosition().getY() + 1);
        }
    }

    public void moveDown(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() > 0){
            piece.getPosition().setY(piece.getPosition().getY() - 1);
        }
    }

    public void moveLeft(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() > 0){
            piece.getPosition().setX(piece.getPosition().getX() - 1);
        }
    }

    public void moveRight(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() < MAX_X){
            piece.getPosition().setX(piece.getPosition().getX() + 1);
        }
    }
}
