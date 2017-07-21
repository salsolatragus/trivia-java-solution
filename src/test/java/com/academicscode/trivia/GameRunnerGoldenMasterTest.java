package com.academicscode.trivia;

import java.io.*;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;

// Intentionally duplicates production GameRunner for now, to avoid changing
// production code that somebody might depend on.
public class GameRunnerGoldenMasterTest {
    private static final File testDataWorkspace = new File("test-data");
    private static final File testRunDirectory = new File(testDataWorkspace, "test-run");

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream originalSysOut = System.out;
        
        int numberOfTestRuns = 1000;

        int initialGameId = 42;
        int arbitraryGameIdOffset = 13;

        for (int i = 0; i < numberOfTestRuns; i++) {
            int gameId = initialGameId + i * arbitraryGameIdOffset;
            File file = new File(testRunDirectory, String.format("game-%d.txt", gameId));

            // REFACTOR Make the game output stream exchangeable, since we want to change it for testing.
            System.setOut(new PrintStream(file));

            Game aGame = new Game();

            aGame.add("Chet");
            aGame.add("Pat");
            aGame.add("Sue");

            // We can conveniently use the game Id as the random number generator seed.
            Random rand = new Random(gameId);

            boolean notAWinner;
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
