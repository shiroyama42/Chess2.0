package com.shiroyama.chess2.screens.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;

/**
 * Represents the promoting dialog menu of the chess game.
 * This dialog is displayed when a pawn reaches the opponent's end of the board and needs to be promoted
 * to a higher-ranking piece (e.g., queen, rook, knight, or bishop).
 */
public class PromotionDialog extends Dialog {

    /**
     * Constructor for the class.
     * Creates {@link ImageButton} for each higher-ranking piece (e.g., queen, rook, knight, or bishop).
     *
     * @param title the title of the dialog
     * @param skin the {@link Skin} used for styling the dialog
     * @param windowStyleName the name of the style to apply to the dialog menu
     * @param team the {@link Team} of the player whose pawn is being promoted
     */
    public PromotionDialog(String title, Skin skin, String windowStyleName, Team team){
        super(title, skin, windowStyleName);

        Texture texture = new Texture(Gdx.files.internal("chess_pieces/"
            + team.toString().toLowerCase() + "-queen.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        ImageButton queenButton = new ImageButton(createDrawable(team, PieceType.QUEEN));
        queenButton.setName("queen");
        ImageButton rookButton = new ImageButton(createDrawable(team, PieceType.ROOK));
        rookButton.setName("rook");
        ImageButton knightButton = new ImageButton(createDrawable(team, PieceType.KNIGHT));
        knightButton.setName("knight");
        ImageButton bishopButton = new ImageButton(createDrawable(team, PieceType.BISHOP));
        bishopButton.setName("bishop");

        button(queenButton);
        button(rookButton);
        button(knightButton);
        button(bishopButton);
    }

    /**
     * Called when a button in the dialog is clicked.
     *
     * @param object the result object associated with the clicked button
     */
    @Override
    protected void result(Object object) {
    }

    /**
     * Creates a {@link TextureRegionDrawable} for a specific team and piece type.
     *
     * @param team the team of the promoting piece
     * @param pieceType the {@link PieceType} of the promoting piece
     * @return a {@link TextureRegionDrawable} containing the texture for the specified team and piece type.
     */
    private TextureRegionDrawable createDrawable(Team team, PieceType pieceType){
        Texture texture = new Texture(Gdx.files.internal("chess_pieces/"
            + team.toString().toLowerCase() + "-" + pieceType.toString().toLowerCase() + ".png"));
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
