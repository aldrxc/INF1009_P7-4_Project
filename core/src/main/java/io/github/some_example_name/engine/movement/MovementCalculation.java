package io.github.some_example_name.engine.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.entity.Entity;

/**
 * MovementHelper - Utility class for movement calculations
 * 
 * Single Responsibility: Provide utility methods for spatial calculations
 * 
 * This class provides helper methods that both PlayerMovementController
 * and AIMovementController might need (distance, direction, etc.)
 */
public class MovementCalculation {
    
    /**
     * Calculate center position of an entity
     * 
     * @param entity The entity
     * @return Vector2 containing center coordinates
     */
    public Vector2 getEntityCenter(Entity entity) {
        float centerX = entity.getPosition().x + entity.getWidth() / 2;
        float centerY = entity.getPosition().y + entity.getHeight() / 2;
        return new Vector2(centerX, centerY);
    }
    
    /**
     * Calculate distance between centers of two entities
     * Uses Euclidean distance formula
     * 
     * @param a First entity
     * @param b Second entity
     * @return Distance in pixels
     */
    public float getDistanceBetween(Entity a, Entity b) {
        Vector2 centerA = getEntityCenter(a);
        Vector2 centerB = getEntityCenter(b);
        
        float dx = centerB.x - centerA.x;
        float dy = centerB.y - centerA.y;
        
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Get normalized direction vector from one entity to another
     * Returns unit vector (length = 1) pointing from 'from' toward 'to'
     * 
     * @param from Source entity
     * @param to Target entity
     * @return Normalized direction vector
     */
    public Vector2 getDirectionTo(Entity from, Entity to) {
        Vector2 centerFrom = getEntityCenter(from);
        Vector2 centerTo = getEntityCenter(to);
        
        float dx = centerTo.x - centerFrom.x;
        float dy = centerTo.y - centerFrom.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            // Normalize
            return new Vector2(dx / distance, dy / distance);
        }
        
        return new Vector2(0, 0);
    }
    
    /**
     * Get normalized direction vector away from target
     * Returns unit vector pointing from 'from' away from 'awayFrom'
     * 
     * @param from Source entity
     * @param awayFrom Entity to move away from
     * @return Normalized direction vector (opposite direction)
     */
    public Vector2 getDirectionAwayFrom(Entity from, Entity awayFrom) {
        Vector2 centerFrom = getEntityCenter(from);
        Vector2 centerAwayFrom = getEntityCenter(awayFrom);
        
        // Reversed direction
        float dx = centerFrom.x - centerAwayFrom.x;
        float dy = centerFrom.y - centerAwayFrom.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            // Normalize
            return new Vector2(dx / distance, dy / distance);
        }
        
        return new Vector2(0, 0);
    }
    
    /**
     * Check if two entities are within a certain range
     * 
     * @param a First entity
     * @param b Second entity
     * @param range Distance threshold in pixels
     * @return true if entities are within range
     */
    public boolean isInRange(Entity a, Entity b, float range) {
        return getDistanceBetween(a, b) <= range;
    }
    
    /**
     * Calculate angle in degrees from one entity to another
     * 0° = right, 90° = up, 180° = left, 270° = down
     * 
     * Useful for rotating sprites to face target
     * 
     * @param from Source entity
     * @param to Target entity
     * @return Angle in degrees
     */
    public float getAngleTo(Entity from, Entity to) {
        Vector2 direction = getDirectionTo(from, to);
        return (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
    }
    
    /**
     * Apply velocity to entity position
     * Simple physics: position += velocity * deltaTime
     * 
     * @param entity Entity to move
     * @param velocity Velocity vector in pixels per second
     * @param deltaTime Time since last frame
     */
    public void applyVelocity(Entity entity, Vector2 velocity, float deltaTime) {
        Vector2 pos = entity.getPosition();
        entity.setPosition(
            pos.x + velocity.x * deltaTime,
            pos.y + velocity.y * deltaTime
        );
    }
}