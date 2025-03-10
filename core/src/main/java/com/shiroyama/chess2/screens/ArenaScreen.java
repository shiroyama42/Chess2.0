package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.arena.model.Arena;
import com.shiroyama.chess2.arena.model.PieceMovementHandler;
import com.shiroyama.chess2.arena.model.Projectile;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ArenaScreen implements Screen {

    private ChessGame game;
    private SpriteBatch batch;
    private Texture attackerTexture, defenderTexture, gunTexture, projectileTexture;
    private Arena arena;
    private PieceMovementHandler movementHandler;

    private BitmapFont font;
    private GlyphLayout layout;
    private Stage stage;
    private Skin skin;
    private boolean kingDied = false;
    private String gameOverMessage;
    private TextButton menuButton;

    private ShapeRenderer shapeRenderer;

    public ArenaScreen(PieceInfo attacker, PieceInfo defender, ChessGame game){
        this.batch = new SpriteBatch();
        this.arena = new Arena(attacker, defender);
        this.game = game;
        this.movementHandler = new PieceMovementHandler();

        attackerTexture = new Texture("chess_pieces/" + attacker.getName() + ".png");
        defenderTexture = new Texture("chess_pieces/" + defender.getName() + ".png");

        gunTexture = new Texture("arena_textures/gun.png");
        projectileTexture = new Texture("arena_textures/projectile.png");

        font = new BitmapFont();
        font.getData().setScale(3);
        font.setColor(1, 0, 0, 1);
        layout = new GlyphLayout();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        this.shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(254f / 255f, 156f / 255f, 28f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!kingDied){
            arena.update(delta);
        }

        if (!kingDied && arena.isCombatOver()){
            PieceInfo winner = arena.attackerWon() ? arena.getAttacker() : null;
            PieceInfo loser = arena.attackerWon() ? arena.getDefender() : arena.getAttacker();

            if(loser.pieceType == PieceType.KING){
                kingDied = true;
                String winningTeam = winner.team == Team.WHITE ? "WHITE" : "BLACK";

                String losingTeam = loser.team == Team.WHITE ? "WHITE" : "BLACK";

                gameOverMessage = losingTeam + " KING DIED\n" + winningTeam + " TEAM WON!";

                menuButton = new TextButton("Return to Menu", skin);
                menuButton.setSize((float) (Gdx.graphics.getWidth() * 0.3), 60);
                menuButton.setPosition(
                    (Gdx.graphics.getWidth() - menuButton.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 4
                );
                menuButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        game.create();
                    }
                });

                stage.addActor(menuButton);
            }else{
                ((GameScreen) game.getScreen()).exitArena(winner, loser);
                return;
            }


        }

        batch.begin();
        drawPiece(attackerTexture, arena.getAttacker().getPosition(), arena.getDefender().getPosition(), gunTexture);
        drawPiece(defenderTexture, arena.getDefender().getPosition(), arena.getAttacker().getPosition(), gunTexture);

        List<Projectile> projectiles = arena.getProjectiles();
        for (Projectile projectile : projectiles){
            batch.draw(projectileTexture, projectile.position.getX() * 50, projectile.position.getY() * 50, 10, 10);
        }

        if (kingDied && gameOverMessage != null) {
            layout.setText(font, gameOverMessage);
            float textX = (Gdx.graphics.getWidth() - layout.width) / 2;
            float textY = Gdx.graphics.getHeight() / 2 + layout.height / 2;
            font.draw(batch, gameOverMessage, textX, textY);
        }

        batch.end();

        drawHealthBar(arena.getAttacker(), arena.getAttacker().getPosition().getX() * 50,
            (arena.getAttacker().getPosition().getY() + 1) * 50);
        drawHealthBar(arena.getDefender(), arena.getDefender().getPosition().getX() * 50,
            (arena.getDefender().getPosition().getY() + 1) * 50);

        if (kingDied) {
            stage.act(delta);
            stage.draw();
        } else {
            handleMovement();
        }
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
        batch.dispose();
        attackerTexture.dispose();
        defenderTexture.dispose();
        gunTexture.dispose();
        font.dispose();
        stage.dispose();
        if (skin != null) {
            skin.dispose();
        }
    }

    private void drawPiece(Texture texture, TargetPoint position, TargetPoint targetPosition, Texture gunTexture){
        if(position == null || targetPosition == null){
            return;
        }

        batch.draw(texture, position.getX() * 50, position.getY() * 50, 50, 50);

        float angle = getGunAngle(targetPosition, position);
        float gunWidth =  10;
        float gunHeight = 10;
        float gunOffset = 5;

        batch.draw(gunTexture,
            position.getX() * 50 + gunOffset - gunWidth / 2,
            position.getY() * 50 + gunOffset - gunHeight / 2,
            gunWidth / 2,
            gunHeight / 2,
            gunWidth,
            gunHeight,
            6,
            6,
            angle,
            0,
            0,
            gunTexture.getWidth(),
            gunTexture.getHeight(),
            false,
            false
        );
    }

    private float getGunAngle(TargetPoint targetPosition, TargetPoint piecePosition){
        float dx = targetPosition.getX() - piecePosition.getX();
        float dy = targetPosition.getY() - piecePosition.getY();
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }

    private void handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movementHandler.moveUp((arena.getAttacker().team == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movementHandler.moveDown((arena.getAttacker().team == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movementHandler.moveLeft((arena.getAttacker().team == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movementHandler.moveRight((arena.getAttacker().team == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movementHandler.moveUp((arena.getDefender().team == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movementHandler.moveDown((arena.getDefender().team == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movementHandler.moveLeft((arena.getDefender().team == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movementHandler.moveRight((arena.getDefender().team == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
    }

    private void drawHealthBar(PieceInfo piece, float x, float y){
        float healthPercentage = (float) piece.hp / loadDefaultHp(piece.pieceType);

        float barWidth = 50;
        float barHeight = 5;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(x, y, barWidth * healthPercentage, barHeight);
        shapeRenderer.end();
    }

    private int loadDefaultHp(PieceType pieceType){
        Properties props =  new Properties();
        FileHandle file = Gdx.files.internal("stats.cfg");

        try {
            props.load(file.reader());
            String pieceTypeName = pieceType.toString();

            return Integer.parseInt(props.getProperty(pieceTypeName + ".hp"));

        }catch (IOException e){
            Gdx.app.error("PieceInfo", "Error loading stats.cfg: " + e.getMessage());
            return PieceInfo.getDefaultHp(pieceType);
        }
    }

}
