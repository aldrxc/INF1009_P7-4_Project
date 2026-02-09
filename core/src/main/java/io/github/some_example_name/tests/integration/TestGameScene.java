package io.github.some_example_name.tests.integration;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;

import io.github.some_example_name.engine.entity.RenderableEntity;
import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.io.OutputManager;
import io.github.some_example_name.engine.scene.AbstractScene;

public class TestGameScene extends AbstractScene {
    
    private TestPlayer player;
    private TestEnemy enemy1;
    private TestEnemy enemy2;
    private TestEnemy enemy3;
    
    private List<RenderableEntity> localRenderables = new ArrayList<>();
    
    @Override
    protected void onInitialise() {
        // 1. Create Player at Bottom Center
        player = new TestPlayer("Hero", 400, 100);
        createEntity(player);
        localRenderables.add(player);
        
        // 2. Create Falling Enemies
        enemy1 = new TestEnemy("Drop 1", 100, 600);
        createEntity(enemy1);
        localRenderables.add(enemy1);
        
        enemy2 = new TestEnemy("Drop 2", 300, 750);
        createEntity(enemy2);
        localRenderables.add(enemy2);
        
        enemy3 = new TestEnemy("Drop 3", 600, 900);
        createEntity(enemy3);
        localRenderables.add(enemy3);
    }
    
    @Override
    protected void onUpdate(float delta) {
        if (player != null) player.update(delta);
        if (enemy1 != null) enemy1.update(delta);
        if (enemy2 != null) enemy2.update(delta);
        if (enemy3 != null) enemy3.update(delta);
        
        // Reset key
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.R)) {
            player.setPosition(400, 100);
        }

        // --- RESTORED: Spacebar Sound ---
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.SPACE)) {
            System.out.println("Spacebar pressed! Playing sound...");
            IOManager.getInstance().getAudio().playSound("test.mp3");
        }
    }
    
    @Override
    public void render(float delta) {
        OutputManager output = IOManager.getInstance().getOutputManager();
        output.beginFrame();
        
        for (RenderableEntity entity : localRenderables) {
            output.drawEntity(entity.asGameEntity());
        }
        
        output.endFrame();
    }
    
    @Override
    protected void onDispose() {
        if (player != null) player.dispose();
        if (enemy1 != null) enemy1.dispose();
        if (enemy2 != null) enemy2.dispose();
        if (enemy3 != null) enemy3.dispose();
    }
}