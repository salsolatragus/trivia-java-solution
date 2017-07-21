package com.academicscode.trivia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Intentionally duplicates production GameRunner for now, to avoid changing
// production code that somebody might depend on.
public class GameRunnerGoldenMasterTest {
    private static final File testDataWorkspace = new File("test-data");
    private static final File testRunDirectory = new File(testDataWorkspace, "test-run");
    private static final File goldenMasterDirectory = new File(testDataWorkspace, "golden-master");

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

    @Test
    public void verifyGoldenMasterTestRun() throws Exception {
        clearDirectory(testRunDirectory);
        
        main(null);

        List<String> goldenMasterFiles = getFileNames(goldenMasterDirectory);
        List<String> testRunFiles = getFileNames(testRunDirectory);
        assertEquals(
                "Expect Golden Master and test run to contain the same game runs,",
                goldenMasterFiles,
                testRunFiles);

        for (String gameRunFileName : goldenMasterFiles) {
            String goldenMasterLog = getFileContentAsString(new File(goldenMasterDirectory, gameRunFileName));
            String testRunLog = getFileContentAsString(new File(testRunDirectory, gameRunFileName));
            assertEquals(
                    "Expect test run to match Golden Master for '" + gameRunFileName + "'.",
                    goldenMasterLog,
                    testRunLog);
        }
    }

    private void clearDirectory(File directory) {
        if (directory.exists()) {
            for (String fileName : getFileNames(directory)) {
                assertTrue("Clear game run '" + fileName + "'.", new File(directory, fileName).delete());
            }
            assertTrue("Clear Golden Master test run.", directory.delete());
        }
        assertTrue("Create test-run directory.", directory.mkdir());
    }

    private List<String> getFileNames(File directory) {
        List<String> fileNames = new ArrayList<String>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }

    private String getFileContentAsString(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } finally {
            reader.close();
        }
    }
}
