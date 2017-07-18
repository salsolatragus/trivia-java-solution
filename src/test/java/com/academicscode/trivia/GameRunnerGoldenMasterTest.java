package com.academicscode.trivia;

import java.io.*;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;

// Intentionally duplicates production GameRunner for now, to avoid changing
// production code that somebody might depend on.
public class GameRunnerGoldenMasterTest {

    private static boolean notAWinner;

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream originalSysOut = System.out;

        int gameId = 42;

        for (int i = 0; i < 1; i++) {
            File file = new File("test-data", String.format("test-run-%d.txt", gameId));

            // REFACTOR Make the game output stream exchangeable, since we want to change it for testing.
            System.setOut(new PrintStream(file));

            Game aGame = new Game();

            aGame.add("Chet");
            aGame.add("Pat");
            aGame.add("Sue");

            // We can conveniently use the game Id as the random number generator seed.
            Random rand = new Random(gameId);

            do {

                aGame.roll(rand.nextInt(5) + 1);

                if (rand.nextInt(9) == 7) {
                    notAWinner = aGame.wrongAnswer();
                } else {
                    notAWinner = aGame.wasCorrectlyAnswered();
                }


            } while (notAWinner);
        }

        System.setOut(originalSysOut);
    }
}
