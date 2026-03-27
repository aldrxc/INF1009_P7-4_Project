package io.github.some_example_name.game.util;

/**
 * Stage-driven population pacing for the game loop.
 * Keeps healthy-cell density and T-cell pressure aligned with the current
 * infection stage instead of using one fixed wave size for the whole run.
 */
public class WaveManager {

    private static final float[] TCELL_SPAWN_INTERVALS = { 10f, 7f, 5f, 4f };
    private static final int[] HEALTHY_CELL_TARGETS = { 28, 24, 20, 16 };
    private static final int[] MAX_ACTIVE_TCELLS = { 3, 5, 7, 8 };

    private int lastTriggeredStage = 0;
    private float spawnTimer = 0f;

    public boolean shouldSpawnTCell(float delta, int currentStage) {
        spawnTimer += delta;
        float interval = getTCellSpawnInterval(currentStage);
        if (spawnTimer >= interval) {
            spawnTimer = 0f;
            return true;
        }
        return false;
    }

    public boolean isNewStageThreshold(int currentStage) {
        if (currentStage > lastTriggeredStage) {
            lastTriggeredStage = currentStage;
            return true;
        }
        return false;
    }

    public int getTargetHealthyCellCount(int currentStage) {
        return HEALTHY_CELL_TARGETS[stageIndex(currentStage)];
    }

    public int getMaxActiveTCells(int currentStage) {
        return MAX_ACTIVE_TCELLS[stageIndex(currentStage)];
    }

    public float getTCellSpawnInterval(int currentStage) {
        return TCELL_SPAWN_INTERVALS[stageIndex(currentStage)];
    }

    public void reset() {
        lastTriggeredStage = 0;
        spawnTimer = 0f;
    }

    private int stageIndex(int currentStage) {
        int clampedStage = Math.max(1, Math.min(4, currentStage));
        return clampedStage - 1;
    }
}
