package io.github.some_example_name.engine.movement;

import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.entity.Entity;

public class NpcMovementController {
    
    // ============================================================================
    // RANDOM WANDERING BEHAVIORS
    // ============================================================================
    
    /**
     * Make NPC wander randomly with smooth direction changes
     * Entity moves in random directions, changing periodically
     * 
     * NOTE: Entity must store currentDirection and directionTimer as state
     * 
     * @param entity The NPC entity to move
     * @param speed Movement speed in pixels per second
     * @param currentDirection Current movement direction (will be modified)
     * @param directionTimer Time until direction change (will be modified - use array)
     * @param changeInterval How often to change direction (seconds)
     * @param deltaTime Time since last frame
     */
    public void wanderRandomly(Entity entity, float speed, Vector2 currentDirection, 
                               float[] directionTimer, float changeInterval, float deltaTime) {
        
        // Decrease timer
        directionTimer[0] -= deltaTime;
        
        // Time to pick new random direction?
        if (directionTimer[0] <= 0) {
            // Pick random angle (0 to 360 degrees)
            float angle = (float) (Math.random() * 2 * Math.PI);
            currentDirection.x = (float) Math.cos(angle);
            currentDirection.y = (float) Math.sin(angle);
            
            // Reset timer
            directionTimer[0] = changeInterval;
        }
        
        // Move in current direction
        Vector2 pos = entity.getPosition();
        entity.setPosition(
            pos.x + currentDirection.x * speed * deltaTime,
            pos.y + currentDirection.y * speed * deltaTime
        );
    }
    
    /**
     * Wander by moving to random points (simpler, no external state needed)
     * Entity picks random target positions and moves toward them
     * 
     * NOTE: Entity must store targetPos as state
     * 
     * @param entity The NPC entity to move
     * @param speed Movement speed in pixels per second
     * @param targetPos Current target position (will be modified when reached)
     * @param stopDistance How close to get before picking new target
     * @param minX Minimum X boundary for random targets
     * @param maxX Maximum X boundary for random targets
     * @param minY Minimum Y boundary for random targets
     * @param maxY Maximum Y boundary for random targets
     * @param deltaTime Time since last frame
     */
    public void wanderToRandomPoints(Entity entity, float speed, Vector2 targetPos,
                                     float stopDistance, float minX, float maxX, 
                                     float minY, float maxY, float deltaTime) {
        
        // Calculate center of entity
        float entityCenterX = entity.getPosition().x + entity.getWidth() / 2;
        float entityCenterY = entity.getPosition().y + entity.getHeight() / 2;
        
        // Calculate distance to current target
        float dx = targetPos.x - entityCenterX;
        float dy = targetPos.y - entityCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        // Reached target? Pick new random target
        if (distance < stopDistance) {
            targetPos.x = minX + (float) (Math.random() * (maxX - minX));
            targetPos.y = minY + (float) (Math.random() * (maxY - minY));
        }
        
        // Move toward target
        if (distance > 0) {
            float dirX = dx / distance;
            float dirY = dy / distance;
            
            Vector2 pos = entity.getPosition();
            entity.setPosition(
                pos.x + dirX * speed * deltaTime,
                pos.y + dirY * speed * deltaTime
            );
        }
    }
    
    // ============================================================================
    // CHASE BEHAVIOR
    // ============================================================================
    
    /**
     * Make entity chase/follow a target
     * Entity moves directly toward target
     * 
     * @param entity The chasing entity
     * @param target The target to chase
     * @param speed Chase speed in pixels per second
     * @param deltaTime Time since last frame
     */
    public void chaseTarget(Entity entity, Entity target, float speed, float deltaTime) {
        // Calculate centers
        float entityCenterX = entity.getPosition().x + entity.getWidth() / 2;
        float entityCenterY = entity.getPosition().y + entity.getHeight() / 2;
        float targetCenterX = target.getPosition().x + target.getWidth() / 2;
        float targetCenterY = target.getPosition().y + target.getHeight() / 2;
        
        // Calculate direction
        float dx = targetCenterX - entityCenterX;
        float dy = targetCenterY - entityCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            // Normalize and move
            float dirX = dx / distance;
            float dirY = dy / distance;
            
            Vector2 pos = entity.getPosition();
            entity.setPosition(
                pos.x + dirX * speed * deltaTime,
                pos.y + dirY * speed * deltaTime
            );
        }
    }
    
    /**
     * Chase target only if within detection range
     * 
     * @param entity The chasing entity
     * @param target The target to chase
     * @param speed Chase speed in pixels per second
     * @param detectionRange Maximum distance to detect and chase target
     * @param deltaTime Time since last frame
     * @return true if currently chasing, false if target out of range
     */
    public boolean chaseIfInRange(Entity entity, Entity target, float speed, 
                                  float detectionRange, float deltaTime) {
        
        // Calculate distance between centers
        float entityCenterX = entity.getPosition().x + entity.getWidth() / 2;
        float entityCenterY = entity.getPosition().y + entity.getHeight() / 2;
        float targetCenterX = target.getPosition().x + target.getWidth() / 2;
        float targetCenterY = target.getPosition().y + target.getHeight() / 2;
        
        float dx = targetCenterX - entityCenterX;
        float dy = targetCenterY - entityCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance <= detectionRange) {
            chaseTarget(entity, target, speed, deltaTime);
            return true;
        }
        return false;
    }
    
    // ============================================================================
    // FLEE BEHAVIOR
    // ============================================================================
    
    /**
     * Make entity run away from a threat
     * Entity moves in opposite direction from threat
     * 
     * @param entity The fleeing entity
     * @param threat The threat to run from
     * @param speed Flee speed in pixels per second
     * @param deltaTime Time since last frame
     */
    public void fleeFromThreat(Entity entity, Entity threat, float speed, float deltaTime) {
        // Calculate centers
        float entityCenterX = entity.getPosition().x + entity.getWidth() / 2;
        float entityCenterY = entity.getPosition().y + entity.getHeight() / 2;
        float threatCenterX = threat.getPosition().x + threat.getWidth() / 2;
        float threatCenterY = threat.getPosition().y + threat.getHeight() / 2;
        
        // Calculate direction AWAY from threat (reversed)
        float dx = entityCenterX - threatCenterX;
        float dy = entityCenterY - threatCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            // Normalize and move away
            float dirX = dx / distance;
            float dirY = dy / distance;
            
            Vector2 pos = entity.getPosition();
            entity.setPosition(
                pos.x + dirX * speed * deltaTime,
                pos.y + dirY * speed * deltaTime
            );
        }
    }
    
    /**
     * Flee only if threat is too close
     * 
     * @param entity The fleeing entity
     * @param threat The threat to run from
     * @param speed Flee speed in pixels per second
     * @param dangerRange Distance threshold to start fleeing
     * @param deltaTime Time since last frame
     * @return true if currently fleeing, false if threat far enough
     */
    public boolean fleeIfTooClose(Entity entity, Entity threat, float speed, 
                                  float dangerRange, float deltaTime) {
        
        // Calculate distance between centers
        float entityCenterX = entity.getPosition().x + entity.getWidth() / 2;
        float entityCenterY = entity.getPosition().y + entity.getHeight() / 2;
        float threatCenterX = threat.getPosition().x + threat.getWidth() / 2;
        float threatCenterY = threat.getPosition().y + threat.getHeight() / 2;
        
        float dx = threatCenterX - entityCenterX;
        float dy = threatCenterY - entityCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance <= dangerRange) {
            fleeFromThreat(entity, threat, speed, deltaTime);
            return true;
        }
        return false;
    }
}