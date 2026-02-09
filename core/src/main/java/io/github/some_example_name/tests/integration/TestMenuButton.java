package io.github.some_example_name.tests.integration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.entity.RenderableEntity;

public class TestMenuButton extends RenderableEntity {

    public TestMenuButton(float x, float y) {
        super(x, y, 0, 0); 
        
        Texture tex;
        try {
            // Try to load the image
            tex = new Texture("pressenter.png");
        } catch (Exception e) {
            System.err.println("WARNING: 'pressenter.png' not found! Using fallback square.");
            
            // Fallback: Create a Purple Square if image is missing
            Pixmap pixmap = new Pixmap(200, 50, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.PURPLE);
            pixmap.fill();
            tex = new Texture(pixmap);
            pixmap.dispose();
        }
        
        // Set texture and size
        setTexture(new TextureRegion(tex));
        // Use the safe casting we fixed earlier
        super.setSize((float)tex.getWidth(), (float)tex.getHeight());
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void onCollision(Collidable other) {
    }

    public void dispose() {
        if (getTexture() != null) {
            getTexture().getTexture().dispose();
        }
    }
}