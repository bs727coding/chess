package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object cp) {
        if (this == cp) {
            return true;
        }
        if (cp == null) {
            return false;
        }
        if (this.getClass() != cp.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) cp;
        return this.row == that.row && this.col == that.col;
    }

    public int hashCode() {
        return 93 + row + col + (row * col);
    }

    public String toString() {
        return "(" + row + "," + col + ")";
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}
