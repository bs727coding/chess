package chess;

import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int r = myPosition.getRow();
        final int c = myPosition.getColumn();
        final ChessPosition originalPosition = new ChessPosition(r, c);
        Collection<ChessMove> moveList = new ArrayList<>();
        final ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i = r + 1;
        int j = c + 2;
        topRight(board, originalPosition, moveList, color, i, j);
        i = r + 2;
        j = c + 1;
        topRight(board, originalPosition, moveList, color, i, j);
        topLeft(board, r, c, originalPosition, moveList, color, 1, 2);
        topLeft(board, r, c, originalPosition, moveList, color, 2, 1);
        i = r - 1;
        j = c - 2;
        bottomLeft(board, originalPosition, moveList, color, i, j);
        i = r - 2;
        j = c - 1;
        bottomLeft(board, originalPosition, moveList, color, i, j);
        i = r + 1;
        j = c - 2;
        bottomRight(board, originalPosition, moveList, color, i, j, i < 9, j > 0);
        i = r + 2;
        j = c - 1;
        bottomRight(board, originalPosition, moveList, color, i, j, i < 9, j > 0);
        return moveList;
    }

    static void bottomRight(ChessBoard board, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i, int j, boolean b, boolean b2) {
        if (b && b2) {
            addToList(board, originalPosition, moveList, color, i, j);
        }
    }

    static void addToList(ChessBoard board, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i, int j) {
        ChessPosition newPosition = new ChessPosition(i, j);
        if (board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() != color) {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
        } else {
            moveList.add(new ChessMove(originalPosition, newPosition, null));
        }
    }

    static void bottomLeft(ChessBoard board, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i, int j) {
        if (i > 0 && j > 0) {
            addToList(board, originalPosition, moveList, color, i, j);
        }
    }

    private void topLeft(ChessBoard board, int r, int c, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i2, int i3) {
        int i;
        int j;
        i = r - i2;
        j = c + i3;
        bottomRight(board, originalPosition, moveList, color, i, j, i > 0, j < 9);
    }

    static void topRight(ChessBoard board, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i, int j) {
        if (i < 9 && j < 9) {
            addToList(board, originalPosition, moveList, color, i, j);
        }
    }
}
