package com.academicscode.trivia;

import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;

// Intentionally duplicates production GameRunner for now, to avoid changing
// production code that somebody might depend on.
public class GameRunnerGoldenMasterTest {

    private static boolean notAWinner;

    public static void main(String[] args) {
        Game aGame = new Game();

        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

        Random rand = new Random(42);

        do {

            aGame.roll(rand.nextInt(5) + 1);

            if (rand.nextInt(9) == 7) {
                notAWinner = aGame.wrongAnswer();
            } else {
                notAWinner = aGame.wasCorrectlyAnswered();
            }


        } while (notAWinner);

    }
}
