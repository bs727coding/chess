package chess;

import java.util.ArrayList;
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
    private ChessMove lastMove;

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
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> invalidMoves = new ArrayList<>();
        ChessPiece originalPieceEnd;
        //if in check, remove moves that do not take the king out of check
        //if not in check, only remove moves that put the king in check
        for (ChessMove move : validMoves) {
            originalPieceEnd = null;
            if (board.getPiece(move.getEndPosition()) != null) {
                originalPieceEnd = new ChessPiece(board.getPiece(move.getEndPosition()).getTeamColor(),
                        board.getPiece(move.getEndPosition()).getPieceType());
            }
            try {
                actuallyMakeMove(move);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            if (isInCheck(piece.getTeamColor())) {
                invalidMoves.add(move);
            }
            if (originalPieceEnd != null) {
                board.addPiece(move.getEndPosition(), originalPieceEnd);
            } else {
                board.clearPiece(move.getEndPosition());
            }
            board.addPiece(startPosition, piece);
        }
        for (ChessMove invalidMove : invalidMoves) {
            validMoves.remove(invalidMove);
        }
        castle(startPosition, piece, validMoves);
        validMoves.addAll(enPassantAvailability(startPosition, piece));
        Collection<ChessMove> nowInvalidMoves = new ArrayList<>();
        for (ChessMove enPassantMove : validMoves) {
            if (isEnPassantMove(enPassantMove) && !board.getPiece(lastMove.getEndPosition()).justDoubleMoved()) {
                nowInvalidMoves.add(enPassantMove);
            }
        }
        validMoves.removeAll(nowInvalidMoves);
        return validMoves;
    }

    private Collection<ChessMove> enPassantAvailability(ChessPosition startPosition, ChessPiece piece) {
        Collection<ChessMove> moveList = new ArrayList<>();
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return moveList;
        } else if (piece.getTeamColor() == TeamColor.WHITE && startPosition.getRow() == 5) {
            if (startPosition.getColumn() == 1) {
                if (board.getPiece(new ChessPosition(5, 2)) != null) {
                    ChessPiece oppositePiece = board.getPiece(new ChessPosition(5, 2));
                    if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                            TeamColor.BLACK && oppositePiece.justDoubleMoved()) {
                        moveList.add(new ChessMove(startPosition, new ChessPosition(6, 2), null));
                        return moveList;
                    }
                }
            } else if (startPosition.getColumn() == 8) {
                if (board.getPiece(new ChessPosition(5, 7)) != null) {
                    ChessPiece oppositePiece = board.getPiece(new ChessPosition(5, 7));
                    if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                            TeamColor.BLACK && oppositePiece.justDoubleMoved()) {
                        moveList.add(new ChessMove(startPosition, new ChessPosition(6, 7),
                                null));
                        return moveList;
                    }
                }
            } else { //2-7
                for (int i = 2; i < 8; i ++) {
                    if (board.getPiece(new ChessPosition(5, i + 1)) != null) {
                        ChessPiece oppositePiece = board.getPiece(new ChessPosition(5, i + 1));
                        if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                                TeamColor.BLACK && oppositePiece.justDoubleMoved()) {
                            moveList.add(new ChessMove(startPosition, new ChessPosition(6, i + 1),
                                    null));
                        }
                    }
                    if (board.getPiece(new ChessPosition(5, i - 1)) != null) {
                        ChessPiece oppositePiece = board.getPiece(new ChessPosition(5, i - 1));
                        if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                                TeamColor.BLACK && oppositePiece.justDoubleMoved()) {
                            moveList.add(new ChessMove(startPosition, new ChessPosition(6, i - 1),
                                    null));
                        }
                    }
                }
                return moveList;
            }
        } else if (startPosition.getRow() == 4) { //Black
            if (startPosition.getColumn() == 1) {
                if (board.getPiece(new ChessPosition(4, 2)) != null) {
                    ChessPiece oppositePiece = board.getPiece(new ChessPosition(4, 2));
                    if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                            TeamColor.WHITE && oppositePiece.justDoubleMoved()) {
                        moveList.add(new ChessMove(startPosition, new ChessPosition(3, 2),
                                null));
                        return moveList;
                    }
                }
            } else if (startPosition.getColumn() == 8) {
                if (board.getPiece(new ChessPosition(4, 7)) != null) {
                    ChessPiece oppositePiece = board.getPiece(new ChessPosition(4, 7));
                    if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                            TeamColor.WHITE && oppositePiece.justDoubleMoved()) {
                        moveList.add(new ChessMove(startPosition, new ChessPosition(3, 7),
                                null));
                        return moveList;
                    }
                }
            } else { //2-7
                for (int i = 2; i < 8; i++) {
                    if (board.getPiece(new ChessPosition(4, i + 1)) != null) {
                        ChessPiece oppositePiece = board.getPiece(new ChessPosition(4, i + 1));
                        if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                                TeamColor.WHITE && oppositePiece.justDoubleMoved()) {
                            moveList.add(new ChessMove(startPosition, new ChessPosition(3, i + 1),
                                    null));
                        }
                    }
                    if (board.getPiece(new ChessPosition(4, i - 1)) != null) {
                        ChessPiece oppositePiece = board.getPiece(new ChessPosition(4, i - 1));
                        if (oppositePiece.getPieceType() == ChessPiece.PieceType.PAWN && oppositePiece.getTeamColor() ==
                                TeamColor.WHITE && oppositePiece.justDoubleMoved()) {
                            moveList.add(new ChessMove(startPosition, new ChessPosition(3, i - 1),
                                    null));
                        }
                    }
                }
                return moveList;
            }
        }
        return moveList;
    }

    private void castle(ChessPosition startPosition, ChessPiece piece, Collection<ChessMove> validMoves) {
        if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.hasNotMoved()) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                castleWhite(startPosition, validMoves);
            } else { //black
                castleBlack(startPosition, validMoves);
            }
        }
    }

    private void castleBlack(ChessPosition startPosition, Collection<ChessMove> validMoves) {
        if (board.getPiece(new ChessPosition(8, 1)) != null) { //queenSide
            ChessPiece rookPiece = board.getPiece(new ChessPosition(8, 1));
            if (rookPiece.hasNotMoved()) {
                if (board.getPiece(new ChessPosition(8, 2)) == null && board.getPiece(new ChessPosition
                        (8, 3)) == null && board.getPiece(new ChessPosition(8, 4)) == null) {
                    boolean moveWorks = true;
                    for (int i = 4; i > 1; i--) {
                        moveWorks = isMoveWorksBlack(moveWorks, i);
                    }
                    if (moveWorks) {
                        validMoves.add(new ChessMove(startPosition, new ChessPosition(8, 3),
                                null));
                    }
                }
            }
        }
        if (board.getPiece(new ChessPosition(8, 8)) != null) { //kingSide
            ChessPiece rookPiece2 = board.getPiece(new ChessPosition(8, 8));
            if (rookPiece2.hasNotMoved()) {
                if (board.getPiece(new ChessPosition(8, 6)) == null && board.getPiece(
                        new ChessPosition(8, 7)) == null) {
                    boolean moveWorks = true;
                    for (int i = 6; i < 8; i++) {
                        moveWorks = isMoveWorksBlack(moveWorks, i);
                    }
                    if (moveWorks) {
                        validMoves.add(new ChessMove(startPosition, new ChessPosition(8, 7),
                                null));
                    }
                }
            }
        }
    }

    private void castleWhite(ChessPosition startPosition, Collection<ChessMove> validMoves) {
        if (board.getPiece(new ChessPosition(1, 1)) != null) {
            ChessPiece rookPiece = board.getPiece(new ChessPosition(1, 1));
            if (rookPiece.hasNotMoved()) {
                if (board.getPiece(new ChessPosition(1, 2)) == null && board.getPiece(new ChessPosition
                        (1, 3)) == null && board.getPiece(new ChessPosition(1, 4)) == null) {
                    boolean moveWorks = true;
                    for (int i = 4; i > 1; i--) {
                        moveWorks = isMoveWorksWhite(moveWorks, i);
                    }
                    if (moveWorks) {
                        validMoves.add(new ChessMove(startPosition, new ChessPosition(1, 3),
                                null));
                        validMoves.add(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 4)
                                , null));
                    }
                }
            }

        }
        if (board.getPiece(new ChessPosition(1, 8)) != null) {
            ChessPiece rookPiece2 = board.getPiece(new ChessPosition(1, 8));
            if (rookPiece2.hasNotMoved()) {
                if (board.getPiece(new ChessPosition(1, 6)) == null && board.getPiece
                        (new ChessPosition(1, 7)) == null) {
                    boolean moveWorks = true;
                    for (int i = 6; i < 8; i++) {
                        moveWorks = isMoveWorksWhite(moveWorks, i);
                    }
                    if (moveWorks) {
                        validMoves.add(new ChessMove(startPosition, new ChessPosition(1, 7),
                                null));
                        validMoves.add(new ChessMove(new ChessPosition(1, 8), new ChessPosition(1, 6)
                                , null));
                    }
                }
            }
        }
    }

    private boolean isMoveWorksBlack(boolean moveWorks, int i) {
        ChessMove move = new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, i),
                null);
        try {
            actuallyMakeMove(move);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        if (isInCheck(TeamColor.BLACK)) {
            moveWorks = false;
        }
        try {
            actuallyMakeMove(new ChessMove(new ChessPosition(8, i), new ChessPosition(8, 5),
                    null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        return moveWorks;
    }

    private boolean isMoveWorksWhite(boolean moveWorks, int i) {
        ChessMove move = new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, i),
                null);
        try {
            actuallyMakeMove(move);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        if (isInCheck(TeamColor.WHITE)) {
            moveWorks = false;
        }
        try {
            actuallyMakeMove(new ChessMove(new ChessPosition(1, i), new ChessPosition(1, 5),
                    null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        return moveWorks;
    }

    public void actuallyMakeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            switch (move.getPromotionPiece()) {
                case KNIGHT -> board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),
                        ChessPiece.PieceType.KNIGHT));
                case QUEEN -> board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),
                        ChessPiece.PieceType.QUEEN));
                case ROOK -> board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),
                        ChessPiece.PieceType.ROOK));
                case BISHOP -> board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),
                        ChessPiece.PieceType.BISHOP));
                case null -> board.addPiece(move.getEndPosition(), piece);
                default -> throw new InvalidMoveException("Error: Pawn was attempted to be promoted to an " +
                        "invalid piece type.");
            }
        } else {
            board.addPiece(move.getEndPosition(), piece);
        }
        board.clearPiece(move.getStartPosition());
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Error: no piece at specified starting position.");
        } else if (board.getPiece(move.getStartPosition()).getTeamColor() != turn) {
            throw new InvalidMoveException("Error: move taken out of turn.");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Error: invalid move chosen for start position: " +
                    move.getStartPosition().toString() + ", end position: " + move.getEndPosition().toString());
        } else {
            try { //castling
                if (move.equals(new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 3),
                        null)))  {
                    actuallyMakeMove(new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 4),
                            null));
                } else if (move.equals(new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 7),
                        null)))  {
                    actuallyMakeMove(new ChessMove(new ChessPosition(1, 8), new ChessPosition(1, 6),
                            null));
                } else if (move.equals(new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 3),
                        null)))  {
                    actuallyMakeMove(new ChessMove(new ChessPosition(8, 1), new ChessPosition(8, 4),
                            null));
                } else if (move.equals(new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 7),
                        null)))  {
                    actuallyMakeMove(new ChessMove(new ChessPosition(8, 8), new ChessPosition(8, 6),
                            null));
                }
                if (isDoubleMove(move)) {
                    board.getPiece(move.getStartPosition()).setJustDoubleMoved(true);
                }
                if (isEnPassantMove(move)) {
                    if (turn == TeamColor.WHITE) {
                        board.clearPiece(new ChessPosition(move.getEndPosition().getRow() - 1,
                                move.getEndPosition().getColumn()));
                    } else {
                        board.clearPiece(new ChessPosition(move.getEndPosition().getRow() + 1,
                                move.getEndPosition().getColumn()));
                    }
                }
                actuallyMakeMove(move);
                board.getPiece(move.getEndPosition()).setHasMoved();
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            if (turn == TeamColor.WHITE) {
                turn = TeamColor.BLACK;
            } else {
                turn = TeamColor.WHITE;
            }
            if (lastMove != null && isDoubleMove(lastMove)) {
                board.getPiece(move.getEndPosition()).setJustDoubleMoved(false);
            }
            lastMove = move;
        }
    }

    private boolean isDoubleMove(ChessMove move) {
        if (board.getPiece(move.getStartPosition()) != null && board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            return Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) == 2;
        }
        return false;
    }

    private boolean isEnPassantMove(ChessMove move) {
        if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow()) == 1 &&
                    Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) == 1) {
                return board.getPiece(move.getEndPosition()) == null;
            }
        }
        return false;
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
        ChessPosition kingPosition = findKing(teamColor); //find king of corresponding color
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
                    ChessPiece opposingPiece = board.getPiece(opposingPosition);
                    Collection<ChessMove> opposingMoves = opposingPiece.pieceMoves(board, opposingPosition);
                    if (opposingMoves.contains(new ChessMove(opposingPosition, kingPosition, null)) ||
                        opposingMoves.contains(new ChessMove(opposingPosition, kingPosition,
                                ChessPiece.PieceType.QUEEN)) ||
                        opposingMoves.contains(new ChessMove(opposingPosition, kingPosition,
                                ChessPiece.PieceType.KNIGHT)) ||
                        opposingMoves.contains(new ChessMove(opposingPosition, kingPosition,
                                ChessPiece.PieceType.BISHOP)) ||
                        opposingMoves.contains(new ChessMove(opposingPosition, kingPosition,
                                ChessPiece.PieceType.ROOK))) {
                        return true;
                    }
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
            return hasAvailableMoves(teamColor);
        } else {
            return false;
        }
    }

    private boolean hasAvailableMoves(TeamColor teamColor) {
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
            return hasAvailableMoves(teamColor);
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
