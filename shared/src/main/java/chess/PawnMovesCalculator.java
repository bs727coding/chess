package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int r = myPosition.getRow();
        final int c = myPosition.getColumn();
        final ChessPosition originalPosition = new ChessPosition(r, c);
        Collection<ChessMove> moveList = new ArrayList<>();
        final ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i, j;
        if (color == ChessGame.TeamColor.WHITE) {
            i = r + 1;
            j = c;
            if (i < 9) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) == null) {
                    if (i == 8) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            j = c - 1;
            if (j > 0 && i < 9) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color) {
                    if (i == 8) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            j = c + 1;
            if (j < 9 && i < 9) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color) {
                    if (i == 8) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            if (r == 2) {
                i = r + 2;
                j = c;
                ChessPosition newPosition1 = new ChessPosition(i, j);
                ChessPosition newPosition2 = new ChessPosition(i - 1, j);
                if (board.getPiece(newPosition1) == null && board.getPiece(newPosition2) == null) {
                    moveList.add(new ChessMove(originalPosition, newPosition1, null));
                }
            }
        } else if (color == ChessGame.TeamColor.BLACK) {
            i = r - 1;
            j = c;
            if (i > 0) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) == null) {
                    if (i == 1) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            j = c - 1;
            if (j > 0 && i > 0) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color) {
                    if (i == 1) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            j = c + 1;
            if (j < 9 && i > 0) {
                ChessPosition newPosition = new ChessPosition(i, j);
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() != color) {
                    if (i == 1) {
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(originalPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moveList.add(new ChessMove(originalPosition, newPosition, null));
                    }
                }
            }
            if (r == 7) {
                i = r - 2;
                j = c;
                ChessPosition newPosition1 = new ChessPosition(i, j);
                ChessPosition newPosition2 = new ChessPosition(i + 1, j);
                if (board.getPiece(newPosition1) == null && board.getPiece(newPosition2) == null) {
                    moveList.add(new ChessMove(originalPosition, newPosition1, null));
                }
            }
        }
        return moveList;
    }
}
