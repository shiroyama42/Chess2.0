package com.shiroyama.chess2.utils;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Represents a utility class for loading textures of chess pieces.
 * This class provides a static method to load and store textures for all chess pieces,
 * categorized by team (black and white) and piece type (king, queen, bishop, knight, rook, pawn).
 */
public class TextureLoader {

    /**
     * Loads and stores textures for all chess pieces.
     * The textures are categorized by team (black and white) and piece type.
     *
     * @return a {@link HashMap} containing the loaded textures, where the keys are strings
     *          representing the piece names (e.g., "black-king", "white-pawn") and the values
     *          are the corresponding {@link Texture} objects
     */
    public static HashMap<String, Texture> loadPieceTextures() {
        HashMap<String, Texture> textures = new HashMap<>();

        // BLACK PIECES
        textures.put("black-king", new Texture("chess_pieces/black-king.png"));
        textures.put("black-queen", new Texture("chess_pieces/black-queen.png"));
        textures.put("black-bishop", new Texture("chess_pieces/black-bishop.png"));
        textures.put("black-knight", new Texture("chess_pieces/black-knight.png"));
        textures.put("black-rook", new Texture("chess_pieces/black-rook.png"));
        textures.put("black-pawn", new Texture("chess_pieces/black-pawn.png"));

        // WHITE PIECES
        textures.put("white-king", new Texture("chess_pieces/white-king.png"));
        textures.put("white-queen", new Texture("chess_pieces/white-queen.png"));
        textures.put("white-bishop", new Texture("chess_pieces/white-bishop.png"));
        textures.put("white-knight", new Texture("chess_pieces/white-knight.png"));
        textures.put("white-rook", new Texture("chess_pieces/white-rook.png"));
        textures.put("white-pawn", new Texture("chess_pieces/white-pawn.png"));

        return textures;
    }
}
