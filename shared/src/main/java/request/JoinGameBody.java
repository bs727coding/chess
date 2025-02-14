package request;

import chess.ChessGame;

public record JoinGameBody(ChessGame.TeamColor teamColor, int gameID) {
}
