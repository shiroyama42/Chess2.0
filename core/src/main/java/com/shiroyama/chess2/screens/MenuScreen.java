package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.chessboard.model.ChessBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the main menu screen of the chess game.
 * This screen provides options for starting a local game, accessing settings,
 * or exiting the application.
 */
public class MenuScreen implements Screen {

    /**
     * The main game instance that manages screen transitions and game logic.
     */
    private ChessGame chessGame;

    /**
     * A {@link SpriteBatch} used for rendering textures and sprites.
     */
    private SpriteBatch batch;

    /**
     * The {@link Stage} used for managing UI elements, such as promotion dialogs.
     */
    private Stage stage;

    /**
     *  A {@link Skin} used for styling UI elements such as buttons and tables.
     */
    private Skin skin;

    /**
     * {@link Logger} for logging button clicks.
     */
    private static final Logger logger = LoggerFactory.getLogger(MenuScreen.class);

    /**
     * Constructor for the class.
     *
     * @param chessGame the main game instance
     */
    public MenuScreen(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Initializes the UI components, including buttons and their listeners,
     * and sets up the layout using a {@link Table}.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.getLabel().setFontScale(2);
        settingsButton.getLabel().setFontScale(2);
        exitButton.getLabel().setFontScale(2);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Play button clicked.");
                chessGame.setScreen(new GameScreen());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Exit button clicked.");
                Gdx.app.exit();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Settings button clicked.");
                chessGame.setScreen(new SettingsScreen(chessGame));
                dispose();
            }
        });

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(playButton).fillX().uniformX().pad(10);
        table.row().pad(10);

        table.add(settingsButton).fillX().uniformX().pad(10);
        table.row().pad(10);

        table.add(exitButton).fillX().uniformX().pad(10);

        stage.addActor(table);
    }

    /**
     * Renders the menu screen, including clearing the screen and drawing UI elements.
     * @param delta the time in seconds between the last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the application window is resized.
     *
     * @param width the new width
     * @param height the new height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
