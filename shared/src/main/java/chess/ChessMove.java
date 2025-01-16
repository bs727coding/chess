package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public String toString() {
        String move = this.startPosition + ", " + this.endPosition;
        if (this.promotionPiece != null) {
            move += ". Promotion piece: " + this.promotionPiece + ".";
        }
        return move;
    }

    public boolean equals(Object move) {
        if (this == move) {
            return true;
        }
        if (move == null) {
            return false;
        }
        if (this.getClass() != move.getClass()) {
            return false;
        }
        ChessMove that = (ChessMove) move;
        if (this.promotionPiece == null && that.promotionPiece == null) {
            return this.startPosition.equals(that.startPosition) && this.endPosition.equals(that.endPosition);
        }
        else if (this.promotionPiece == null ^ that.promotionPiece == null) {
            return false;
        } else {
            return this.startPosition.equals(that.startPosition) && this.endPosition.equals(that.endPosition)
                    && this.promotionPiece.equals(that.promotionPiece);
        }
    }

    public int hashCode() {
        int value = (this.startPosition.getRow() * 20) + (this.endPosition.getColumn() * 2) + (this.startPosition.getColumn() * 34) + (this.endPosition.getRow() * 23);
        if (this.promotionPiece != null) {
            value += this.promotionPiece.hashCode();
        }
        return value;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }
}
