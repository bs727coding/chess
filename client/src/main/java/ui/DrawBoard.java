package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private final ChessBoard board;
    private boolean boardColor;  //false is dark

    public DrawBoard(ChessBoard board) {
        this.board = board;
        boardColor = false;
    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        DrawBoard drawBoard = new DrawBoard(board);
        drawBoard.drawBoard(out, ChessGame.TeamColor.WHITE);
        out.println();
        drawBoard.drawBoard(out, ChessGame.TeamColor.BLACK);
        out.println();
        drawBoard.drawBoard(out, null); //test observer and make sure it shows white's perspective
    }

    public void drawBoard(PrintStream out, ChessGame.TeamColor color) {
        if (color == null || color == ChessGame.TeamColor.WHITE) {
            drawBoardWhite(out);
        } else {
            drawBoardBlack(out);
        }
        out.print(RESET_BG_COLOR);
    }

    public void highlight(PrintStream out, Collection<ChessMove> moves, ChessGame.TeamColor color) {
        if (color == null || color == ChessGame.TeamColor.WHITE) {
            drawWhiteHeader(out);
            for (int i = 8; i > 0; i--) {
                drawWhiteRowHighlightedGivenNumber(out, i, moves);
            }
            drawWhiteHeader(out);
        } else {
            drawBlackHeader(out);
            for (int i = 1; i < 9; i++) {
                drawBlackRowHighlightedGivenNumber(out, i, moves);
            }
            drawWhiteHeader(out);
        }
        out.print(RESET_BG_COLOR);
    }

    private void drawBoardWhite(PrintStream out) {
        drawWhiteHeader(out);
        for (int i = 8; i > 0; i--) {
            drawWhiteRowGivenNumber(out, i);
        }
        drawWhiteHeader(out);
    }

    private void drawBoardBlack(PrintStream out) {
        drawBlackHeader(out);
        for (int i = 1; i < 9; i++) {
            drawBlackRowGivenNumber(out, i);
        }
        drawBlackHeader(out);
    }

    private void drawWhiteRowGivenNumber(PrintStream out, int row) {
        //even rows start with white square
        drawNumber(out, row);
        for (int i = 1; i < 9; i++) {
            drawInnerRow(out, row, i);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawWhiteRowHighlightedGivenNumber(PrintStream out, int row, Collection<ChessMove> moves) {
        //even rows start with white square
        drawNumber(out, row);
        for (int i = 1; i < 9; i++) {
            drawInnerHighlightedRow(out, row, i, moves);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawBlackRowHighlightedGivenNumber(PrintStream out, int row, Collection<ChessMove> moves) {
        //odd rows start with white square
        drawNumber(out, row);
        for (int i = 8; i > 0; i--) {
            drawInnerHighlightedRow(out, row, i, moves);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawBlackRowGivenNumber(PrintStream out, int row) {
        //odd rows start with white square
        drawNumber(out, row);
        for (int i = 8; i > 0; i--) {
            drawInnerRow(out, row, i);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawInnerHighlightedRow(PrintStream out, int row, int i, Collection<ChessMove> moves) {
        boolean highlighted = false;
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(new ChessPosition(row, i))) {
                out.print(SET_BG_COLOR_YELLOW);
                highlighted = true;
                findChessPiece(out, row, i, true);
            }
        }
        if (!highlighted) {
            drawInnerRow(out, row, i);
        }
    }

    private void drawInnerRow(PrintStream out, int row, int i) {
        if ((i + row) % 2 == 0) { //even column
            out.print(SET_BG_COLOR_DARK_GREY);
            boardColor = false;
        } else {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            boardColor = true;
        }
        findChessPiece(out, row, i, false);
    }

    private static void drawWhiteHeader(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(EMPTY);
        out.print(" " + 'a' + "  " + 'b' + SPACE + " " + 'c' + "   " + 'd' + "  " + 'e' + SPACE + " " + 'f' +
                "   " + 'g' + "  " + 'h' + " ");
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawBlackHeader(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(EMPTY);
        out.print(" " + 'h' + "  " + 'g' + SPACE + " " + 'f' + "   " + 'e' + "  " + 'd' + SPACE + " " + 'c' +
                "   " + 'b' + "  " + 'a' + " ");
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void findChessPiece(PrintStream out, int row, int col, boolean highlighted) {
        //locate chessPiece at given coordinate and pass it into drawChessPiece
        drawChessPiece(out, board.getPiece(new ChessPosition(row, col)), highlighted);
    }

    private void drawChessPiece(PrintStream out, ChessPiece piece, boolean highlighted) {
        if (highlighted && piece == null) {
            out.print(SET_TEXT_COLOR_YELLOW);
            out.print(BLACK_KNIGHT);
        } else if (piece == null) {
            if (boardColor) {
                out.print(SET_TEXT_COLOR_LIGHT_GREY);
            } else {
                out.print(SET_TEXT_COLOR_DARK_GREY);
            }
            out.print(BLACK_KNIGHT);
        } else {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_GREEN);
            } else { //Black
                out.print(SET_TEXT_COLOR_BLUE);
            }
            switch (piece.getPieceType()) {
                case PAWN -> out.print(BLACK_PAWN);
                case KING -> out.print(BLACK_KING);
                case QUEEN -> out.print(BLACK_QUEEN);
                case BISHOP -> out.print(BLACK_BISHOP);
                case ROOK -> out.print(BLACK_ROOK);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                default -> out.print(EMPTY);
            }
        }
    }

    private static void drawNumber(PrintStream out, int number) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + number + " ");
    }
}
