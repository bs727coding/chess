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
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int i, j;
        for (i = r + 1, j = c + 1; i < 8 && j < 8; i++, j++) {
            myPosition.setPosition(i, j);
            if (board.getPiece(myPosition).getTeamColor() == color) {
                break;
            } else if (board.getPiece(myPosition).getTeamColor() != null) {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
                break;
            } else {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
            }
        }
        for (i = r + 1, j = c - 1; i < 8 && j >= 0; i++, j--) {
            myPosition.setPosition(i,j);
            if (board.getPiece(myPosition).getTeamColor() == color) {
                break;
            } else if (board.getPiece(myPosition).getTeamColor() != null) {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
                break;
            } else {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
            }
        }
        for (i = r - 1, j = c + 1; i >= 0 && j < 8; i--, j++) {
            myPosition.setPosition(i,j);
            if (board.getPiece(myPosition).getTeamColor() == color) {
                break;
            } else if (board.getPiece(myPosition).getTeamColor() != null) {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
                break;
            } else {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
            }
        }
        for (i = r - 1, j = c - 1; i >= 0 && j >= 0; i--, j--) {
            myPosition.setPosition(i,j);
            if (board.getPiece(myPosition).getTeamColor() == color) {
                break;
            } else if (board.getPiece(myPosition).getTeamColor() != null) {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
                break;
            } else {
                moveList.add(new ChessMove(originalPosition, myPosition, null));
            }
        }
        return moveList;
    }
}
