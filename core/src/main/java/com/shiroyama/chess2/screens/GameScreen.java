package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.chessboard.controller.GameState;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.utils.TextureLoader;

import java.util.HashMap;

public class GameScreen implements Screen {

    private ChessGame game;
    private SpriteBatch batch;
    private ChessBoard board;
    private GameState gameState;

    private float centerX, centerY;

    private boolean isInArena = false;
    private ArenaScreen arenaScreen;

    private TargetPoint originalAttackerPosition, originalDefenderPosition;

    private Stage stage;
    private boolean showingDialog = false;
    private PieceInfo promotingPiece;

    private PieceInfo attackerPiece;

    public GameScreen(){
        this.game = (ChessGame) Gdx.app.getApplicationListener();
    }

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        int size = (int) Math.min(h, w);
        centerX = (w - size) / 2;
        centerY = (h - size) / 2;

        HashMap<String, Texture> textures = TextureLoader.loadPieceTextures();

        board = new ChessBoard(size, textures);
        batch = new SpriteBatch();

        gameState = new GameState(size, board, centerX, centerY);

        stage = new Stage(new ScreenViewport());

        board.setPromotionListener((piece) -> {

            showingDialog = true;
            promotingPiece = piece;
            Gdx.input.setInputProcessor(stage);

            Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
            PromotionDialog promotionDialog = new PromotionDialog("", skin, "dialog", promotingPiece.getTeam());

            promotionDialog.show(stage);
            board.setPromoting(true);

            stage.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (actor instanceof ImageButton && showingDialog){
                        PieceType newType = determinePieceTypeFromButton(actor);
                        if (newType != null){
                            promotingPiece.setPieceType(newType);
                            showingDialog = false;
                            board.setPromoting(false);

                            Gdx.input.setInputProcessor(gameState);
                        }
                    }
                }
            });


        });

        board.setOnAttackListener((attacker, defender) -> {
            originalAttackerPosition = new TargetPoint(attacker.getPosition().getX(), attacker.getPosition().getY());
            originalDefenderPosition = new TargetPoint(defender.getPosition().getX(), defender.getPosition().getY());

            attackerPiece = attacker;

            isInArena = true;
            arenaScreen = new ArenaScreen(attacker, defender, game);
        });



        Gdx.input.setInputProcessor(gameState);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (isInArena){
            arenaScreen.render(delta);
        }else{
            board.draw(batch, centerX, centerY);
            gameState.draw(batch);

            if (showingDialog && stage != null){
                stage.act(delta);
                stage.draw();
            }
        }
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void exitArena(PieceInfo winner, PieceInfo loser){
        isInArena = false;
        if (winner == null){
            board.pieces[(int)originalAttackerPosition.getX()][(int)originalAttackerPosition.getY()] = null;

        }else{
            board.pieces[(int)originalAttackerPosition.getX()][(int)originalAttackerPosition.getY()] = null;
            board.pieces[(int)originalDefenderPosition.getX()][(int)originalDefenderPosition.getY()] = winner;
            winner.setPosition(originalDefenderPosition);

            if (attackerPiece == winner &&
                ((winner.getTeam() == Team.WHITE && winner.getPosition().getY() == 0 && winner.getPieceType() == PieceType.PAWN)
                || (winner.getTeam() == Team.BLACK && winner.getPosition().getY() == 7 && winner.getPieceType() == PieceType.PAWN))){

                showingDialog = true;
                promotingPiece = winner;

                Gdx.input.setInputProcessor(stage);

                Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
                PromotionDialog promotionDialog = new PromotionDialog("", skin, "dialog", promotingPiece.getTeam());

                promotionDialog.show(stage);

                board.setPromoting(true);

                stage.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        if (actor instanceof ImageButton && showingDialog){
                            PieceType newType = determinePieceTypeFromButton(actor);
                            if (newType != null){
                                promotingPiece.setPieceType(newType);
                                showingDialog = false;
                                board.setPromoting(false);

                                Gdx.input.setInputProcessor(gameState);
                            }
                        }
                    }
                });

            }
        }

        if (!showingDialog){
            Gdx.input.setInputProcessor(gameState);
        }
    }

    private PieceType determinePieceTypeFromButton(Actor actor) {
        if (actor instanceof ImageButton) {
            String name = ((ImageButton) actor).getName();
            switch (name) {
                case "queen":
                    return PieceType.QUEEN;
                case "rook":
                    return PieceType.ROOK;
                case "knight":
                    return PieceType.KNIGHT;
                case "bishop":
                    return PieceType.BISHOP;
            }
        }
        return null;
    }
}
