package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.chessboard.controller.GameState;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.utils.ScoreBoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the scoreboard screen of the chess game.
 * This screen displays statistics about the current game session, including the total number of moves,
 * the count of captured pieces for each type and team, and a button to return to the main menu.
 */
public class ScoreBoardScreen implements Screen {

    /**
     * The main game instance that manages the screen transitions and game logic.
     */
    private ChessGame game;

    /**
     * The {@link Stage} used for managing UI elements like labels, tables, and buttons.
     */
    private Stage stage;

    /**
     * A {@link Skin} used for styling UI elements such as buttons, labels, and tables.
     */
    private Skin skin;

    /**
     * The {@link ScoreBoardManager} instance used to retrieve game statistics, such as move counts and captured pieces.
     */
    private ScoreBoardManager scoreBoardManager;

    /**
     * {@link Logger} for logging button clicked.
     */
    private static final Logger logger = LoggerFactory.getLogger(ScoreBoardScreen.class);

    /**
     * Constructor for the class.
     *
     * @param game the main game instance
     */
    public ScoreBoardScreen(ChessGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.scoreBoardManager = ScoreBoardManager.getInstance();

        createUI();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates the user interface for the scoreboard screen.
     * This includes a title, labels for move counts and captured pieces, and a "Back to Main Menu" button.
     */
    private void createUI(){
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label("Scoreboard", skin, "title");
        mainTable.add(titleLabel).colspan(3).pad(20);
        mainTable.row();

        mainTable.add(new Label("Overall Movement: ", skin)).pad(10);
        final Label moveField = new Label(
            String.valueOf(scoreBoardManager.getMoveCount()), skin);
        mainTable.add(moveField).pad(10);
        mainTable.row();

        mainTable.add(new Label("Piece Type", skin)).pad(10);
        mainTable.add(new Label("White Team", skin)).pad(10);
        mainTable.add(new Label("Black Team", skin)).pad(10);
        mainTable.row();

        for (final PieceType tpye : PieceType.values()){

            mainTable.add(new Label(tpye.toString(), skin));

            final Label countWhiteField = new Label(
                String.valueOf(scoreBoardManager.getCapturedPieces(Team.WHITE).get(tpye)), skin);
            mainTable.add(countWhiteField);

            final Label countBlackField = new Label(
                String.valueOf(scoreBoardManager.getCapturedPieces(Team.BLACK).get(tpye)), skin);
            mainTable.add(countBlackField);

            mainTable.row();
        }

        mainTable.row().pad(20);
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.setScreen(new MenuScreen(game));
                logger.info("Menu button clicked.");
                dispose();
                scoreBoardManager.reset();
            }
        });
        mainTable.add(backButton).colspan(4).width(200).height(50);
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets the input processor to the stage to handle user interactions.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the scoreboard screen, including clearing the screen and drawing the UI elements.
     *
     * @param delta the time between the last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
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
     * Releases all resources used by this screen, such as textures, fonts, and stages.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
