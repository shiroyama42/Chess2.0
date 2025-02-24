package com.shiroyama.chess2.arena.model;

import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {
    private PieceInfo attacker;
    private PieceInfo defender;
    private List<Projectile> projectiles;

    private float attackerTimeSinceLastShot;
    private float defenderTimeSinceLastShot;

    public Arena(PieceInfo attacker, PieceInfo defender){
        this.attacker = attacker;
        this.defender = defender;
        this.projectiles = new ArrayList<>();
        this.attackerTimeSinceLastShot = 0f;
        this.defenderTimeSinceLastShot = 0f;
    }

    public void update(float deltaTime){
        attackerTimeSinceLastShot += deltaTime;
        defenderTimeSinceLastShot += deltaTime;

        if (attackerTimeSinceLastShot >= 1f / attacker.attackRate){
            attacker.shoot(defender.getPosition(), projectiles);
            attackerTimeSinceLastShot = 0f;
        }

        if (defenderTimeSinceLastShot >= 1f / defender.attackRate){
            defender.shoot(attacker.getPosition(), projectiles);
            defenderTimeSinceLastShot = 0f;
        }

        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()){
            Projectile projectile = iterator.next();
            projectile.update(deltaTime);

            if (projectile.hit(attacker)){
                attacker.hp -= 1;
                iterator.remove();
            } else if (projectile.hit(defender)) {
                defender.hp -= 1;
                iterator.remove();
            }

            if (projectile.isOutOfBounds(new Vector2(800, 800))){
                iterator.remove();
            }
        }
    }

    public boolean isCombatOver(){
        return attacker.hp <= 0 || defender.hp <= 0;
    }

    public boolean attackerWon(){
        return defender.hp <= 0;
    }

    public List<Projectile> getProjectiles(){
        return projectiles;
    }

    public PieceInfo getAttacker() {
        return attacker;
    }

    public void setAttacker(PieceInfo attacker) {
        this.attacker = attacker;
    }

    public PieceInfo getDefender() {
        return defender;
    }

    public void setDefender(PieceInfo defender) {
        this.defender = defender;
    }
}
