package io.github.some_example_name.engine.io;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

// Interfaces: Implements Disposable for cleanup and InputProcessor for logic
public class DynamicInput implements InputProcessor, Disposable {

    private Map<Integer, Boolean> keyState;
    private Map<Integer, Boolean> keyJustPressed;
    private Vector2 mousePosition;

    public void initialize() {
        keyState = new HashMap<>();
        keyJustPressed = new HashMap<>();
        mousePosition = new Vector2();
    }

    // --- Logic Queries ---
    public boolean isKeyPressed(int keycode) {
        return keyState.getOrDefault(keycode, false);
    }

    public boolean isKeyJustPressed(int keycode) {
        boolean pressed = keyJustPressed.getOrDefault(keycode, false);
        if (pressed)
            keyJustPressed.put(keycode, false);
        return pressed;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    // --- InputProcessor Implementation ---
    @Override
    public boolean keyDown(int keycode) {
        keyState.put(keycode, true);
        keyJustPressed.put(keycode, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyState.put(keycode, false);
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int p, int b) {
        mousePosition.set(x, y);
        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int p, int b) {
        mousePosition.set(x, y);
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        mousePosition.set(x, y);
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchCancelled(int x, int y, int p, int b) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int p) {
        return false;
    }

    @Override
    public boolean scrolled(float x, float y) {
        return false;
    }

    @Override
    public void dispose() {
        keyState.clear();
        keyJustPressed.clear();
    }
}