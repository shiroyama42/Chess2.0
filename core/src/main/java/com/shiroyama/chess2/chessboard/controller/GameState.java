package com.shiroyama.chess2.chessboard.controller;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Rules;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.util.ArrayList;

/**
 * Represents the state of the {@link ChessBoard} and manages user input.
 * This class manages current turn, game board, valid moves and selected pieces.
 */
public class GameState implements InputProcessor {

    /**
     * The main game instance that manages screen transitions and game logic.
     */
    private ChessBoard board;

    /**
     * The size of the game board.
     */
    private int size;

    /**
     * Team which can move in the round.
     */
    private Team currentTurn;

    /**
     * Represents an {@link ArrayList} for storing valid moves of specified piece.
     */
    private ArrayList<TargetPoint> validMoves;

    /**
     * Represents the currently selected piece's position on the chess board.
     */
    private TargetPoint selected;

    /**
     * A {@link Texture} used to visually highlight valid moves and the selected piece on the chessboard.
     */
    private Texture overlayBoxTexture;

    /**
     * A sprite derived from the {@link #overlayBoxTexture} that is used to render the visual overlay.
     */
    private Sprite overlayBoxSprite;

    /**
     * x- and y-coordinate of the board's center.
     */
    private float centerX, centerY;

    /**
     * Constructor for the class
     *
     * @param size the size of the game board
     * @param board the {@link ChessBoard} object representing the game board
     * @param centerX the x-coordinate of the board's center
     * @param centerY the y-coordinate of the board's center
     */
    public GameState(int size, ChessBoard board, float centerX, float centerY) {
        validMoves = new ArrayList<TargetPoint>();
        currentTurn = Team.WHITE;

        this.size = size;
        this.centerX = centerX;
        this.centerY = centerY;

        IntRect rect = board.getRectangle(new TargetPoint(0, 0));
        int nextPow2 = Integer.highestOneBit(rect.getHeight() - 1) << 1;
        Pixmap pixmap = new Pixmap(nextPow2, nextPow2, Pixmap.Format.RGBA8888);
        int borderWidth = rect.getWidth() / 10 + 1;
        pixmap.setColor(Color.WHITE);
        for (int i = 0; i < borderWidth; i++) {
            pixmap.drawRectangle(rect.getX() + i, rect.getY() + i,
                rect.getWidth() - 2 * i, rect.getHeight() - 2 * i);
        }
        overlayBoxTexture = new Texture(pixmap);
        pixmap.dispose();
        overlayBoxSprite = new Sprite(overlayBoxTexture, rect.getWidth(), rect.getHeight());

        this.board = board;
    }

    /**
     * Draws the game state, including selected pieces and valid moves.
     *
     * @param batch the {@link SpriteBatch} used for drawing
     */
    public void draw(SpriteBatch batch) {
        if (selected != null) {
            IntRect tile = board.getRectangle(selected);
            overlayBoxSprite.setPosition(tile.getX() + centerX, tile.getY() + centerY);
            overlayBoxSprite.setColor(Color.GREEN);
            overlayBoxSprite.draw(batch);
        }

        for (TargetPoint moveTile : validMoves) {
            IntRect tile = board.getRectangle(moveTile);
            overlayBoxSprite.setPosition(tile.getX() + centerX, tile.getY() + centerY);

            Color color = (board.getPiece(moveTile) == null) ? Color.YELLOW : Color.RED;
            overlayBoxSprite.setColor(color);
            overlayBoxSprite.draw(batch);
        }
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    /**
     * Handles mouse click input to the game board.
     *
     * @param x the x-coordinate of the touch
     * @param y the y-coordinate of the touch
     * @param pointer the pointer for the event
     * @param button the button that was pressed
     * @return true if the input was process, false otherwise
     */
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (button != 0 && board.isPromoting()) {
            return false;
        }

        int adjustedX = x - (int)centerX;
        int adjustedY = y - (int)centerY;

        TargetPoint tileIdx = board.getPoint(adjustedX, adjustedY);

        boolean moved = false;

        for (TargetPoint move : validMoves) {
            if (tileIdx.equals(move)) {
                board.movePiece(selected, tileIdx);
                currentTurn = (currentTurn == Team.WHITE) ? Team.BLACK : Team.WHITE;
                moved = true;
            }
        }

        validMoves.clear();
        selected = null;

        if (!moved) {
            PieceInfo piece = board.getPiece(tileIdx);
            if (piece != null && piece.getTeam() == currentTurn) {
                selected = tileIdx;
                Rules.GetValidMoves(validMoves, tileIdx, piece, board);
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
