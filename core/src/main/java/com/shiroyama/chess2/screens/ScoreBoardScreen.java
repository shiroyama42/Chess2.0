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
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.utils.ScoreBoardManager;

public class ScoreBoardScreen implements Screen {

    private ChessGame game;
    private Stage stage;
    private Skin skin;
    private ScoreBoardManager scoreBoardManager;

    public ScoreBoardScreen(ChessGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.scoreBoardManager = ScoreBoardManager.getInstance();

        createUI();
        Gdx.input.setInputProcessor(stage);
    }

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
                dispose();
                scoreBoardManager.reset();
            }
        });
        mainTable.add(backButton).colspan(4).width(200).height(50);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
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

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
