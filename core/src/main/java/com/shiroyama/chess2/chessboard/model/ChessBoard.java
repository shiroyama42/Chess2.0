package com.shiroyama.chess2.chessboard.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shiroyama.chess2.chessboard.controller.IntRect;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.chessboard.utils.AttackListener;
import com.shiroyama.chess2.chessboard.utils.PromotionListener;
import com.shiroyama.chess2.utils.ScoreBoardManager;

import java.util.HashMap;

/**
 * Represents the chess board with pieces and game logic.
 * Manages board state, piece movement, attacks and promotions.
 */
public class ChessBoard {

    /**
     * 2D array representing the pieces on the board.
     */
    public PieceInfo[][] pieces;

    /**
     * The size of the board in pixels.
     */
    private int size;

    /**
     * The size of each square on the board in pixels.
     */
    private int squareSize;

    /**
     * The {@link Texture} representing the chessboard.
     */
    private Texture boardTexture;

    /**
     * {@link HashMap} of piece textures indexed by piece name.
     */
    private final HashMap<String, Texture> textures;

    /**
     * The position of white king on the board.
     */
    private TargetPoint whiteKing;

    /**
     * The position of black king on the board.
     */
    private TargetPoint blackKing;

    /**
     * {@link ScoreBoardManager} for tracking game scores.
     */
    private ScoreBoardManager scoreBoardManager;

    /**
     * Flag indicating if the pawn promotion is in progress.
     */
    private boolean isPromoting = false;

    /**
     * Constructor for the class.
     *
     * @param size the size of the board in pixels
     * @param textures {@link HashMap} containing {@link Texture} for each chess piece
     */
    public ChessBoard(int size, HashMap<String, Texture> textures) {
        this.size = size;
        squareSize = size / 8;
        this.textures = textures;

        pieces = new PieceInfo[8][8];
        initializePieces();

        whiteKing = new TargetPoint(3, 7);
        blackKing = new TargetPoint(3, 0);

        this.scoreBoardManager = ScoreBoardManager.getInstance();
    }

    /**
     * Initializes the chess pieces in their starting positions.
     */
    private void initializePieces(){

        // Black pieces
        pieces[0][0] = new PieceInfo(Team.BLACK, PieceType.ROOK, new TargetPoint(0, 0));
        pieces[1][0] = new PieceInfo(Team.BLACK, PieceType.KNIGHT, new TargetPoint(1, 0));
        pieces[2][0] = new PieceInfo(Team.BLACK, PieceType.BISHOP, new TargetPoint(2, 0));
        pieces[3][0] = new PieceInfo(Team.BLACK, PieceType.KING, new TargetPoint(3, 0));
        pieces[4][0] = new PieceInfo(Team.BLACK, PieceType.QUEEN, new TargetPoint(4, 0));
        pieces[5][0] = new PieceInfo(Team.BLACK, PieceType.BISHOP, new TargetPoint(5, 0));
        pieces[6][0] = new PieceInfo(Team.BLACK, PieceType.KNIGHT, new TargetPoint(6, 0));
        pieces[7][0] = new PieceInfo(Team.BLACK, PieceType.ROOK, new TargetPoint(7, 0));
        for (int i = 0; i < 8; i++) {
            pieces[i][1] = new PieceInfo(Team.BLACK, PieceType.PAWN, new TargetPoint(i, 1));
        }

        // White pieces
        pieces[0][7] = new PieceInfo(Team.WHITE, PieceType.ROOK, new TargetPoint(0, 7));
        pieces[1][7] = new PieceInfo(Team.WHITE, PieceType.KNIGHT, new TargetPoint(1, 7));
        pieces[2][7] = new PieceInfo(Team.WHITE, PieceType.BISHOP, new TargetPoint(2, 7));
        pieces[3][7] = new PieceInfo(Team.WHITE, PieceType.KING, new TargetPoint(3, 7));
        pieces[4][7] = new PieceInfo(Team.WHITE, PieceType.QUEEN, new TargetPoint(4, 7));
        pieces[5][7] = new PieceInfo(Team.WHITE, PieceType.BISHOP, new TargetPoint(5, 7));
        pieces[6][7] = new PieceInfo(Team.WHITE, PieceType.KNIGHT, new TargetPoint(6, 7));
        pieces[7][7] = new PieceInfo(Team.WHITE, PieceType.ROOK, new TargetPoint(7, 7));
        for (int i = 0; i < 8; i++) {
            pieces[i][6] = new PieceInfo(Team.WHITE, PieceType.PAWN, new TargetPoint(i, 6));
        }
    }

