package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.chessboard.controller.GameState;
import com.shiroyama.chess2.chessboard.TextureLoader;

import java.awt.*;
import java.util.HashMap;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private ChessBoard board;
    private GameState gameState;

    private float centerX, centerY;

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
        Gdx.input.setInputProcessor(gameState);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Gdx.graphics.setWindowedMode(700, 800);

        batch.begin();
        board.draw(batch, centerX, centerY);
        gameState.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
