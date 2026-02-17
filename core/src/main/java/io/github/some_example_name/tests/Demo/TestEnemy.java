// core/src/main/java/io/github/some_example_name/tests/Demo/TestEnemy.java
package io.github.some_example_name.tests.Demo;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.entity.RenderableEntity;
import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.movement.MovementManager;

public class TestEnemy extends RenderableEntity implements Collidable {

    private TextureRegion redTexture;
    private TextureRegion yellowTexture;

    private float speed = 150f;
    private float soundTimer = 0f;
    private float slideDirection = 1f;
    private float lastDelta = 1f / 60f;

    private final MovementManager movementManager;

    public TestEnemy(String name, float x, float y) {
        super(x, y, 48, 48);
        redTexture = DemoTextureFactory.createEnemyTexture(false);
        yellowTexture = DemoTextureFactory.createEnemyTexture(true);
        this.setTexture(redTexture);
        this.movementManager = new MovementManager();
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(40f, speed);
    }

    @Override
    public void update(float deltaTime) {
        if (!Float.isNaN(deltaTime) && !Float.isInfinite(deltaTime) && deltaTime > 0f) {
            lastDelta = deltaTime;
        }
        if (soundTimer > 0) soundTimer -= deltaTime;
        this.setTexture(redTexture);

        Vector2 velocity = new Vector2(0, -speed);
        movementManager.moveNpc(this, velocity, deltaTime);

        if (getPositionY() < -50) {
            float newX = (float) (Math.random() * 750);
            setPosition(newX, 650);
            slideDirection = Math.random() > 0.5 ? 1f : -1f;
        }
    }

    @Override
    public void onCollision(Collidable other) {
        if (other instanceof TestPlayer) {
            TestPlayer player = (TestPlayer) other;
            this.setTexture(yellowTexture);
            if (soundTimer <= 0) {
                IOManager.getInstance().getAudio().playSound("crash.mp3");
                soundTimer = 0.2f;
            }
            if (this.getPositionY() > player.getPositionY() + player.getHeight() / 2) {
                this.setPosition(this.getPositionX(), player.getPositionY() + player.getHeight());
            }
        }

        if (other instanceof TestWall) {
            resolveWallCollision((TestWall) other);
            }
        }

    private void resolveWallCollision(TestWall wall) {
        float eCX = getPositionX() + getWidth() / 2f;
        float eCY = getPositionY() + getHeight() / 2f;
        float wCX = wall.getPositionX() + wall.getWidth() / 2f;
        float wCY = wall.getPositionY() + wall.getHeight() / 2f;

        float dx = wCX - eCX;
        float dy = wCY - eCY;

        float overlapX = ((getWidth() + wall.getWidth()) / 2f) - Math.abs(dx);
        float overlapY = ((getHeight() + wall.getHeight()) / 2f) - Math.abs(dy);

        if (overlapX <= 0 || overlapY <= 0) return;

        final float EPS = 0.01f;

        // Side hit: resolve horizontally (prevents phasing through wall sides)
        if (overlapX < overlapY) {
            if (dx > 0f) setPosition(wall.getPositionX() - getWidth() - EPS, getPositionY());
            else setPosition(wall.getPositionX() + wall.getWidth() + EPS, getPositionY());
            slideDirection *= -1f;
            return;
        }

        // Vertical hit
        if (dy > 0f) {
            // wall is above enemy -> place enemy below wall
            setPosition(getPositionX(), wall.getPositionY() - getHeight() - EPS);
        } else {
            // enemy is above wall -> place enemy on top
            setPosition(getPositionX(), wall.getPositionY() + wall.getHeight() + EPS);

            // optional top slide
            float dt = Math.max(0f, Math.min(lastDelta, 1f / 30f));
            float oldX = getPositionX();
            float slideSpeed = 100f;
            setPosition(oldX + (slideSpeed * slideDirection * dt), getPositionY());

            // if slide immediately re-penetrates, revert and bounce direction
            if (getBounds().overlaps(wall.getBounds())) {
                setPosition(oldX, getPositionY());
                slideDirection *= -1f;
            }
        }
    }

    public void dispose() {
        if (redTexture != null) redTexture.getTexture().dispose();
        if (yellowTexture != null) yellowTexture.getTexture().dispose();
    }
}
