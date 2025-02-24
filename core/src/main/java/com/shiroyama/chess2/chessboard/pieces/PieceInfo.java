package com.shiroyama.chess2.chessboard.pieces;

import com.badlogic.gdx.math.Vector2;
import com.shiroyama.chess2.arena.model.Projectile;

import java.util.List;

public class PieceInfo {

    public Team team;
    public PieceType pieceType;
    public int hp;
    public float attackRate;

    public PieceInfo(Team team, PieceType pieceType) {
        super();
        this.team = team;
        this.pieceType = pieceType;
        this.hp = getBaseHP(pieceType);
        this.attackRate = getAttackRate(pieceType);
    }

    public String getName(){
        return ((team == Team.WHITE) ? "white" : "black")+ "-" + pieceType.toString().toLowerCase();
    }

    private int getBaseHP(PieceType pieceType){
        switch (pieceType){
            case PAWN: return 1;
            case KNIGHT: return 10;
            case BISHOP: return 3;
            case ROOK: return 10;
            case QUEEN: return 15;
            case KING: return 20;
            default: return 1;
        }
    }

    private float getAttackRate(PieceType pieceType){
        switch (pieceType){
            case PAWN: return 1f;
            case KNIGHT: return 1.5f;
            case BISHOP: return 2f;
            case ROOK: return 2.5f;
            case QUEEN: return 3f;
            case KING: return 1f;
            default: return 1f;
        }
    }

    public void shoot(Vector2 targetPosition, List<Projectile> projectiles){
        Vector2 position = new Vector2(0, 0);
        Projectile projectile = new Projectile(this.team, position, targetPosition);
        projectiles.add(projectile);
    }

    public Vector2 getPosition(){
        return new Vector2(0, 0);
    }
}
