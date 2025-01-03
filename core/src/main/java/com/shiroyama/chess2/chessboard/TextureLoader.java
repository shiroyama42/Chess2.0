package com.shiroyama.chess2.chessboard;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class TextureLoader {
    public static HashMap<String, Texture> loadPieceTextures() {
        HashMap<String, Texture> textures = new HashMap<>();

        // Load black pieces
        textures.put("black-king", new Texture("black-king.png"));
        textures.put("black-queen", new Texture("black-queen.png"));
        textures.put("black-bishop", new Texture("black-bishop.png"));
        textures.put("black-knight", new Texture("black-knight.png"));
        textures.put("black-rook", new Texture("black-rook.png"));
        textures.put("black-pawn", new Texture("black-pawn.png"));

        // Load white pieces
        textures.put("white-king", new Texture("white-king.png"));
        textures.put("white-queen", new Texture("white-queen.png"));
        textures.put("white-bishop", new Texture("white-bishop.png"));
        textures.put("white-knight", new Texture("white-knight.png"));
        textures.put("white-rook", new Texture("white-rook.png"));
        textures.put("white-pawn", new Texture("white-pawn.png"));

        return textures;
    }
}
