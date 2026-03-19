package io.github.some_example_name.game.movement;

import java.util.function.IntPredicate;

import io.github.some_example_name.engine.entity.Entity;
import io.github.some_example_name.engine.movement.MovementManager;

public class PlayerMovement {
    private final MovementManager movementManager;
    private boolean isDashing = false;
    private float dashTimer = 0f;
    private static final float DASH_DURATION = 0.2f;
    private static final float DASH_MULTIPLIER = 3f;

    // Constructor
    public PlayerMovement(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    // Basic movement
    public void movePlayer(Entity player, float speed, float delta, IntPredicate isPressed) {
        movementManager.handlePlayerMovement(player, speed, delta, isPressed);
    }

    // Dash movement
    public void dashPlayer(Entity player, float speed, float delta, IntPredicate isPressed) {
        isDashing = true;
        dashTimer = DASH_DURATION;
        movementManager.handlePlayerMovement(player, speed * DASH_MULTIPLIER, delta, isPressed);
    }

    public void update(float delta) {
        if (isDashing) {
            dashTimer -= delta;
            if (dashTimer <= 0f) isDashing = false;
        }
    }

    public boolean isDashing() { return isDashing; }
}