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
        KnightMovesCalculator.topRight(board, originalPosition, moveList, color, i, j);
        j = c - 1;
        if (i < 9 && j > 0) {
            KnightMovesCalculator.addToList(board, originalPosition, moveList, color, i, j);
        }
        j = c;
        if (i < 9) {
            KnightMovesCalculator.addToList(board, originalPosition, moveList, color, i, j);
        }
        i = r - 1;
        j = c + 1;
        KnightMovesCalculator.bottomRight(board, originalPosition, moveList, color, i, j, i > 0, j < 9);
        j = c - 1;
        KnightMovesCalculator.bottomLeft(board, originalPosition, moveList, color, i, j);
        j = c;
        if (i > 0) {
            KnightMovesCalculator.addToList(board, originalPosition, moveList, color, i, j);
        }
        i = r;
        j = c - 1;
        if (j > 0) {
            KnightMovesCalculator.addToList(board, originalPosition, moveList, color, i, j);
        }
        j = c + 1;
        if (j < 9) {
            KnightMovesCalculator.addToList(board, originalPosition, moveList, color, i, j);
        }
        return moveList;
    }
}
