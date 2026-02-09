package io.github.some_example_name.engine.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.collision.Collidable;

public abstract class RenderableEntity extends Entity implements Collidable {
    
    // Rendering data
    private float width;
    private float height;
    private TextureRegion texture;
    
    // Collision data
    private Rectangle bounds;
    
    // Cached adapter
    private GameEntity cachedAdapter;
    
    public RenderableEntity(float x, float y, float width, float height) {
        super(x, y); 
        this.width = width;
        this.height = height;
        this.bounds = new Rectangle(x, y, width, height);
    }
    
    // ===== SMART BRIDGE METHOD =====
    public GameEntity asGameEntity() {
        if (cachedAdapter == null) {
            cachedAdapter = new GameEntity() {
                @Override
                public TextureRegion getTexture() { return RenderableEntity.this.texture; }
                @Override
                public Vector2 getPosition() { return new Vector2(getPositionX(), getPositionY()); }
                @Override
                public float getWidth() { return RenderableEntity.this.width; }
                @Override
                public float getHeight() { return RenderableEntity.this.height; }
            };
        }
        return cachedAdapter;
    }
    
    // ===== Collidable IMPLEMENTATION =====
    @Override
    public Rectangle getBounds() {
        bounds.set(getPositionX(), getPositionY(), width, height);
        return bounds;
    }
    
    @Override
    public void onCollision(Collidable other) {
        // Subclasses will override this
    }
    
    // ===== GETTERS/SETTERS =====
    public TextureRegion getTexture() { return texture; }
    public void setTexture(TextureRegion texture) { this.texture = texture; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    
    // --- THIS WAS MISSING! ---
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        if (this.bounds != null) {
            this.bounds.setSize(width, height);
        }
    }
}