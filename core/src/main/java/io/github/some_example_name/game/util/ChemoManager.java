package io.github.some_example_name.game.util;

/**
 * Controls recurring chemotherapy pressure once activated.
 * Later stages shorten the chemo interval slightly and make each hit stronger.
 */
public class ChemoManager {
    private static final float WARN_BEFORE = 5f;

    private float chemoTimer = 0f;
    private boolean active = false;
    private boolean warningSent = false;

    public void activate() {
        if (!active) {
            active = true;
            System.out.println("[Chemo] Chemotherapy activated!");
        }
    }

    public void reset() {
        chemoTimer = 0f;
        active = false;
        warningSent = false;
    }

    public float update(float delta, int currentStage, float currentSpreadPercent) {
        if (!active) {
            return 0f;
        }

        float interval = getIntervalForStage(currentStage, currentSpreadPercent);
        float reduction = getReductionForStage(currentStage, currentSpreadPercent);
        chemoTimer += delta;

        if (!warningSent && chemoTimer >= interval - WARN_BEFORE) {
            warningSent = true;
            System.out.println("[Chemo] WARNING - chemo incoming in " + (int) WARN_BEFORE + "s!");
        }

        if (chemoTimer >= interval) {
            chemoTimer = 0f;
            warningSent = false;
            System.out.println("[Chemo] Hit! Spread reduced by " + reduction + "%");
            return reduction;
        }

        return 0f;
    }

    public boolean isActive() {
        return active;
    }

    public float getTimeUntilNextChemo(int currentStage, float currentSpreadPercent) {
        return Math.max(0f, getIntervalForStage(currentStage, currentSpreadPercent) - chemoTimer);
    }

    private float getIntervalForStage(int currentStage, float currentSpreadPercent) {
        float baseInterval;
        switch (Math.max(1, Math.min(4, currentStage))) {
            case 1:
                baseInterval = 30f;
                break;
            case 2:
                baseInterval = 28f;
                break;
            case 3:
                baseInterval = 24f;
                break;
            case 4:
                baseInterval = 22f;
                break;
            default:
                baseInterval = 30f;
                break;
        }
        return Math.max(10f, baseInterval - (getSpreadAggression(currentSpreadPercent) * 6f));
    }

    private float getReductionForStage(int currentStage, float currentSpreadPercent) {
        return 7.0f;
    }

    private float getSpreadAggression(float currentSpreadPercent) {
        float clampedSpread = Math.max(0f, Math.min(100f, currentSpreadPercent));
        if (clampedSpread <= 75f) {
            return clampedSpread / 100f;
        }
        return 0.75f;
    }
}
