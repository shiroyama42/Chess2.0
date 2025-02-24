package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.arena.model.Arena;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.sun.xml.internal.bind.v2.TODO;

public class ArenaScreen implements Screen {

    private SpriteBatch batch;
    private Texture attackerTexture, defenderTexture, gunTexture;
    private Arena arena;

    public ArenaScreen(PieceInfo attacker, PieceInfo defender){
        this.batch = new SpriteBatch();
        this.arena = new Arena(attacker, defender);

        attackerTexture = new Texture(attacker.getName() + ".png");
        defenderTexture = new Texture(defender.getName() + ".png");

        //TODO: texture for projectile and gun
        gunTexture = new Texture("gun.png");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        arena.update(delta);

        batch.begin();
        drawPiece(attackerTexture, arena.getAttacker().getPosition(), arena.getDefender().getPosition(), gunTexture);
        drawPiece(defenderTexture, arena.getDefender().getPosition(), arena.getAttacker().getPosition(), gunTexture);

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

    private void drawPiece(Texture texture, Vector2 position, Vector2 targetPosition, Texture gunTexture){
        batch.draw(texture, position.x - 20, position.y -20, 40, 40);
        float angle = getGunAngle(targetPosition, position);
        batch.draw(gunTexture, position.x - 10, position.y - 10, 10, 10, 20, 20);
    }

    private float getGunAngle(Vector2 targetPosition, Vector2 piecePosition){
        float dx = targetPosition.x - piecePosition.x;
        float dy = targetPosition.y - piecePosition.y;
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }

    private void handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            //arena.getAttacker().moveUp();
            System.out.println("white up");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            //arena.getAttacker().moveDown();
            System.out.println("white down");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            //arena.getAttacker().moveLeft();
            System.out.println("white left");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            //arena.getAttacker().moveRight();
            System.out.println("white right");
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //arena.getDefender().moveUp();
            System.out.println("black up");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //arena.getDefender().moveDown();
            System.out.println("black down");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //arena.getDefender().moveLeft();
            System.out.println("black left");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //arena.getDefender().moveRight();
            System.out.println("black right");
        }
    }

}
