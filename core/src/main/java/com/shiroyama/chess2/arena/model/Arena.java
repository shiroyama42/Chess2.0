package com.shiroyama.chess2.arena.model;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public class Arena {
    private PieceInfo attacker;
    private PieceInfo defender;

    public Arena(PieceInfo attacker, PieceInfo defender){
        this.attacker = attacker;
        this.defender = defender;
    }

    public void startCombat(){
        while(attacker.hp > 0 && defender.hp > 0){
            attack(attacker, defender);
        }
    }

    private void attack(PieceInfo attacker, PieceInfo defender){
        defender.hp -= attacker.attackRate;

    }
}
