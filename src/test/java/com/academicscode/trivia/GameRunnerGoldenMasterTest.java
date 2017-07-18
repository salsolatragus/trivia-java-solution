package com.academicscode.trivia;

import java.io.*;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;

// Intentionally duplicates production GameRunner for now, to avoid changing
// production code that somebody might depend on.
public class GameRunnerGoldenMasterTest {

    private static boolean notAWinner;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("test-data", "test-run-42.txt");

        // REFACTOR Make the game output stream exchangeable, since we want to change it for testing.
        PrintStream originalSysOut = System.out;
        System.setOut(new PrintStream(file));

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

        System.setOut(originalSysOut);
    }
}
