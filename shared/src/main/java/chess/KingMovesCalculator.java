package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int r = myPosition.getRow();
        final int c = myPosition.getColumn();
        final ChessPosition originalPosition = new ChessPosition(r, c);
        Collection<ChessMove> moveList = new ArrayList<>();
        final ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i, j;
        i = r + 1;
        j = c + 1;
        if (i < 9 && j < 9) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        j = c - 1;
        if (i < 9 && j > 0) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        j = c;
        if (i < 9) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        i = r - 1;
        j = c + 1;
        if (i > 0 && j < 9) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        j = c - 1;
        if (i > 0 && j > 0) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        j = c;
        if (i > 0) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        i = r;
        j = c - 1;
        if (j > 0) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        j = c + 1;
        if (j < 9) {
            ChessPosition newPosition = new ChessPosition(i, j);
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    moveList.add(new ChessMove(originalPosition, newPosition, null));
                }
            } else {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        }
        return moveList;
    }
}
