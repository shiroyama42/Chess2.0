package com.shiroyama.chess2.arena;

import com.badlogic.gdx.Gdx;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the combat area where the pieces fight each other.
 * Manages combat state, {@link Projectile} and interactions between the two pieces.
 */
public class Arena {

    /**
     * The attacking piece.
     */
    private PieceInfo attacker;

    /**
     * The defending piece.
     */
    private PieceInfo defender;

    /**
     * List of active {@link Projectile} in the arena.
     */
    private List<Projectile> projectiles;

    /**
     * Flag indicating whether the combat started.
     */
    private boolean combatStarted = false;

    /**
     * The time since the attacker last shot.
     */
    private float attackerTimeSinceLastShot;

    /**
     * The time since the defender last shot.
     */
    private float defenderTimeSinceLastShot;

    /**
     * Constructor for the arena class.
     *
     * @param attacker the piece which initiated the start of the combat
     * @param defender the piece which got attacked
     */
    public Arena(PieceInfo attacker, PieceInfo defender){
        this.attacker = attacker;
        this.defender = defender;
        this.projectiles = new ArrayList<>();
        this.attackerTimeSinceLastShot = 0f;
        this.defenderTimeSinceLastShot = 0f;
    }

    /**
     * Updates the arena state, including projectile movements and combat logic.
     *
     * @param deltaTime the time elapsed since the last update
     */
    public void update(float deltaTime){

        if (!combatStarted) return;

        attackerTimeSinceLastShot += deltaTime;
        defenderTimeSinceLastShot += deltaTime;

        if (attackerTimeSinceLastShot >= 1f / attacker.getAttackRate()){
            attacker.shoot(defender.getPosition(), projectiles);
            attackerTimeSinceLastShot = 0f;
        }

        if (defenderTimeSinceLastShot >= 1f / defender.getAttackRate()){
            defender.shoot(attacker.getPosition(), projectiles);
            defenderTimeSinceLastShot = 0f;
        }

        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()){
            Projectile projectile = iterator.next();
            projectile.update(deltaTime);

            if (projectile.hit(attacker)){
                attacker.setHp(attacker.getHp() - 1);
                iterator.remove();
            } else if (projectile.hit(defender)) {
                defender.setHp(defender.getHp() -1);
                iterator.remove();
            }

            if (projectile.isOutOfBounds(new TargetPoint((float) Gdx.graphics.getWidth() / 50,
                (float) Gdx.graphics.getHeight() / 50))){
                iterator.remove();
            }
        }
    }

    /**
     * Starts the combat between the attacker and defender.
     */
    public void startCombat(){
        combatStarted = true;
    }

    /**
     * Checks if the combat is over.
     *
     * @return true if the combat is over, false otherwise
     */
    public boolean isCombatOver(){
        return attacker.getHp() <= 0 || defender.getHp() <= 0;
    }

    /**
     * Check if the attacking piece won.
     *
     * @return true if the attacker won, false otherwise
     */
    public boolean attackerWon(){
        return defender.getHp() <= 0;
    }

    /**
     * Gets the list of active {@link Projectile}.
     *
     * @return a list containing the active projectiles
     */
    public List<Projectile> getProjectiles(){
        return projectiles;
    }

    /**
     * Gets the attacking piece.
     *
     * @return the attacker piece
     */
    public PieceInfo getAttacker() {
        return attacker;
    }

    /**
     * Sets the attacking piece.
     *
     * @param attacker the attacker piece
     */
    public void setAttacker(PieceInfo attacker) {
        this.attacker = attacker;
    }

    /**
     * Gets the defending piece.
     *
     * @return the defender piece
     */
    public PieceInfo getDefender() {
        return defender;
    }

    /**
     * Sets the defending piece.
     *
     * @param defender the defender piece
     */
    public void setDefender(PieceInfo defender) {
        this.defender = defender;
    }
}
