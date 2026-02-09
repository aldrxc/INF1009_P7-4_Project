package io.github.some_example_name.tests.integration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.entity.RenderableEntity;
import io.github.some_example_name.engine.io.IOManager;

public class TestEnemy extends RenderableEntity {

    // Textures
    private Texture redTexture;
    private Texture yellowTexture;
    
    // Fall Speed
    private float speed = 150f;
    private float hitTimer = 0f;

    public TestEnemy(String name, float x, float y) {
        super(x, y, 48, 48);
        createTexture();
    }

    private void createTexture() {
        // Red (Normal)
        Pixmap p1 = new Pixmap(48, 48, Pixmap.Format.RGBA8888);
        p1.setColor(Color.RED);
        p1.fill();
        redTexture = new Texture(p1);
        p1.dispose();

        // Yellow (Hit)
        Pixmap p2 = new Pixmap(48, 48, Pixmap.Format.RGBA8888);
        p2.setColor(Color.YELLOW);
        p2.fill();
        yellowTexture = new Texture(p2);
        p2.dispose();

        this.setTexture(new TextureRegion(redTexture));
    }

    @Override
    public void update(float deltaTime) {
        // 1. GRAVITY (Always applies!)
        float newY = getPositionY() - (speed * deltaTime);
        
        // 2. LOOP LOGIC (Recycle if it falls off bottom)
        if (newY < -50) {
            newY = 650;
            float newX = (float) (Math.random() * 750);
            super.setPosition(newX, newY);
            this.setTexture(new TextureRegion(redTexture)); // Reset color
        } else {
            super.setPosition(getPositionX(), newY);
        }

        // 3. Color Timer
        if (hitTimer > 0) {
            hitTimer -= deltaTime;
            if (hitTimer <= 0) {
                this.setTexture(new TextureRegion(redTexture));
            }
        }
    }
    
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof TestPlayer) {
            TestPlayer player = (TestPlayer) other;

            // --- VISUALS ---
            if (hitTimer <= 0) {
                IOManager.getInstance().getAudio().playSound("crash.mp3");
                this.setTexture(new TextureRegion(yellowTexture));
                hitTimer = 0.5f;
            }
            
            // --- PHYSICS RESOLUTION (The "Landing" Logic) ---
            // 1. Calculate the top of the player
            float playerTop = player.getPositionY() + player.getHeight();
            
            // 2. Check if we are landing ON TOP (not hitting from bottom)
            if (this.getPositionY() >= player.getPositionY()) {
                
                // 3. SNAP to the top! 
                // This cancels out the gravity frame, making it "stick"
                super.setPosition(getPositionX(), playerTop);
            }
        }
    }
    
    public void dispose() {
        if (redTexture != null) redTexture.dispose();
        if (yellowTexture != null) yellowTexture.dispose();
    }
}