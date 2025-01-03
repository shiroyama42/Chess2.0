package com.shiroyama.chess2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.shiroyama.chess2.chessboard.ChessBoard;
import com.shiroyama.chess2.chessboard.TextureLoader;

import java.util.HashMap;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {

    private SpriteBatch batch;
    private ChessBoard board;

    @Override
    public void show() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        HashMap<String, Texture> textures = TextureLoader.loadPieceTextures();

        int size = (int) Math.min(h, w);
        board = new ChessBoard(size, textures);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        board.draw(batch);
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
