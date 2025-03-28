package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.shiroyama.chess2.screens.dialog.PromotionDialog;
import com.shiroyama.chess2.utils.TextureLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Represents the {@link Screen} where the normal chess game takes place.
 * Handles {@link com.shiroyama.chess2.chessboard.utils.AttackListener}, {@link com.shiroyama.chess2.chessboard.utils.PromotionListener},
 * rendering, updating the game state.
 * Manages UI elements, such as promotion button, transition to arena screen when an attack occurs.
 */
public class GameScreen implements Screen {

    /**
     * The main game instance that manages screen transitions and game logic.
     */
    private ChessGame game;

    /**
     * A {@link SpriteBatch} used for rendering textures and sprites.
     */
    private SpriteBatch batch;

    /**
     * The {@link ChessBoard} instance representing the game board.
     */
    private ChessBoard board;

    /**
     * The {@link GameState} instance that manages the current state of the game, including turn management,
     * valid moves, and selected pieces.
     */
    private GameState gameState;

    /**
     * x- and y-coordinate of the center of the board.
     */
    private float centerX, centerY;

    /**
     * Indicates whether the game is currently in the arena screen for combat between two pieces.
     */
    private boolean isInArena = false;

    /**
     * The {@link ArenaScreen} instance representing the combat arena screen.
     */
    private ArenaScreen arenaScreen;

    /**
     * The original positions of the attacking and the defending pieces.
     */
    private TargetPoint originalAttackerPosition, originalDefenderPosition;

    /**
     * The {@link Stage} used for managing UI elements, such as promotion dialogs.
     */
    private Stage stage;

    /**
     * Indicates whether the promoting dialog is currently being displayed.
     */
    private boolean showingDialog = false;

    /**
     * The {@link PieceInfo} instance representing the piece that is being promoted.
     */
    private PieceInfo promotingPiece;

    /**
     * Represents the attacking piece.
     */
    private PieceInfo attackerPiece;

    /**
     * {@link Logger} for logging screen switch.
     */
    private static final Logger logger = LoggerFactory.getLogger(GameScreen.class);

    /**
     * Constructor for the class.
     */
    public GameScreen(){
        this.game = (ChessGame) Gdx.app.getApplicationListener();
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * This method is part of the {@link Screen} interface and is invoked by the LibGDX framework
     * when the screen is displayed.
     */
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

            logger.info("Arena combat starting.");
        });



        Gdx.input.setInputProcessor(gameState);
    }

    /**
     * Renders the chess board, game state and pieces.
     *
     * @param delta the time in seconds since the last frame
     */
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

    /**
     * Releases all resources used by this screen, such as batch.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

    /**
     * Handles the transition back from the {@link ArenaScreen}.
     * This method is called when the combat between the two pieces end.
     *
     * @param winner the piece that won the combat
     * @param loser the piece that lost the combat
     */
    public void exitArena(PieceInfo winner, PieceInfo loser){
        isInArena = false;

        logger.info("Arena combat ended.");
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
                logger.info("Promoting {}.", winner.getName());

                stage.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        if (actor instanceof ImageButton && showingDialog){
                            logger.info("Showing promotion dialog menu.");
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

    /**
     * Determines the type of piece to promote a pawn to based on a clicked button in the promotion dialog.
     *
     * @param actor the UI actor ({@link ImageButton} that was clicked
     * @return the {@link PieceType} corresponding to the clicked button, or null if the button is invalid
     */
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
