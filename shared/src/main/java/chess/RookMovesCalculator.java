package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int r = myPosition.getRow();
        final int c = myPosition.getColumn();
        final ChessPosition originalPosition = new ChessPosition(r, c);
        Collection<ChessMove> moveList = new ArrayList<>();
        final ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i, j;
        for (i = r + 1, j = c; i < 9; i++) {
            if (BishopMovesCalculator.addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r - 1, j = c; i > 0; i--) {
            if (BishopMovesCalculator.addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r, j = c + 1; j < 9; j++) {
            if (BishopMovesCalculator.addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        for (i = r, j = c - 1; j > 0; j--) {
            if (BishopMovesCalculator.addMove(board, originalPosition, moveList, color, i, j)) {
                break;
            }
        }
        return moveList;
    }
}
