package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.scene.SceneManager;
import io.github.some_example_name.game.scene.GameScene;
import io.github.some_example_name.game.scene.LoseScene;
import io.github.some_example_name.game.scene.PauseScene;
import io.github.some_example_name.game.scene.StartScene;
import io.github.some_example_name.game.entity.TextureFactory; // Placeholder textures! 
import io.github.some_example_name.game.scene.WinScene;

public class GameMaster extends Game {
    private IOManager ioManager;
    private SceneManager sceneManager;

    @Override
    public void create() {
        System.out.println("==========================================");
        System.out.println("=      GAME ENGINE INITIALISATION        =");
        System.out.println("==========================================");

        ioManager = IOManager.getInstance();
        ioManager.init();
        ioManager.getAudio().preloadSound("crash.mp3");
        ioManager.getAudio().preloadSound("test.mp3");

        if (ioManager.getOutputManager() != null) {
            ioManager.getOutputManager().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        sceneManager = new SceneManager();
        sceneManager.setOnSceneActivated(() -> ioManager.getDynamicInput().clearJustPressed());

        sceneManager.load("start", new StartScene(sceneManager));
        sceneManager.load("game", new GameScene(sceneManager));
        sceneManager.load("pause", new PauseScene(sceneManager));
        sceneManager.load("win", new WinScene(sceneManager));
        sceneManager.load("lose", new LoseScene(sceneManager));

        sceneManager.setActive("start");
        System.out.println("Engine Online: Start Scene Loaded");
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
        if (sceneManager != null) sceneManager.dispose();
        if (ioManager != null) ioManager.dispose();
        TextureFactory.disposeAll();
    }
}
