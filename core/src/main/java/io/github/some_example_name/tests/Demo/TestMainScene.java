// core/src/main/java/io/github/some_example_name/tests/Demo/TestMainScene.java
package io.github.some_example_name.tests.Demo;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.some_example_name.engine.entity.Entity;
import io.github.some_example_name.engine.io.IOManager;
import io.github.some_example_name.engine.io.OutputManager;
import io.github.some_example_name.engine.scene.AbstractScene;
import io.github.some_example_name.engine.scene.SceneManager;
import java.util.Locale;

public class TestMainScene extends AbstractScene {

    private static final int START_LIVES = 99;
    private static final float WIN_TIME_SECONDS = 45f;
    private static final float BASE_ENEMY_SPEED = 150f;
    private static final float MAX_ENEMY_SPEED = 340f;
    private static final int MAX_ENEMIES = 9;
    private static final float INITIAL_SPAWN_INTERVAL = 7f;
    private static final float MIN_SPAWN_INTERVAL = 2.5f;

    private TestPlayer player;
    private BitmapFont font;

    private int lives;
    private int score;
    private float elapsed;
    private float spawnTimer;
    private float spawnInterval;
    private boolean ended;

    @Override
    protected void onInitialise() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);

        lives = START_LIVES;
        score = 0;
        elapsed = 0f;
        spawnTimer = 0f;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        ended = false;

        createEntity(new TestWall(200, 300, 64));
        createEntity(new TestWall(550, 400, 64));

        player = new TestPlayer("Hero", 400, 100);
        OutputManager output = IOManager.getInstance().getOutputManager();
        player.setMovementBounds(0f, 0f, output.getWorldWidth(), output.getWorldHeight());
        createEntity(player);

        createEntity(new TestEnemy("Drop 1", 100, 600));
        createEntity(new TestEnemy("Drop 2", 300, 750));
        createEntity(new TestEnemy("Drop 3", 600, 900));
    }

    @Override
    protected void onUpdate(float delta) {
        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.P)) {
            SceneManager.getInstance().setActive("pause");
            return;
        }

        if (ended) return;

        elapsed += delta;
        score = (int) (elapsed * 100f);

        updateEnemyDifficulty();
        updateSpawning(delta);

        if (player.consumeHitEvent()) {
            lives--;
            if (lives <= 0) {
                ended = true;
                DemoRunStats.recordRun(score, elapsed, false);
                SceneManager.getInstance().setActive("lose");
                return;
            }
        }

        if (elapsed >= WIN_TIME_SECONDS) {
            ended = true;
            DemoRunStats.recordRun(score, elapsed, true);
            SceneManager.getInstance().setActive("win");
            return;
        }

        if (IOManager.getInstance().getDynamicInput().isKeyJustPressed(Input.Keys.SPACE)) {
            IOManager.getInstance().getAudio().playSound("test.mp3");
        }
    }

    private void updateEnemyDifficulty() {
        float scaledSpeed = Math.min(BASE_ENEMY_SPEED + elapsed * 4f, MAX_ENEMY_SPEED);
        for (Entity e : getEntities()) {
            if (e instanceof TestEnemy) {
                ((TestEnemy) e).setSpeed(scaledSpeed);
            }
        }
    }

    private void updateSpawning(float delta) {
        spawnTimer += delta;
        if (spawnTimer < spawnInterval) return;

        spawnTimer = 0f;
        spawnInterval = Math.max(MIN_SPAWN_INTERVAL, spawnInterval - 0.2f);

        if (countEnemies() < MAX_ENEMIES) {
            float x = 20f + (float) (Math.random() * 760f);
            createEntity(new TestEnemy("Wave", x, 680f));
        }
    }

    private int countEnemies() {
        int count = 0;
        for (Entity e : getEntities()) {
            if (e instanceof TestEnemy) count++;
        }
        return count;
    }

    @Override
    public void render(float delta) {
        OutputManager output = IOManager.getInstance().getOutputManager();
        output.beginFrame();

        for (Entity entity : getEntities()) {
            output.drawEntity(entity);
        }

        float top = output.getWorldHeight() - 15f;
        font.draw(output.getBatch(), "LIVES: " + lives, 20, top);
        font.draw(output.getBatch(), "SCORE: " + score, 20, top - 24f);
        font.draw(output.getBatch(),
            "TIME: " + String.format(Locale.US, "%.1f / %.0fs", elapsed, WIN_TIME_SECONDS), 20, top - 48f);
        font.draw(output.getBatch(), "P:PAUSE  SPACE:SFX", 20, top - 72f);

        if (player != null && player.isInvulnerable()) {
            font.draw(output.getBatch(), "HIT COOLING...", 20, top - 96f);
        }

        output.endFrame();
    }

    @Override
    protected void onDispose() {
        if (font != null) font.dispose();

        for (Entity e : getEntities()) {
            if (e instanceof TestWall) ((TestWall) e).dispose();
            if (e instanceof TestPlayer) ((TestPlayer) e).dispose();
            if (e instanceof TestEnemy) ((TestEnemy) e).dispose();
        }
    }
}
