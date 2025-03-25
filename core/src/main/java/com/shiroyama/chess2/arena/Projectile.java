package com.shiroyama.chess2.arena;

import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Team;


/**
 * Represents a projectile in the arena.
 * Projectiles are fired by chess pieces and move across the arena.
 */
public class Projectile {

    /**
     * The team of the piece that fired the projectile.
     */
    private Team shooterTeam;

    /**
     * The current position of the projectile.
     */
    private TargetPoint position;

    /**
     * The velocity vector of the projectile.
     */
    private TargetPoint velocity;

    /**
     * The size of the projectile for collision detection.
     */
    private static final float PROJECTILE_SIZE = 10;

    /**
     * Constructor for the Projectile class.
     * Creates a new projectile.
     *
     * @param shooterTeam the team of the piece that fired the projectile
     * @param startPosition the initial position of the projectile
     * @param targetPosition the position toward which the projectile will move
     */
    public Projectile(Team shooterTeam, TargetPoint startPosition, TargetPoint targetPosition){
        this.shooterTeam = shooterTeam;
        this.position = startPosition;
        this.velocity = calculateVelocity(startPosition, targetPosition);
    }

    /**
     * Updated the projectile's position based on its velocity.
     *
     * @param deltaTime the time elapsed since the last update
     */
    public void update(float deltaTime){
        position = new TargetPoint(
            position.getX() + velocity.getX() * deltaTime,
            position.getY() + velocity.getY() * deltaTime
        );
    }

    /**
     * Check if the projectile is out of the arena boundaries.
     *
     * @param arenaBounds the boundaries of the arena
     * @return true if the projectile is out of bounds, false otherwise
     */
    public boolean isOutOfBounds(TargetPoint arenaBounds){
        return position.getX() < 0 || position.getX() > arenaBounds.getX()
            || position.getY() < 0 || position.getY() > arenaBounds.getY();
    }

    /**
     * Check if the projectile hit the other piece.
     *
     * @param piece the piece to check for collision
     * @return true if the projectile hit the piece, false otherwise
     */
    public boolean hit(PieceInfo piece){

        float pieceX = piece.getPosition().getX() * 50;
        float pieceY = piece.getPosition().getY() * 50;
        float projectileX = position.getX() * 50;
        float projectileY = position.getY() * 50;

        float pieceSize = 50f;

        return piece.getTeam() != shooterTeam
            && projectileX < pieceX + pieceSize
            && projectileX + PROJECTILE_SIZE > pieceX
            && projectileY < pieceY + pieceSize
            && projectileY + PROJECTILE_SIZE > pieceY;
    }

    /**
     * Calculates the velocity vector for the projectile based on the start and target positions.
     *
     * @param start the start position
     * @param target the target position
     * @return a normalised velocity vector scaled by speed
     */
    private TargetPoint calculateVelocity(TargetPoint start, TargetPoint target){
        float dx = target.getX() - start.getX();
        float dy = target.getY() - start.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        return new TargetPoint((float) (dx / length * 5), (float) (dy / length * 5));
    }

    /**
     * Gets the team of the shooter.
     *
     * @return the team which fired the projectile
     */
    public Team getShooterTeam() {
        return shooterTeam;
    }

    /**
     * Sets the team of the shooter.
     *
     * @param shooterTeam the team to set
     */
    public void setShooterTeam(Team shooterTeam) {
        this.shooterTeam = shooterTeam;
    }

    /**
     * Gets the position of the projectile.
     *
     * @return the current position of the projectile
     */
    public TargetPoint getPosition() {
        return position;
    }

    /**
     * Sets the position of the projectile
     *
     * @param position the new position
     */
    public void setPosition(TargetPoint position) {
        this.position = position;
    }

    /**
     * Gets the velocity of the projectile.
     *
     * @return the velocity vector
     */
    public TargetPoint getVelicoty() {
        return velocity;
    }

    /**
     * Sets the velocity of the projectile.
     *
     * @param velocity the new velocity
     */
    public void setVelicoty(TargetPoint velocity) {
        this.velocity = velocity;
    }
}
