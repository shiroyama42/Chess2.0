package com.shiroyama.chess2.chessboard.pieces;

import com.shiroyama.chess2.chessboard.Chessboard;
import com.shiroyama.chess2.chessboard.TargetPoint;

import java.util.ArrayList;

public class Rules {

    public Rules() {}

    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, Chessboard board){
        GetValidMoves(list, selection, piece, board, true);
    }

    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, Chessboard board, boolean checkCheck){

    }

    private static void getValidMovesPawn(ArrayList<TargetPoint> list, TargetPoint selection, Team team, Chessboard board){
        int direction = (team == Team.BLACK) ? 1 : -1;

        TargetPoint normalMove = selection.Transpose(0, direction);
        //TODO: isInBoudns

        int startRow = (team == Team.BLACK) ? 1 : 6;

        TargetPoint firstMove = selection.Transpose(0, direction * 2);
        //TODO: isInBounds
    }
}
