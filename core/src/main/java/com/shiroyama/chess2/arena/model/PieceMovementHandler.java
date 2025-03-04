package com.shiroyama.chess2.arena.model;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public class PieceMovementHandler {

    private static final float MAX_X = 11.7f;
    private static final float MAX_Y = 8.5f;

    public void moveUp(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() < MAX_Y){
            piece.getPosition().setY(piece.getPosition().getY() + 0.07f);
        }
    }

    public void moveDown(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() > 0.2f){
            piece.getPosition().setY(piece.getPosition().getY() - 0.07f);
        }
    }

    public void moveLeft(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() > 0.2f){
            piece.getPosition().setX(piece.getPosition().getX() - 0.07f);
        }
    }

    public void moveRight(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() < MAX_X){
            piece.getPosition().setX(piece.getPosition().getX() + 0.07f);
        }
    }
}
