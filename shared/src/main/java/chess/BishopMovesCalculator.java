package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int r = myPosition.getRow();
        final int c = myPosition.getColumn();
        final ChessPosition originalPosition = new ChessPosition(r, c);
        Collection<ChessMove> moveList = new ArrayList<>();
        final ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i, j;
        for (i = r + 1, j = c + 1; i < 9 && j < 9; i++, j++) {
            if (addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r + 1, j = c - 1; i < 9 && j > 0; i++, j--) {
            if (addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r - 1, j = c + 1; i > 0 && j < 9; i--, j++) {
            if (addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r - 1, j = c - 1; i > 0 && j > 0; i--, j--) {
            if (addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        return moveList;
    }

    static boolean addMove(ChessBoard board, ChessPosition originalPosition, Collection<ChessMove> moveList, ChessGame.TeamColor color, int i, int j) {
        ChessPosition newPosition = new ChessPosition(i, j);
        if (board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() != color) {
                moveList.add(new ChessMove(originalPosition, newPosition, null));
            }
            return true;
        } else {
            moveList.add(new ChessMove(originalPosition, newPosition, null));
        }
        return false;
    }
}
