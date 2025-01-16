package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public String toString() {
        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            return switch (this.type) {
                case PieceType.ROOK -> "r";
                case PieceType.KING -> "k";
                case PieceType.BISHOP -> "b";
                case PieceType.KNIGHT -> "n";
                case PieceType.PAWN -> "p";
                case PieceType.QUEEN -> "q";
            };
        }
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            return switch (this.type) {
                case PieceType.ROOK -> "R";
                case PieceType.KING -> "K";
                case PieceType.BISHOP -> "B";
                case PieceType.KNIGHT -> "N";
                case PieceType.PAWN -> "P";
                case PieceType.QUEEN -> "Q";
            };
        }
        return "How did you get here? Your piece does not have a color.";
    }
    public boolean equals(Object piece) {
        if (this == piece) {
            return true;
        }
        if (piece == null) {
            return false;
        }
        if (this.getClass() != piece.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) piece;
        return this.type == that.type && this.pieceColor == that.pieceColor;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece newPiece = new ChessPiece(board.getPiece(myPosition).pieceColor, board.getPiece(myPosition).type);
        return switch (newPiece.type) {
            case KING -> {
                KingMovesCalculator kingCalculator = new KingMovesCalculator();
                yield kingCalculator.pieceMoves(board, myPosition);
            }
            case QUEEN -> {
                QueenMovesCalculator queenCalculator = new QueenMovesCalculator();
                yield queenCalculator.pieceMoves(board, myPosition);
            }
            case ROOK -> {
                RookMovesCalculator rookCalculator = new RookMovesCalculator();
                yield rookCalculator.pieceMoves(board, myPosition);
            }
            case KNIGHT -> {
                KnightMovesCalculator knightCalculator = new KnightMovesCalculator();
                yield knightCalculator.pieceMoves(board, myPosition);
            }
            case BISHOP -> {
                BishopMovesCalculator bishopCalculator = new BishopMovesCalculator();
                yield bishopCalculator.pieceMoves(board, myPosition);
            }
            case PAWN -> {
                PawnMovesCalculator pawnCalculator = new PawnMovesCalculator();
                yield pawnCalculator.pieceMoves(board, myPosition);
            }
        };
    }
}
