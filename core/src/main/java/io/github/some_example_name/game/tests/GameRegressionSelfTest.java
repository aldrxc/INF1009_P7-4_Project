package io.github.some_example_name.game.tests;

import io.github.some_example_name.game.config.GameBalanceConfig;
import io.github.some_example_name.game.util.CancerEvolutionManager;
import io.github.some_example_name.game.util.ChemoManager;
import io.github.some_example_name.game.util.RunStats;
import io.github.some_example_name.game.util.WaveManager;

public final class GameRegressionSelfTest {
    public static void main(String[] args) {
        testBalanceConfig();
        testWaveManager();
        testCancerEvolution();
        testChemoManager();
        testRunStats();
        System.out.println("All game regression checks passed.");
    }

    private static void testBalanceConfig() {
        require(GameBalanceConfig.getTCellAggressionForSpread(100f) == GameBalanceConfig.getTCellAggressionForSpread(75f),
                "Late-game T-cell aggression should flatten after the configured threshold.");
        require(GameBalanceConfig.getHealthyCellSpeed(4) > GameBalanceConfig.getHealthyCellSpeed(1),
                "Late-game healthy cells should move faster than early-game cells.");
    }

    private static void testWaveManager() {
        WaveManager waveManager = new WaveManager();
        require(waveManager.getTargetHealthyCellCount(1) == 28, "Stage 1 should maintain a larger healthy population.");
        require(waveManager.getTargetHealthyCellCount(4) == 16, "Stage 4 should still maintain a reduced but recoverable healthy population.");
        require(waveManager.getMaxActiveTCells(1) == 3, "Stage 1 should cap T-cells lower.");
        require(waveManager.getMaxActiveTCells(4) == 8, "Stage 4 should cap T-cells higher without overwhelming the player.");
        require(waveManager.getTCellSpawnInterval(4) < waveManager.getTCellSpawnInterval(1),
                "Late-stage T-cells should spawn faster.");
    }

    private static void testCancerEvolution() {
        CancerEvolutionManager evolution = new CancerEvolutionManager();
        evolution.addSpread(0.06f);
        require(evolution.getCurrentSpreadPercent() == 0.1f, "Spread changes should round to one decimal place.");
        evolution.addSpread(30f);
        require(evolution.checkEvolution(), "Spread reaching 30 should trigger stage 2.");
        require(evolution.getCurrentStage() == 2, "Stage should now be 2.");

        evolution.addSpread(50f);
        require(evolution.checkEvolution(), "Further spread should trigger another stage increase.");
        require(evolution.canEatTCells(), "Stage 4 should allow eating T-cells.");
        evolution.addSpread(10f);
        require(evolution.checkEvolution(), "Very high spread should trigger the terminal stage.");
        require(evolution.isTerminalStage(), "Terminal stage should unlock at extreme spread.");
    }

    private static void testChemoManager() {
        ChemoManager chemo = new ChemoManager();
        require(chemo.update(10f, 2, 40f) == 0f, "Chemo should do nothing before activation.");
        chemo.activate();
        float totalReduction = 0f;
        for (int i = 0; i < 30; i++) {
            totalReduction += chemo.update(1f, 4, 90f);
        }
        require(totalReduction > 0f, "Chemo should eventually reduce spread.");
        require(chemo.isActive(), "Chemo should remain active once started.");
        require(chemo.getTimeUntilNextChemo(4, 90f) <= chemo.getTimeUntilNextChemo(2, 40f),
                "Higher spread should not make chemo slower.");
        require(chemo.getTimeUntilNextChemo(4, 100f) == chemo.getTimeUntilNextChemo(4, 75f),
                "Chemo aggression should flatten once late-game spread is reached.");
    }

    private static void testRunStats() {
        RunStats.recordRun(1400, 22, 4, 88.6f, 97.4f, true);
        require(RunStats.getLastScore() == 1400, "RunStats should keep the last run score.");
        require(RunStats.getLastInfectedCells() == 22, "RunStats should store infected cell count.");
        require(RunStats.getLastLevel() == 4, "RunStats should store the player level reached.");
        require(RunStats.getLastSpreadPercent() == 88.6f, "RunStats should store final spread percent.");
        require(RunStats.wasLastRunWon(), "RunStats should remember the run outcome.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
