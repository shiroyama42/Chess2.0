package com.shiroyama.chess2.arena;

import com.badlogic.gdx.Gdx;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {
    private PieceInfo attacker;
    private PieceInfo defender;
    private List<Projectile> projectiles;
    private boolean combatStarted = false;

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

    public void startCombat(){
        combatStarted = true;
    }

    public boolean isCombatOver(){
        return attacker.getHp() <= 0 || defender.getHp() <= 0;
    }

    public boolean attackerWon(){
        return defender.getHp() <= 0;
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
