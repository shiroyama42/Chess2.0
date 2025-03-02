package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.arena.model.Arena;
import com.shiroyama.chess2.arena.model.PieceMovementHandler;
import com.shiroyama.chess2.arena.model.Projectile;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.util.List;

public class ArenaScreen implements Screen {

    private ChessGame game;
    private SpriteBatch batch;
    private Texture attackerTexture, defenderTexture, gunTexture, projectileTexture;
    private Arena arena;
    private PieceMovementHandler movementHandler;

    public ArenaScreen(PieceInfo attacker, PieceInfo defender, ChessGame game){
        this.batch = new SpriteBatch();
        this.arena = new Arena(attacker, defender);
        this.game = game;
        this.movementHandler = new PieceMovementHandler();

        attackerTexture = new Texture(attacker.getName() + ".png");
        defenderTexture = new Texture(defender.getName() + ".png");

        gunTexture = new Texture("gun.png");
        projectileTexture = new Texture("projectile.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(254f / 255f, 156f / 255f, 28f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        arena.update(delta);

        if (arena.isCombatOver()){
            PieceInfo winner = arena.attackerWon() ? arena.getAttacker() : null;

        }

        batch.begin();
        drawPiece(attackerTexture, arena.getAttacker().getPosition(), arena.getDefender().getPosition(), gunTexture);
        drawPiece(defenderTexture, arena.getDefender().getPosition(), arena.getAttacker().getPosition(), gunTexture);

        List<Projectile> projectiles = arena.getProjectiles();
        for (Projectile projectile : projectiles){
            batch.draw(projectileTexture, projectile.position.getX() * 50, projectile.position.getY() * 50, 10, 10);
        }


        batch.end();


        handleMovement();
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
    }

    private void drawPiece(Texture texture, TargetPoint position, TargetPoint targetPosition, Texture gunTexture){
        if(position == null || targetPosition == null){
            return;
        }

        batch.draw(texture, position.getX() * 50, position.getY() * 50, 50, 50);

        float angle = getGunAngle(targetPosition, position);
        batch.draw(gunTexture, position.getX() * 50, position.getY() * 50, 10, 10, 5, 5);
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

}
