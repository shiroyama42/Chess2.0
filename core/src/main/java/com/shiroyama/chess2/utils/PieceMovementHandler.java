package com.shiroyama.chess2.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

/**
 * Represents the movement handler for chess pieces in the game.
 * This class manages the movement logic for chess pieces, ensuring that they stay within the bounds
 * of the screen or arena while moving up, down, left, or right.
 */
public class PieceMovementHandler {

    /**
     * The maximum x-coordinate a piece can move to, calculated based on the screen width.
     */
    private float maxX;

    /**
     * The maximum y-coordinate a piece can move to, calculated based on the screen width.
     */
    private float maxY;

    /**
     * The minimum x-coordinate a piece can move to, ensuring it does not go off the left edge.
     */
    private float minX = 0.3f;

    /**
     * The minimum y-coordinate a piece can move to, ensuring it does not go off the left edge.
     */
    private float minY = 0.2f;

    /**
     * Constructor for the class.
     *
     * @param screenWidth the width of the screen in pixels
     * @param screenHeight the height of the screen in pixels
     */
    public PieceMovementHandler(float screenWidth, float screenHeight) {
        this.maxX = (screenWidth / 50) - 1.15f;
        this.maxY = (screenHeight / 50) - 1.3f;
    }

    /**
     * Moves the specified piece upward if it is within the allowed vertical bounds.
     *
     * @param piece the {@link PieceInfo} representing the piece to be moved
     */
    public void moveUp(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() < maxY){
            piece.getPosition().setY(piece.getPosition().getY() + 0.07f);
        }
    }

    /**
     * Moves the specified piece downward if it is within the allowed vertical bounds.
     *
     * @param piece the {@link PieceInfo} representing the piece to be moved
     */
    public void moveDown(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getY() > minY){
            piece.getPosition().setY(piece.getPosition().getY() - 0.07f);
        }
    }

    /**
     * Moves the specified piece to the left if it is within the allowed horizontal bounds.
     *
     * @param piece the {@link PieceInfo} representing the piece to be moved
     */
    public void moveLeft(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() > minX){
            piece.getPosition().setX(piece.getPosition().getX() - 0.07f);
        }
    }

    /**
     * Moves the specified piece to the right if it is within the allowed horizontal bounds.
     *
     * @param piece the {@link PieceInfo} representing the piece to be moved
     */
    public void moveRight(PieceInfo piece){
        if (piece != null && piece.getPosition() != null && piece.getPosition().getX() < maxX){
            piece.getPosition().setX(piece.getPosition().getX() + 0.07f);
        }
    }
}
