package com.shiroyama.chess2.chessboard.pieces;

import com.shiroyama.chess2.chessboard.ChessBoard;
import com.shiroyama.chess2.chessboard.TargetPoint;

import java.util.ArrayList;

public class Rules {

    public Rules() {}

    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, ChessBoard board){
        GetValidMoves(list, selection, piece, board, true);
    }

    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, ChessBoard board, boolean checkCheck){

    }

    private static void getValidMovesPawn(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        int direction = (team == Team.BLACK) ? 1 : -1;

        TargetPoint normalMove = selection.Transpose(0, direction);
        //TODO: isInBoudns

        int startRow = (team == Team.BLACK) ? 1 : 6;

        TargetPoint firstMove = selection.Transpose(0, direction * 2);
        //TODO: isInBounds

        for(int i = -1; i <= 1; i += 2){
            TargetPoint captureMove = selection.Transpose(i, direction);
            // TODO: this part
        }
    }
}
