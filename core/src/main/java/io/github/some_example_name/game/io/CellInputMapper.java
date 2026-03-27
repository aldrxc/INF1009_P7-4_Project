package io.github.some_example_name.game.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.engine.io.DynamicInput;

public class CellInputMapper {
    private final DynamicInput input;

    public CellInputMapper(DynamicInput input) {
        if (input == null)
            throw new IllegalArgumentException("DynamicInput cannot be null");
        this.input = input;
    }

    public Vector2 processMovementInput() {
        Vector2 movement = new Vector2(0f, 0f);
        if (input.isKeyPressed(Input.Keys.W) || input.isKeyPressed(Input.Keys.UP))
            movement.y += 1f;
        if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Input.Keys.DOWN))
            movement.y -= 1f;
        if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Input.Keys.LEFT))
            movement.x -= 1f;
        if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Input.Keys.RIGHT))
            movement.x += 1f;
        if (movement.len2() > 0f)
            movement.nor();
        return movement;
    }

    public boolean checkDashAction() {
        return input.isKeyPressed(Input.Keys.SHIFT_LEFT) || input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
    }

    public boolean checkPauseAction() {
        return input.isKeyJustPressed(Input.Keys.P);
    }

    public boolean checkMenuAction() {
        return input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    public boolean checkConfirmAction() {
        return input.isKeyJustPressed(Input.Keys.ENTER);
    }

    public boolean checkRestartAction() {
        return input.isKeyJustPressed(Input.Keys.R);
    }

    public boolean checkDonateAction() {
        return input.isKeyJustPressed(Input.Keys.O);
    }

    public boolean checkDebugHitboxToggle() {
        return input.isKeyJustPressed(Input.Keys.F3);
    }

    public Vector2 getMouseSelection() {
        return input.getMousePosition();
    }
}
