package com.shiroyama.chess2.chessboard.pieces;

public class PieceInfo {

    public Team team;
    public PieceType pieceType;

    public PieceInfo(Team team, PieceType pieceType) {
        super();
        this.team = team;
        this.pieceType = pieceType;
    }

    public String getName(){
        return ((team == Team.WHITE) ? "white" : "black")+ "-" + pieceType.toString().toLowerCase();
    }
}
