package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.scene.SceneManager;
import io.github.some_example_name.tests.integration.TestGameScene;
import io.github.some_example_name.tests.integration.TestMenuScene;

/**
 * GameMaster - Main entry point for the game
 */
public class GameMaster extends Game {
    
    // Singleton managers
    private IOManager ioManager;
    private SceneManager sceneManager;
    
    @Override
    public void create() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      GAME ENGINE INITIALIZATION        ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        // 1. Init IO
        ioManager = IOManager.getInstance();
        ioManager.init();
        
        // 2. Init Scenes
        sceneManager = SceneManager.getInstance();
        
        // 3. Load BOTH Scenes
        // Load Game (but don't start it yet)
        TestGameScene gameScene = new TestGameScene();
        sceneManager.load("game", gameScene);
        
        // Load Menu (and start this one!)
        TestMenuScene menuScene = new TestMenuScene();
        sceneManager.load("menu", menuScene);
        
        // Set MENU as the starting screen
        sceneManager.setActive("menu");
        
        System.out.println("✓ Engine Online: Menu Loaded");
        printControls();
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        sceneManager.runFrame(delta);
    }
    
    @Override
    public void resize(int width, int height) {
        if (ioManager != null && ioManager.getOutputManager() != null) {
            ioManager.getOutputManager().resize(width, height);
        }
        if (sceneManager != null) {
            sceneManager.resize(width, height);
        }
    }
    
    @Override
    public void dispose() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         SHUTTING DOWN ENGINE           ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        if (sceneManager != null) sceneManager.dispose();
        if (ioManager != null) ioManager.dispose();
    }
    
    private void printControls() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║              CONTROLS                  ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ WASD or Arrow Keys - Move Player       ║");
        System.out.println("║ R - Reset player position              ║");
        System.out.println("║ SPACE - Play sound effect              ║");
        System.out.println("║ ENTER - Start Game (from Menu)         ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}