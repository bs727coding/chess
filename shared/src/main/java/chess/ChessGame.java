package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        turn = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = new ChessPiece(board.getPiece(startPosition).getTeamColor(),
                board.getPiece(startPosition).getPieceType());
        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
        ChessPiece originalPieceEnd;
        //if in check, remove moves that do not take the king out of check
        //if not in check, only remove moves that put the king in check
        for (ChessMove move : validMoves) {
            originalPieceEnd = null;
            if (board.getPiece(move.getEndPosition()) != null) {
                originalPieceEnd = new ChessPiece(board.getPiece(move.getEndPosition()).getTeamColor(), board.getPiece(move.getEndPosition()).getPieceType());
            }
            actuallyMakeMove(move);
            if (isInCheck(piece.getTeamColor())) {
                validMoves.remove(move);
            }
            //reset to the way it was
            if (originalPieceEnd != null) {
                board.addPiece(move.getEndPosition(), originalPieceEnd);
            }
            board.addPiece(startPosition, piece);
        }
        return validMoves;
    }

    public void actuallyMakeMove(ChessMove move) {
        ChessPiece piece = new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                board.getPiece(move.getStartPosition()).getPieceType());
        board.clearPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Error: invalid move chosen for start position: " +
                    move.getStartPosition().toString() + ", end position: " + move.getEndPosition().toString());
        } else {
            actuallyMakeMove(move);
        }
    }

    private ChessPosition findKing(TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == color &&
                        board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                    return position;
                }
            }
        }
        return null;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //throw new RuntimeException("Not implemented");
        ChessPosition kingPosition = findKing(teamColor);//find king of corresponding color
        TeamColor opposingColor;
        if (teamColor == TeamColor.WHITE) {
            opposingColor = TeamColor.BLACK;
        } else {
            opposingColor = TeamColor.WHITE;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition opposingPosition = new ChessPosition(i, j);
                if (board.getPiece(opposingPosition) != null &&
                        board.getPiece(opposingPosition).getTeamColor() == opposingColor) {

                }
            }
        }
        return false;
        //check valid moves for opposing color for moves that end with the king's position
        //if such a move exists, return true. Else return false
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if (board.getPiece(position) != null &&
                            board.getPiece(position).getTeamColor() == teamColor) {
                        if (!validMoves(position).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if (board.getPiece(position) != null &&
                            board.getPiece(position).getTeamColor() == teamColor) {
                        if (!validMoves(position).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
