package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;


public class PromotionDialog extends Dialog {

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

    @Override
    protected void result(Object object) {
    }

    private TextureRegionDrawable createDrawable(Team team, PieceType pieceType){
        Texture texture = new Texture(Gdx.files.internal("chess_pieces/"
            + team.toString().toLowerCase() + "-" + pieceType.toString().toLowerCase() + ".png"));
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
