package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private final ChessBoard board;

    public DrawBoard(ChessBoard board) {
        this.board = board;
    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        DrawBoard drawBoard = new DrawBoard(board);
        drawBoard.drawBoard(out, ChessGame.TeamColor.WHITE);
        out.println();
        //drawBoard(out, ChessGame.TeamColor.Black);
        //out.println();
        //drawBoard(out, null); //test observer and make sure it shows white's perspective
    }

    public void drawBoard(PrintStream out, ChessGame.TeamColor color) {
        if (Objects.requireNonNull(color) == ChessGame.TeamColor.BLACK) {
            drawBoardBlack(out);
        } else {
            drawBoardWhite(out);
        }
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
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        for (int i = 1; i < 9; i++) {
            if (i % 2 == 0) { //even column
                out.print(SET_BG_COLOR_WHITE);
            } else {
                out.print(SET_BG_COLOR_BLACK);
            }
            findChessPiece(out, row, i);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.println();
    }

    private void drawBlackRowGivenNumber(PrintStream out, int row) {
        //odd rows start with white square
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        for (int i = 9; i > 0; i--) {
            if (i % 2 == 0) { //even column
                out.print(SET_BG_COLOR_BLACK);
            } else {
                out.print(SET_BG_COLOR_WHITE);
            }
            findChessPiece(out, row, i);
        }
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        drawNumber(out, row);
        out.println();
    }

    private static void drawWhiteHeader(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(EMPTY);
        char letter = 'a';
        for (int i = 1; i < 9; i++) {
            out.print(" " + letter + " ");
            letter++;
        }
        out.print(EMPTY);
        out.println();
    }

    private static void drawBlackHeader(PrintStream out) {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(EMPTY);
        char letter = 'h';
        for (int i = 1; i < 9; i++) {
            out.print(" " + letter + " ");
            letter--;
        }
        out.print(EMPTY);
        out.println();
    }

    private void findChessPiece(PrintStream out, int row, int col) {
        //locate chessPiece at given coordinate and pass it into drawChessPiece
        drawChessPiece(out, board.getPiece(new ChessPosition(row, col)));
    }

    private static void drawChessPiece(PrintStream out, ChessPiece piece) {
        if (piece == null) {
            out.print(EMPTY);
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
            out.print(" ");
            switch (piece.getPieceType()) {
                case PAWN -> out.print(WHITE_PAWN);
                case KING -> out.print(WHITE_KING);
                case QUEEN -> out.print(WHITE_QUEEN);
                case BISHOP -> out.print(WHITE_BISHOP);
                case ROOK -> out.print(WHITE_ROOK);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                default -> out.print(EMPTY);
            }
        } else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) { //Black
            out.print(SET_TEXT_COLOR_DARK_GREY);
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
        out.print(" ");
    }

    private static void drawNumber(PrintStream out, int number) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + number + " ");
    }
}