    /**
     * Generates the {@link Texture} for the chess board with alternating colors.
     */
    private void generateTexture(){
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    pixmap.setColor(Color.WHITE);
                } else {
                    pixmap.setColor(Color.DARK_GRAY);
                }
                pixmap.fillRectangle(x * squareSize, y * squareSize, squareSize, squareSize);
            }
        }

        boardTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Draws the chessboard and pieces.
     *
     * @param batch the {@link SpriteBatch} user for drawing
     * @param offsetX the x-coordinate offset for drawing
     * @param offsetY the y-coordinate offset for drawing
     */
    public void draw(SpriteBatch batch, float offsetX, float offsetY) {
        if (boardTexture == null) {
            generateTexture();
        }
        batch.draw(boardTexture, offsetX, offsetY);

        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                PieceInfo piece = pieces[col][row];
                if (piece != null) {
                    String pieceName = piece.getName();
                    Texture texture = textures.get(pieceName);
                    batch.draw(texture, offsetX + col * squareSize, offsetY + row * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    /**
     * Gets the piece in a specified location.
     *
     * @param location the target location on the board
     * @return the piece at the location, or null if no piece exists there
     */
    public PieceInfo getPiece(TargetPoint location){
        if(!isInBounds(location)){
            return null;
        }
        return pieces[(int)location.getX()][(int)location.getY()];
    }

    /**
     * Moves a piece from one location to the other.
     * Handles captures, promotions and updates the score.
     *
     * @param from the starting location of the piece
     * @param to the destination location for the piece
     */
    public void movePiece(TargetPoint from, TargetPoint to){

        PieceInfo piece = pieces[(int)from.getX()][(int)from.getY()];
        if (piece == null) {
            System.out.println("Error: No piece at " + from.getX() + ", " + from.getY());
            return;
        }
        PieceInfo target = pieces[(int)to.getX()][(int)to.getY()];

        scoreBoardManager.addMoveCount();

        if (target != null && attackListener != null){

            piece.setPosition(from);
            target.setPosition(to);

            System.out.println("moved to: " + to.getX() + "-" + to.getY());

            attackListener.onAttack(piece, target);

        }else{
            pieces[(int)to.getX()][(int)to.getY()] = piece;
            pieces[(int)to.getX()][(int)to.getY()].setPosition(to);
            pieces[(int)from.getX()][(int)from.getY()] = null;
            System.out.println("moved to: " + to.getX() + "-" + to.getY());

            if (piece.getPieceType() == PieceType.PAWN && piece.getTeam() == Team.WHITE && piece.getPosition().getY() == 0){
                promotionListener.onPromote(piece);
            }

            if (piece.getPieceType() == PieceType.PAWN && piece.getTeam() == Team.BLACK && piece.getPosition().getY() == 7){
                promotionListener.onPromote(piece);
            }
        }
    }

    /**
     * Converts screen coordinates for board coordinates.
     *
     * @param x the x-coordinate on the screen
     * @param y the y-coordinate on the screen
     * @return a {@link TargetPoint} representing the board coordinates
     */
    public TargetPoint getPoint(int x, int y){
        return new TargetPoint( x / squareSize, 7 - y /squareSize);
    }

    /**
     * Checks if the location is within the bounds of the chess board.
     *
     * @param location the location to check
     * @return true if the location is on the board, false otherwise
     */
    public boolean isInBounds(TargetPoint location){
        return location.getX() < 8 && location.getX() >= 0 && location.getY() < 8 && location.getY() >= 0;
    }

    /**
     * Gets a rectangle representing a square on the board.
     *
     * @param point the board coordinates
     * @return an {@link IntRect} representing the square
     */
    public IntRect getRectangle(TargetPoint point){
        return new IntRect((int)point.getX()*squareSize, (int)point.getY() * squareSize, squareSize, squareSize);
    }

    private AttackListener attackListener;

    /**
     * Sets a {@link AttackListener} for attack events.
     *
     * @param listener the attack listener to set
     */
    public void setOnAttackListener(AttackListener listener){
        this.attackListener = listener;
    }

    private PromotionListener promotionListener;

    /**
     * Sets a {@link PromotionListener} for promotion events.
     *
     * @param listener the promotion listener to set
     */
    public void setPromotionListener(PromotionListener listener){
        this.promotionListener = listener;
    }

    /**
     * Check if the pawn promotion is in progress.
     *
     * @return true if the promotion is in progress, false otherwise
     */
    public boolean isPromoting() {
        return isPromoting;
    }

    /**
     * Sets the promotion state.
     *
     * @param promoting true to indicate if the promotion is in progress, false otherwise
     */
    public void setPromoting(boolean promoting) {
        isPromoting = promoting;
    }
}
