package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shiroyama.chess2.ChessGame;

public class MenuScreen implements Screen {

    private ChessGame chessGame;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    public MenuScreen(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Load the Skin (you can customize or add your own skin)
        skin = new Skin(Gdx.files.internal("uiskin.json")); // make sure to add this skin to your assets folder

        // Create the buttons
        TextButton playButton = new TextButton("Local Play", skin);
        TextButton aiButton = new TextButton("VS AI", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.getLabel().setFontScale(2);
        aiButton.getLabel().setFontScale(2);
        exitButton.getLabel().setFontScale(2);

        // Add listeners to the buttons
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                chessGame.setScreen(new GameScreen()); // Takes you to GameScreen
            }
        });

        aiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // VS AI button does nothing for now
                System.out.println("VS AI button clicked");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the game
            }
        });

        // Create a table to organize the layout
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(playButton).fillX().uniformX().pad(10); // Adds "Local Play" button
        table.row().pad(10);
        table.add(aiButton).fillX().uniformX().pad(10); // Adds "VS AI" button
        table.row().pad(10);
        table.add(exitButton).fillX().uniformX().pad(10); // Adds "Exit" button

        // Add the table to the stage
        stage.addActor(table);
    }

    @Override
    public void render(float v) {
        // Clear the screen with a color
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage (the UI)
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

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

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
