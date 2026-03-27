package io.github.some_example_name.game.util;

public final class RunStats {
    private static int lastScore;
    private static int lastInfectedCells;
    private static int lastLevel;
    private static float lastSurvivalSeconds;
    private static float lastSpreadPercent;
    private static boolean lastRunWon;
    private static int bestScore;

    private RunStats() {}

    public static void recordRun(int score, int infectedCells, int level, float spreadPercent, float survivalSeconds, boolean won) {
        lastScore = score;
        lastInfectedCells = infectedCells;
        lastLevel = level;
        lastSpreadPercent = spreadPercent;
        lastSurvivalSeconds = survivalSeconds;
        lastRunWon = won;
        if (score > bestScore) bestScore = score;
    }

    public static int getLastScore() { return lastScore; }
    public static int getLastInfectedCells() { return lastInfectedCells; }
    public static int getLastLevel() { return lastLevel; }
    public static float getLastSurvivalSeconds() { return lastSurvivalSeconds; }
    public static float getLastSpreadPercent() { return lastSpreadPercent; }
    public static boolean wasLastRunWon() { return lastRunWon; }
    public static int getBestScore() { return bestScore; }
}
