package com.shiroyama.chess2.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the scoreboard manager for the chess game.
 * This class is responsible for tracking game statistics, including the total number of moves made,
 * the count of captured pieces for each team and piece type.
 */
public class ScoreBoardManager {

    /**
     * The singleton instance of the {@code ScoreBoardManager}.
     */
    private static ScoreBoardManager instance;

    /**
     * The total number of moves made during the game.
     */
    private int moveCount;

    /**
     * A nested map that tracks the number of captured pieces for each team and piece type.
     * The outer map uses {@link Team} as the key, and the inner map uses {@link PieceType} as the key.
     */
    private Map<Team, Map<PieceType, Integer>> capturedPieces;

    /**
     * Constructor for the class.
     * Initializes the move count to zero and sets up the captured pieces map with default values.
     */
    public ScoreBoardManager() {
        moveCount = 0;
        capturedPieces = new HashMap<>();
        capturedPieces.put(Team.WHITE, initializeMap());
        capturedPieces.put(Team.BLACK, initializeMap());
    }

    /**
     * Retrieves the singleton instance of the {@code ScoreBoardManager}.
     * @return the singleton instance of the {@code ScoreBoardManager}
     */
    public static ScoreBoardManager getInstance(){
        if (instance == null){
            instance = new ScoreBoardManager();
        }
        return instance;
    }

    /**
     * Initializes a map to store the count of captured pieces for each piece type.
     * All piece types are initialized with a count of zero.
     *
     * @return a map where the keys are {@link PieceType} and the values are the counts of captured pieces
     */
    private Map<PieceType, Integer> initializeMap(){
        Map<PieceType, Integer> map = new HashMap<>();
        for (PieceType type : PieceType.values()){
            map.put(type, 0);
        }
        return map;
    }

    /**
     * Increments the total move count by one.
     */
    public void addMoveCount(){
        moveCount++;
    }

    /**
     * Records the capture of a piece by a specific team.
     *
     * @param capturedPiece the captured piece
     * @param capturingTeam the {@link Team} that captured the piece
     */
    public void recordCapture(PieceInfo capturedPiece, Team capturingTeam){
        if (capturedPiece != null){
            Map<PieceType, Integer> teamCaptures = capturedPieces.get(capturingTeam);
            PieceType pieceType = capturedPiece.getPieceType();
            teamCaptures.put(pieceType, teamCaptures.get(pieceType) + 1);
        }
    }

    /**
     * Retrieves the total number of moves made during the game.
     *
     * @return the number of moves
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Retrieves the map of captured pieces for the specified team.
     *
     * @param team the {@link Team} for which to retrieve the captured pieces
     * @return a map where the keys are {@link PieceType} and the values are the counts of captured pieces
     */
    public Map<PieceType, Integer> getCapturedPieces(Team team) {
        return capturedPieces.get(team);
    }

    /**
     * Resets the scoreboard statistics, including the move count and captured pieces.
     */
    public void reset(){
        moveCount = 0;
        capturedPieces.get(Team.WHITE).replaceAll((k, v) -> 0);
        capturedPieces.get(Team.BLACK).replaceAll((k, v) -> 0);
    }
}
