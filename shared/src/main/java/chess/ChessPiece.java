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
            if (this.type == PieceType.ROOK) {
                return "r";
            }
            if (this.type == PieceType.KNIGHT) {
                return "n";
            }
            if (this.type == PieceType.BISHOP) {
                return "b";
            }
            if (this.type == PieceType.QUEEN) {
                return "q";
            }
            if (this.type == PieceType.KING) {
                return "k";
            }
            if (this.type == PieceType.PAWN) {
                return "p";
            }
        }
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            if (this.type == PieceType.ROOK) {
                return "R";
            }
            if (this.type == PieceType.KNIGHT) {
                return "N";
            }
            if (this.type == PieceType.BISHOP) {
                return "B";
            }
            if (this.type == PieceType.QUEEN) {
                return "Q";
            }
            if (this.type == PieceType.KING) {
                return "K";
            }
            if (this.type == PieceType.PAWN) {
                return "P";
            }
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
        return new ArrayList<>();
    }
}
