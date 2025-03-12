package com.shiroyama.chess2.arena;

import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Team;

public class Projectile {

    public Team shooterTeam;
    public TargetPoint position;
    public TargetPoint velicoty;
    public static final float PROJECTILE_SIZE = 10;

    public Projectile(Team shooterTeam, TargetPoint startPosition, TargetPoint targetPosition){
        this.shooterTeam = shooterTeam;
        this.position = startPosition;
        this.velicoty = calculateVelocity(startPosition, targetPosition);
    }

    public void update(float deltaTime){
        position = new TargetPoint(
            position.getX() + velicoty.getX() * deltaTime,
            position.getY() + velicoty.getY() * deltaTime
        );
    }

    public boolean isOutOfBounds(TargetPoint arenaBounds){
        return position.getX() < 0 || position.getX() > arenaBounds.getX()
            || position.getY() < 0 || position.getY() > arenaBounds.getY();
    }

    public boolean hit(PieceInfo piece){

        float pieceX = piece.getPosition().getX() * 50;
        float pieceY = piece.getPosition().getY() * 50;
        float projectileX = position.getX() * 50;
        float projectileY = position.getY() * 50;

        float pieceSize = 50f;

        return piece.team != shooterTeam
            //&& Math.abs(position.getX() - piece.getPosition().getX()) <= 1
            //&& Math.abs(position.getY() - piece.getPosition().getY()) <= 1;
            && projectileX < pieceX + pieceSize
            && projectileX + PROJECTILE_SIZE > pieceX
            && projectileY < pieceY + pieceSize
            && projectileY + PROJECTILE_SIZE > pieceY;
    }

    private TargetPoint calculateVelocity(TargetPoint start, TargetPoint target){
        float dx = target.getX() - start.getX();
        float dy = target.getY() - start.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        return new TargetPoint((float) (dx / length * 5), (float) (dy / length * 5));
    }
}
