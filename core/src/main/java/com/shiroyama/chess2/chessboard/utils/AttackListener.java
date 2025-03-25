package com.shiroyama.chess2.chessboard.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

/**
 * Interface for handling attack events in a chess game.
 * Implementers of this interface will be notified when one piece
 * attacks another piece on the board.
 */
public interface AttackListener {
    /**
     * Called when an attack occurs between two pieces.
     *
     * @param attacker the piece that is attacking
     * @param defender the piece that is being attacked
     */
    void onAttack(PieceInfo attacker, PieceInfo defender);
}
