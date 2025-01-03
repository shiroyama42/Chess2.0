package com.shiroyama.chess2.chessboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.util.HashMap;

public class ChessBoard {

    private PieceInfo[][] pieces;
    private int size;
    private int squareSize;

    private Texture boardTexture;

    private HashMap<String, Texture> textures;

    private TargetPoint whiteKing;
    private TargetPoint blackKing;

    public ChessBoard(int size, HashMap<String, Texture> textures) {
        this.size = size;
        squareSize = size / 8;
        this.textures = textures;

        pieces = new PieceInfo[8][8];
        initializePieces();

        whiteKing = new TargetPoint(3, 7);
        blackKing = new TargetPoint(3, 0);
    }

    private void initializePieces(){

        // Black pieces
        pieces[0][0] = new PieceInfo(Team.BLACK, PieceType.ROOK);
        pieces[1][0] = new PieceInfo(Team.BLACK, PieceType.KNIGHT);
        pieces[2][0] = new PieceInfo(Team.BLACK, PieceType.BISHOP);
        pieces[3][0] = new PieceInfo(Team.BLACK, PieceType.KING);
        pieces[4][0] = new PieceInfo(Team.BLACK, PieceType.QUEEN);
        pieces[5][0] = new PieceInfo(Team.BLACK, PieceType.BISHOP);
        pieces[6][0] = new PieceInfo(Team.BLACK, PieceType.KNIGHT);
        pieces[7][0] = new PieceInfo(Team.BLACK, PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            pieces[i][1] = new PieceInfo(Team.BLACK, PieceType.PAWN);
        }

        // White pieces
        pieces[0][7] = new PieceInfo(Team.WHITE, PieceType.ROOK);
        pieces[1][7] = new PieceInfo(Team.WHITE, PieceType.KNIGHT);
        pieces[2][7] = new PieceInfo(Team.WHITE, PieceType.BISHOP);
        pieces[3][7] = new PieceInfo(Team.WHITE, PieceType.KING);
        pieces[4][7] = new PieceInfo(Team.WHITE, PieceType.QUEEN);
        pieces[5][7] = new PieceInfo(Team.WHITE, PieceType.BISHOP);
        pieces[6][7] = new PieceInfo(Team.WHITE, PieceType.KNIGHT);
        pieces[7][7] = new PieceInfo(Team.WHITE, PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            pieces[i][6] = new PieceInfo(Team.WHITE, PieceType.PAWN);
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

    public void draw(SpriteBatch batch){
        if(boardTexture == null){
            generateTexture();
        }
        batch.draw(boardTexture, 0, 0);
        for (int col = 0; col < 8; col++){
            for (int row = 0; row < 8; row++){
                PieceInfo piece = pieces[col][row];
                if (piece != null){
                    String pieceName = piece.getName();
                    Texture texture = textures.get(pieceName);
                    batch.draw(texture, col * squareSize, row * squareSize, squareSize, squareSize);
                }
            }
        }
    }

    /*private void drawPiece(SpriteBatch batch, int col, int row){
        PieceInfo info = pieces[col][row];
        if (info == null){
            return;
        }

        String name = info.getName();
        Sprite sprite = sprites.get(spriteIndexMap.get(name));
        sprite.setX(col * squareSize + squareSize / 2 - sprite.getWidth() / 2);
        sprite.setY(row * squareSize + squareSize / 2 - sprite.getHeight() / 2);
        sprite.draw(batch);
    }*/
}
