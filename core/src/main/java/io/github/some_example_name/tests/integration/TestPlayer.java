package io.github.some_example_name.tests.integration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.entity.GameEntity;
import io.github.some_example_name.engine.entity.RenderableEntity;
import io.github.some_example_name.engine.movement.MovementManager;

public class TestPlayer extends RenderableEntity {

    private MovementManager movementManager;
    private GameEntity movementAdapter; 
    private Vector2 internalPosition = new Vector2();
    
    // Store previous position to "undo" movement on collision
    private float prevX, prevY;

    public TestPlayer(String name, float x, float y) {
        super(x, y, 64, 64);
        createTexture();
        movementManager = new MovementManager();
        
        movementAdapter = new GameEntity() {
            public Vector2 getPosition() { return internalPosition; }
            public float getWidth() { return TestPlayer.this.getWidth(); }
            public float getHeight() { return TestPlayer.this.getHeight(); }
            public TextureRegion getTexture() { return null; } 
        };
    }

    private void createTexture() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLUE);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        this.setTexture(new TextureRegion(tex));
        pixmap.dispose();
    }

    @Override
    public void update(float deltaTime) {
        // 1. Save valid position BEFORE moving
        prevX = getPositionX();
        prevY = getPositionY();
        
        internalPosition.set(getPositionX(), getPositionY());

        // 2. Try to move
        movementManager.handleWASDMovementNormalized(movementAdapter, 200f, deltaTime);
        
        // 3. Update Position
        super.setPosition(internalPosition.x, internalPosition.y);
    }
    
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof TestEnemy) {
            // COLLISION DETECTED!
            // Undo the movement (Step back to previous safe position)
            super.setPosition(prevX, prevY);
            
            // Optional: Play sound
            // IOManager.getInstance().getAudio().playSound("test.mp3");
            System.out.println("Blocked by Enemy!");
        }
    }

    public void dispose() {
        if (getTexture() != null) getTexture().getTexture().dispose();
    }
}