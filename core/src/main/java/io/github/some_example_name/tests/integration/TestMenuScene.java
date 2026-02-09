package io.github.some_example_name.tests.integration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.scene.AbstractScene;
import io.github.some_example_name.engine.scene.SceneManager;

public class TestMenuScene extends AbstractScene {

    // LibGDX tools for drawing text (Bypassing OutputManager for the Menu)
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    protected void onInitialise() {
        System.out.println("=== MENU SCENE INITIALIZED ===");
        
        batch = new SpriteBatch();
        font = new BitmapFont(); // Loads the default arial font
        font.getData().setScale(2.0f); // Make text bigger
        font.setColor(Color.WHITE);
    }

    @Override
    protected void onUpdate(float delta) {
        // Switch to Game on ENTER
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.ENTER)) {
            System.out.println(">> STARTING GAME...");
            SceneManager.getInstance().setActive("game");
        }
    }

    @Override
    public void render(float delta) {
        // Clear screen to Black
        ScreenUtils.clear(0, 0, 0, 1);
        
        batch.begin();
        
        // Draw Centered Text
        String text = "PRESS ENTER TO START";
        float x = (Gdx.graphics.getWidth() - 300) / 2f;
        float y = Gdx.graphics.getHeight() / 2f;
        
        font.draw(batch, text, x, y);
        
        batch.end();
    }

    @Override
    protected void onDispose() {
        batch.dispose();
        font.dispose();
    }
}