package com.shiroyama.chess2.chessboard.utils;

import com.shiroyama.chess2.chessboard.pieces.PieceInfo;

public interface AttackListener {
    void onAttack(PieceInfo attacker, PieceInfo defender);
}
