package com.shiroyama.chess2.chessboard.pieces;

import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.chessboard.model.TargetPoint;

import java.util.ArrayList;

/**
 * Defines the rules for the valid moves of chess pieces on the chess board.
 */
public class Rules {

    /**
     * Creates a new Rule object.
     */
    public Rules() {}

    /**
     * Determines and add valid moves for a given piece to the provided list.
     *
     * @param list the list which the valid moves will be added
     * @param selection the current position of the piece
     * @param piece the {@link PieceInfo} for which to determine valid moves
     * @param board the {@link ChessBoard} on which the piece is located
     */
    public static void GetValidMoves(ArrayList<TargetPoint> list, TargetPoint selection, PieceInfo piece, ChessBoard board){
        switch (piece.getPieceType()){
            case PAWN:
                getValidMovesPawn(list, selection, piece.getTeam(), board);
                break;
            case BISHOP:
                getValidMovesBishop(list, selection, piece.getTeam(), board);
                break;
            case ROOK:
                getValidMovesRook(list, selection, piece.getTeam(), board);
                break;
            case KNIGHT:
                getValidMovesKnight(list, selection, piece.getTeam(), board);
                break;
            case QUEEN:
                getValidMovesQueen(list, selection, piece.getTeam(), board);
                break;
            case KING:
                getValidMovesKing(list, selection, piece.getTeam(), board);
                break;
        }
    }

    /**
     * Determines valid moves for a pawn.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the pawn
     * @param team the {@link Team} of the pawn (BLACK or WHITE)
     * @param board the {@link ChessBoard} on which the pawn is located
     */
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
            if(board.isInBounds(captureMove) && target != null && target.getTeam() != team){
                list.add(captureMove);
            }
        }
    }

    /**
     * Determines valid moves for a bishop.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the bishop
     * @param team  the {@link Team} of the bishop (BLACK or WHITE)
     * @param board the {@link ChessBoard} on which the bishop is located
     */
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
                        if (target.getTeam() != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    /**
     * Determines valid moves for a rook.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the rook
     * @param team the {@link Team} of the rook (BLACK or WHITE)
     * @param board the {@link ChessBoard}on which the rook is located
     */
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
                        if (target.getTeam() != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    /**
     * Determines valid moves for a knight.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the knight
     * @param team the {@link Team} of the knight (BLACK or WHITE)
     * @param board the {@link ChessBoard} on which the knight is located
     */
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
                        if (target == null || target.getTeam() != team){
                            list.add(move);
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines valid moves for a queen.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the queen
     * @param team the {@link Team} of the queen (BLACK or WHITE)
     * @param board the {@link ChessBoard} on which the queen is located
     */
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
                        if (target.getTeam() != team){
                            list.add(move);
                        }
                        break;
                    }
                    list.add(move);
                }
            }
        }
    }

    /**
     * Determines valid moves for a king.
     *
     * @param list the list to which the valid moves will be added
     * @param selection the current position of the king
     * @param team the {@link Team} of the king (BLACK or WHITE)
     * @param board the {@link ChessBoard} on which the king is located
     */
    private static void getValidMovesKing(ArrayList<TargetPoint> list, TargetPoint selection, Team team, ChessBoard board){
        for (int xDir = - 1; xDir <= 1; xDir++){
            for (int yDir = -1; yDir <= 1; yDir++){

                if (xDir == 0 && yDir == 0){
                    continue;
                }
                TargetPoint move = selection.Transpose(xDir, yDir);
                PieceInfo target = board.getPiece(move);
                if (board.isInBounds(move)){
                    if (target == null || target.getTeam() != team){
                        list.add(move);
                    }
                }
            }
        }
    }
}
