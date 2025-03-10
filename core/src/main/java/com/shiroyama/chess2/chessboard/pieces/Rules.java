package com.shiroyama.chess2.chessboard.pieces;

import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.chessboard.model.TargetPoint;

import java.util.ArrayList;

public class Rules {

    public Rules() {}

    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, ChessBoard board){
        switch (piece.pieceType){
            case PAWN:
                getValidMovesPawn(list, selection, piece.team, board);
                break;
            case BISHOP:
                getValidMovesBishop(list, selection, piece.team, board);
                break;
            case ROOK:
                getValidMovesRook(list, selection, piece.team, board);
                break;
            case KNIGHT:
                getValidMovesKnight(list, selection, piece.team, board);
                break;
            case QUEEN:
                getValidMovesQueen(list, selection, piece.team, board);
                break;
            case KING:
                getValidMovesKing(list, selection, piece.team, board);
                break;
        }
    }

    private static void getValidMovesPawn(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        int direction = (team == Team.BLACK) ? 1 : -1;

        TargetPoint normalMove = selection.Transpose(0, direction);
        if (board.isInBounds(normalMove) && board.getPiece(normalMove) == null){
            list.add(normalMove);
        }

        int startRow = (team == Team.BLACK) ? 1 : 6;

        TargetPoint firstMove = selection.Transpose(0, direction * 2);
        if (selection.getY() == startRow && board.isInBounds(firstMove) && board.getPiece(firstMove) == null){
            list.add(firstMove);
        }

        for(int i = -1; i <= 1; i += 2){
            TargetPoint captureMove = selection.Transpose(i, direction);
            PieceInfo target = board.getPiece(captureMove);
            if(board.isInBounds(captureMove) && target != null && target.team != team){
                list.add(captureMove);
            }
        }
    }

    private static void getValidMovesBishop(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int xDir = -1; xDir <= 1; xDir += 2){
            for (int yDir = -1; yDir <= 1; yDir += 2){
                TargetPoint move = selection;
                while (true){
                    move = move.Transpose(xDir, yDir);
                    if (!board.isInBounds(move)){
                        break;
                    }
                    PieceInfo target = board.getPiece(move);
                    if (target != null){
                        if (target.team != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    private static void getValidMovesRook(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int direction = 0; direction < 2; direction++){
            for (int dir = -1; dir <= 1; dir += 2){
                TargetPoint move = selection;
                while (true){

                    if (direction == 0){
                        move = move.Transpose(dir, 0);
                    }else{
                        move = move.Transpose(0, dir);
                    }

                    if (!board.isInBounds(move)){
                        break;
                    }
                    PieceInfo target = board.getPiece(move);
                    if (target != null){
                        if (target.team != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    private static void getValidMovesKnight(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int direction = 0; direction < 2; direction++){
            for (int longDir = -2; longDir <= 2; longDir += 4){
                for (int shortDir = -1; shortDir <= 1; shortDir += 2){

                    TargetPoint move;
                    if (direction == 0){
                        move = selection.Transpose(longDir, shortDir);
                    }else{
                        move = selection.Transpose(shortDir, longDir);
                    }

                    PieceInfo target = board.getPiece(move);
                    if (board.isInBounds(move)){
                        if (target == null || target.team != team){
                            list.add(move);
                        }
                    }
                }
            }
        }
    }

    private static void getValidMovesQueen(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int xDir = -1; xDir <= 1; xDir++){
            for (int yDir = -1; yDir <= 1; yDir++){
                if (xDir == 0 && yDir == 0) {
                    continue;
                }
                TargetPoint move = selection;
                while (true){

                    move = move.Transpose(xDir, yDir);
                    if (!board.isInBounds(move)){
                        break;
                    }

                    PieceInfo target = board.getPiece(move);
                    if (target != null){
                        if (target.team != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    private static void getValidMovesKing(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int xDir = - 1; xDir <= 1; xDir++){
            for (int yDir = -1; yDir <= 1; yDir++){

                if (xDir == 0 && yDir == 0){
                    continue;
                }
                TargetPoint move = selection.Transpose(xDir, yDir);
                PieceInfo target = board.getPiece(move);
                if (board.isInBounds(move)){
                    if (target == null || target.team != team){
                        list.add(move);
                    }
                }
            }
        }
    }
}
