package com.shiroyama.chess2.chessboard.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

/**
 * Interface for handling pawn promotion events in the chess game.
 * Implementation of this interface will be notified when a pawn reaches
 * the opposite end of the board and is eligible for promotion.
 */
public interface PromotionListener {
    /**
     * Called when a pawn is eligible for promotion.
     *
     * @param piece the pawn piece that is being promoted.
     */
    void onPromote(PieceInfo piece);
}
