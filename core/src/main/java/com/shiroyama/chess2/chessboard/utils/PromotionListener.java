package com.shiroyama.chess2.chessboard.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public interface PromotionListener {
    void onPromote(PieceInfo piece);
}
