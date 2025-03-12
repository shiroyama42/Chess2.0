package com.shiroyama.chess2.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public class PieceMovementHandler {

    private float maxX;
    private float maxY;
    private float minX = 0.3f;
    private float minY = 0.2f;

    public PieceMovementHandler(float screenWidth, float screenHeight) {
        this.maxX = (screenWidth / 50) - 1.15f;
        this.maxY = (screenHeight / 50) - 1.3f;
    }

    public void moveUp(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() < maxY){
            piece.getPosition().setY(piece.getPosition().getY() + 0.07f);
        }
    }

    public void moveDown(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() > minY){
            piece.getPosition().setY(piece.getPosition().getY() - 0.07f);
        }
    }

    public void moveLeft(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() > minX){
            piece.getPosition().setX(piece.getPosition().getX() - 0.07f);
        }
    }

    public void moveRight(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() < maxX){
            piece.getPosition().setX(piece.getPosition().getX() + 0.07f);
        }
    }
}
