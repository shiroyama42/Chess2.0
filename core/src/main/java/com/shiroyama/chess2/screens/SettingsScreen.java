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
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.utils.ConfigurationManager;

public class SettingsScreen implements Screen {

    private ChessGame game;
    private Stage stage;
    private Skin skin;
    private ConfigurationManager configurationManager;

    public SettingsScreen(ChessGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.configurationManager = ConfigurationManager.getInstance();

        createUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void createUI(){
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label titleLabel = new Label("Chess Piece Settings", skin, "title");
        mainTable.add(titleLabel).colspan(3).pad(20);
        mainTable.row();

        mainTable.add(new Label("Piece Type", skin, "default")).pad(10);
        mainTable.add(new Label("HP", skin, "default")).pad(10);
        mainTable.add(new Label("Attack Rate", skin, "default")).pad(10);
        mainTable.row();

        for (final PieceType pieceType : PieceType.values()) {

            mainTable.add(new Label(pieceType.toString(), skin)).pad(5);

            final TextField hpField = new TextField(
                String.valueOf(configurationManager.getHp(pieceType)), skin);
            mainTable.add(hpField).width(100).pad(5);

            final TextField attackRateField = new TextField(
                String.valueOf(configurationManager.getAttackRate(pieceType)), skin);
            mainTable.add(attackRateField).width(100).pad(5);

            TextButton saveButton = new TextButton("Save", skin);
            saveButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {

                    try {
                        int hp = Integer.parseInt(hpField.getText());
                        float attackRate = Float.parseFloat(attackRateField.getText());

                        configurationManager.setHp(pieceType, hp);
                        configurationManager.setAttackRate(pieceType, attackRate);
                        configurationManager.saveConfiguration();

                    } catch (NumberFormatException e) {
                        Dialog dialog = new Dialog("Error", skin);
                        dialog.text("Please enter valid numbers");
                        dialog.button("OK");
                        dialog.show(stage);
                    }
                }
            });
            mainTable.add(saveButton).pad(5);
            mainTable.row();

        }

        mainTable.row().pad(20);
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
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
