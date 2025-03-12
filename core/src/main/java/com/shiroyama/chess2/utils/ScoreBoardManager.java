package com.shiroyama.chess2.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoardManager {

    private static ScoreBoardManager instance;

    private int moveCount;

    private Map<Team, Map<PieceType, Integer>> capturedPieces;

    public ScoreBoardManager() {
        moveCount = 0;
        capturedPieces = new HashMap<>();
        capturedPieces.put(Team.WHITE, initializeMap());
        capturedPieces.put(Team.BLACK, initializeMap());
    }

    public static ScoreBoardManager getInstance(){
        if (instance == null){
            instance = new ScoreBoardManager();
        }
        return instance;
    }

    private Map<PieceType, Integer> initializeMap(){
        Map<PieceType, Integer> map = new HashMap<>();
        for (PieceType type : PieceType.values()){
            map.put(type, 0);
        }
        return map;
    }

    public void addMoveCount(){
        moveCount++;
    }

    public void recordCapture(PieceInfo capturedPiece, Team capturingTeam){
        if (capturedPiece != null){
            Map<PieceType, Integer> teamCaptures = capturedPieces.get(capturingTeam);
            PieceType pieceType = capturedPiece.pieceType;
            teamCaptures.put(pieceType, teamCaptures.get(pieceType) + 1);
        }
    }

    public int getMoveCount() {
        return moveCount;
    }

    public Map<PieceType, Integer> getCapturedPieces(Team team) {
        return capturedPieces.get(team);
    }

    public void reset(){
        moveCount = 0;
        capturedPieces.get(Team.WHITE).replaceAll((k, v) -> 0);
        capturedPieces.get(Team.BLACK).replaceAll((k, v) -> 0);
    }
}
