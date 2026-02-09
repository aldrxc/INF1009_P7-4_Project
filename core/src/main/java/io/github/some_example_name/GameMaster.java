package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

// IMPORTS
import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.tests.TestEntity;
import com.badlogic.gdx.math.Vector2;

public class GameMaster extends Game {

    // We add a test object here so we can see if rendering works
    private TestEntity testObject;

    @Override
    public void create() {
        // 1. Initialize the Engine Hub
        IOManager.getInstance().init();
        System.out.println("IOManager Online. Systems Check Initiated...");

        // 2. Create a dummy object to test the OutputManager
        testObject = new TestEntity(200, 200);

        System.out.println("------------------------------------------------");
        System.out.println(" VISUAL TEST: You should see a RED SQUARE.");
        System.out.println(" INPUT TEST:  Use ARROW KEYS to move.");
        System.out.println(" MOUSE TEST:  Click anywhere to see coordinates.");
        System.out.println(" AUDIO TEST:  Press SPACEBAR to play sound.");
        System.out.println("------------------------------------------------");
    }

    @Override
    public void render() {
        // --- LOGIC PHASE (Testing InputManager) ---
        float dt = Gdx.graphics.getDeltaTime();

        // We use OUR DynamicInput, not Gdx.input directly!
        Vector2 pos = testObject.getPosition();
        if (IOManager.getInstance().getDynamicInput().isKeyPressed(Input.Keys.LEFT)) {
            pos.x -= 200 * dt;
        }
        if (IOManager.getInstance().getDynamicInput().isKeyPressed(Input.Keys.RIGHT)) {
            pos.x += 200 * dt;
        }
        if (IOManager.getInstance().getDynamicInput().isKeyPressed(Input.Keys.UP)) {
            pos.y += 200 * dt;
        }
        if (IOManager.getInstance().getDynamicInput().isKeyPressed(Input.Keys.DOWN)) {
            pos.y -= 200 * dt;
        }
        testObject.setPosition(pos.x, pos.y);

        // Test Mouse Input (Hardware Check)
        if (Gdx.input.justTouched()) {
            System.out.println("[MOUSE] Click detected at: " +
                    IOManager.getInstance().getDynamicInput().getMousePosition());
        }

        // AUDIO TEST
        // We use isKeyJustPressed so it doesn't machine-gun the sound 60 times a second
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("[AUDIO] Attempting to play 'test.mp3'...");
            IOManager.getInstance().getAudio().playSound("test.mp3");
        }

        // --- RENDER PHASE (Testing OutputManager) ---

        // 1. Clear Screen & Prep Camera
        IOManager.getInstance().getOutputManager().beginFrame();

        // 2. Draw our test entity
        IOManager.getInstance().getOutputManager().drawEntity(testObject);

        // 3. Finalize
        IOManager.getInstance().getOutputManager().endFrame();
    }

    @Override
    public void resize(int width, int height) {
        // Test Resize Logic
        IOManager.getInstance().getOutputManager().resize(width, height);
    }

    @Override
    public void dispose() {
        // Cleanup
        testObject.dispose();
        IOManager.getInstance().dispose();
        System.out.println("GameMaster Disposed.");
    }
}