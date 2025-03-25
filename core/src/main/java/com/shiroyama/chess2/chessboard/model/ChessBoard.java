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

public class ChessBoard {

    public PieceInfo[][] pieces;

    private int size;
    private int squareSize;

    private Texture boardTexture;

    private final HashMap<String, Texture> textures;

    private TargetPoint whiteKing;
    private TargetPoint blackKing;

    private ScoreBoardManager scoreBoardManager;

    private boolean isPromoting = false;

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

    public PieceInfo getPiece(TargetPoint location){
        if(!isInBounds(location)){
            return null;
        }
        return pieces[(int)location.getX()][(int)location.getY()];
    }

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

    public TargetPoint getPoint(int x, int y){
        return new TargetPoint( x / squareSize, 7 - y /squareSize);
    }

    public boolean isInBounds(TargetPoint location){
        return location.getX() < 8 && location.getX() >= 0 && location.getY() < 8 && location.getY() >= 0;
    }

    public IntRect getRectangle(TargetPoint point){
        return new IntRect((int)point.getX()*squareSize, (int)point.getY() * squareSize, squareSize, squareSize);
    }

    private AttackListener attackListener;

    public void setOnAttackListener(AttackListener listener){
        this.attackListener = listener;
    }

    private PromotionListener promotionListener;

    public void setPromotionListener(PromotionListener listener){
        this.promotionListener = listener;
    }

    public boolean isPromoting() {
        return isPromoting;
    }

    public void setPromoting(boolean promoting) {
        isPromoting = promoting;
    }
}
