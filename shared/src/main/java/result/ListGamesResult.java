package result;

import model.GameInformation;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameInformation> games) {
}
