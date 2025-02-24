package com.shiroyama.chess2.arena.model;

import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Team;

public class Projectile {

    public Team shooterTeam;
    public Vector2 position;
    public Vector2 velicoty;

    public Projectile(Team shooterTeam, Vector2 startPosition, Vector2 targetPosition){
        this.shooterTeam = shooterTeam;
        this.position = startPosition.cpy();
        this.velicoty = targetPosition.cpy().sub(startPosition).nor().scl(5f);
    }

    public void update(float deltaTime){
        position.add(velicoty.x * deltaTime, velicoty.y * deltaTime);
    }

    public boolean isOutOfBounds(Vector2 arenaBounds){
        return position.x < 0 || position.x > arenaBounds.x || position.y < 0 || position.y > arenaBounds.y;
    }

    public boolean hit(PieceInfo piece){
        return piece.team != shooterTeam;
    }
}
