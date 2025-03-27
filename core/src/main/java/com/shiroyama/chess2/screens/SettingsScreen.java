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
import com.shiroyama.chess2.utils.ConfigurationManager;

public class SettingsScreen implements Screen {

    private ChessGame game;
    private Stage stage;
    private Skin skin;
    private ConfigurationManager configurationManager;

    private final String[][] resolutions = {
        {"640x480", "640", "480"},
        {"800x600", "800", "600"},
        {"1024x768", "1024", "768"},
        {"1280x720", "1280", "720"},
        {"1366x768", "1366", "768"},
        {"1920x1080", "1920", "1080"}
    };

    private SelectBox<String> resolutionSelect;
    private CheckBox vsyncCheckbox;
    private CheckBox fullscreenCheckbox;

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
        mainTable.setFillParent(false);
        stage.addActor(mainTable);

        mainTable.row().pad(20);
        mainTable.add(new Label("Chess Piece Stats Settings", skin, "title")).colspan(4);
        mainTable.row().pad(10);

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
        mainTable.add(new Label("Graphics Settings", skin, "title")).colspan(4);
        mainTable.row().pad(10);

        mainTable.add(new Label("Resolution: ", skin)).right().padRight(10);
        resolutionSelect = new SelectBox<>(skin);
        resolutionSelect.setItems(getResolutionLabels());
        setCurrentResolution();
        resolutionSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                String[] res = resolutions[resolutionSelect.getSelectedIndex()];
                configurationManager.setWindowSize(Integer.parseInt(res[1]), Integer.parseInt(res[2]));
            }
        });
        mainTable.add(resolutionSelect).left().colspan(3);
        mainTable.row().pad(10);

        mainTable.add(new Label("Fullscreen: ", skin)).right().padRight(10);
        fullscreenCheckbox = new CheckBox("", skin);
        fullscreenCheckbox.setChecked(configurationManager.isFullscreenEnabled());
        fullscreenCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                configurationManager.setFullscreen(fullscreenCheckbox.isChecked());
            }
        });
        mainTable.add(fullscreenCheckbox).left();

        mainTable.add(new Label("VSync: ", skin)).right().padRight(10);
        vsyncCheckbox = new CheckBox("", skin);
        vsyncCheckbox.setChecked(configurationManager.isVSyncEnabled());
        vsyncCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                configurationManager.setVSync(vsyncCheckbox.isChecked());
            }
        });
        mainTable.add(vsyncCheckbox).left();

        mainTable.row().pad(10);
        TextButton applyButton = new TextButton("Apply Graphics Settings", skin);
        applyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                applyGraphicsSettings();
            }
        });
        mainTable.add(applyButton).colspan(4).width(200).height(50);

        mainTable.row().pad(5);
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });

        mainTable.add(backButton).colspan(4).width(200).height(50);

        ScrollPane scrollPane = new ScrollPane(mainTable, skin);
        scrollPane.setFillParent(true);
        scrollPane.setFadeScrollBars(false);

        stage.addActor(scrollPane);
    }

    private String[] getResolutionLabels() {
        String[] labels = new String[resolutions.length];
        for (int i = 0; i < resolutions.length; i++) {
            labels[i] = resolutions[i][0];
        }
        return labels;
    }

    private void setCurrentResolution() {
        int currentWidth = configurationManager.getWindowWidth();
        int currentHeight = configurationManager.getWindowHeight();
        String currentRes = currentWidth + "x" + currentHeight;

        for (int i = 0; i < resolutions.length; i++) {
            if (resolutions[i][0].equals(currentRes)) {
                resolutionSelect.setSelectedIndex(i);
                break;
            }
        }
    }

    private void applyGraphicsSettings() {
        configurationManager.saveConfiguration();

        Gdx.graphics.setVSync(configurationManager.isVSyncEnabled());

        if (configurationManager.isFullscreenEnabled()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(
                configurationManager.getWindowWidth(),
                configurationManager.getWindowHeight()
            );
        }
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
        stage.dispose();
        skin.dispose();
    }
}
