package com.shiroyama.chess2.chessboard.pieces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shiroyama.chess2.arena.model.Projectile;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.utils.ConfigurationManager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class PieceInfo {

    public Team team;
    public PieceType pieceType;
    public int hp;
    public float attackRate;
    public TargetPoint position;

    public PieceInfo(Team team, PieceType pieceType, TargetPoint position) {
        super();
        this.team = team;
        this.pieceType = pieceType;
        this.position = position;
        loadStats(pieceType);
    }

    public String getName(){
        return ((team == Team.WHITE) ? "white" : "black")+ "-" + pieceType.toString().toLowerCase();
    }

    public static int getDefaultHp(PieceType pieceType){
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

    private float getDefaultAttackRate(PieceType pieceType){
        switch (pieceType){
            case PAWN: return 1f;
            case KNIGHT: return 1.5f;
            case BISHOP: return 2.5f;
            case ROOK: return 2f;
            case QUEEN: return 3f;
            case KING: return 1f;
            default: return 1f;
        }
    }

    public void shoot(TargetPoint targetPosition, List<Projectile> projectiles){
        Projectile projectile = new Projectile(this.team, position, targetPosition);
        projectiles.add(projectile);
    }

    public TargetPoint getPosition(){
        return position;
    }

    public void setPosition(TargetPoint position){
        this.position = position;
    }

    private void loadStats(PieceType pieceType){

        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        hp = configurationManager.getHp(pieceType);
        attackRate = configurationManager.getAttackRate(pieceType);
    }
}
