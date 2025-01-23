package chess;

import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        RookMovesCalculator rookCalculator = new RookMovesCalculator();
        Collection<ChessMove> moveList = new ArrayList<>(rookCalculator.pieceMoves(board, myPosition));
        BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
        moveList.addAll(bishopCalculator.pieceMoves(board, myPosition));
        return moveList;
    }
}
