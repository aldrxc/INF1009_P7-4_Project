// core/src/main/java/io/github/some_example_name/tests/Demo/TestPlayer.java
package io.github.some_example_name.tests.Demo;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.entity.RenderableEntity;
import io.github.some_example_name.engine.movement.MovementManager;

public class TestPlayer extends RenderableEntity implements Collidable {

    private static final float HIT_COOLDOWN_SECONDS = 0.75f;
    private float minX = 0f;
    private float minY = 0f;
    private float maxX = Float.NaN;
    private float maxY = Float.NaN;

    private final MovementManager movementManager;
    private float hitCooldown;
    private boolean hitEvent;

    public TestPlayer(String name, float x, float y) {
        super(x, y, 64, 64);
        this.setTexture(DemoTextureFactory.createPlayerTexture());
        this.movementManager = new MovementManager();
    }

    @Override
    public void update(float deltaTime) {
        if (hitCooldown > 0f) hitCooldown -= deltaTime;
        movementManager.handlePlayerMovement(this, 400f, deltaTime);
        clampToBounds();
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof TestEnemy) {
            if (hitCooldown <= 0f) {
                hitEvent = true;
                hitCooldown = HIT_COOLDOWN_SECONDS;
            }
            resolveEnemyCollision((TestEnemy) other);
        }
        if (other instanceof TestWall) {
            resolveWallCollision((TestWall) other);
        }
    }

    public boolean consumeHitEvent() {
        if (hitEvent) {
            hitEvent = false;
            return true;
        }
        return false;
    }

    public boolean isInvulnerable() {
        return hitCooldown > 0f;
    }

    private void resolveWallCollision(TestWall wall) {
        float pCX = getPositionX() + getWidth() / 2;
        float pCY = getPositionY() + getHeight() / 2;
        float wCX = wall.getPositionX() + wall.getWidth() / 2;
        float wCY = wall.getPositionY() + wall.getHeight() / 2;
        float dx = wCX - pCX;
        float dy = wCY - pCY;
        float combinedHalfWidth = (getWidth() + wall.getWidth()) / 2;
        float combinedHalfHeight = (getHeight() + wall.getHeight()) / 2;
        float overlapX = combinedHalfWidth - Math.abs(dx);
        float overlapY = combinedHalfHeight - Math.abs(dy);

        if (overlapX < overlapY) {
            if (dx > 0) setPosition(wall.getPositionX() - getWidth(), getPositionY());
            else setPosition(wall.getPositionX() + wall.getWidth(), getPositionY());
        } else {
            if (dy > 0) setPosition(getPositionX(), wall.getPositionY() - getHeight());
            else setPosition(getPositionX(), wall.getPositionY() + wall.getHeight());
        }
    }

    private void resolveEnemyCollision(TestEnemy enemy) {
        float pCX = getPositionX() + getWidth() / 2;
        float pCY = getPositionY() + getHeight() / 2;
        float eCX = enemy.getPositionX() + enemy.getWidth() / 2;
        float eCY = enemy.getPositionY() + enemy.getHeight() / 2;
        float dx = eCX - pCX;
        float dy = eCY - pCY;
        float overlapX = ((getWidth() + enemy.getWidth()) / 2) - Math.abs(dx);
        float overlapY = ((getHeight() + enemy.getHeight()) / 2) - Math.abs(dy);

        if (overlapX < overlapY) {
            if (dx > 0) enemy.setPosition(getPositionX() + getWidth(), enemy.getPositionY());
            else enemy.setPosition(getPositionX() - enemy.getWidth(), enemy.getPositionY());
        } else {
            if (dy < 0) setPosition(getPositionX(), enemy.getPositionY() + enemy.getHeight());
        }
    }

    public void setMovementBounds(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    private void clampToBounds() {
        if (Float.isNaN(maxX) || Float.isNaN(maxY)) return;

        float clampedX = Math.max(minX, Math.min(getPositionX(), maxX - getWidth()));
        float clampedY = Math.max(minY, Math.min(getPositionY(), maxY - getHeight()));
        setPosition(clampedX, clampedY);
    }

    public void dispose() {
        if (getTexture() != null) getTexture().getTexture().dispose();
    }
}
