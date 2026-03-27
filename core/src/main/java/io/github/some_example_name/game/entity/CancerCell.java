package io.github.some_example_name.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.game.io.CellInputMapper;
import io.github.some_example_name.game.movement.PlayerMovement;

public class CancerCell extends GameEntity {
    private static final float STARTING_SIZE = 72f;
    private static final float INITIAL_EXP_PER_LEVEL = 120f;
    private static final float EXP_GROWTH_FACTOR = 1.25f;
    private static final float PROGRESSION_POWER_GROWTH = 6f;
    private static final float VISUAL_SIZE_GROWTH = 4f;
    private static final float MAX_VISUAL_SIZE = 92f;
    private static final float MOVEMENT_ANIMATION_THRESHOLD = 0.01f;

    private final HealthBar healthBar;
    private final PlayerMovement playerMovement;
    private final CellInputMapper inputMapper;
    private final AnimationStateController<CancerCellState> animationController;

    private float exp = 0f;
    private float expToNextLevel = INITIAL_EXP_PER_LEVEL;
    private float progressionPower = STARTING_SIZE;
    private int level = 1;

    public CancerCell(float x, float y, CellInputMapper inputMapper) {
        super(x, y, STARTING_SIZE, STARTING_SIZE);
        if (inputMapper == null) {
            throw new IllegalArgumentException("CellInputMapper cannot be null.");
        }

        this.inputMapper = inputMapper;
        this.healthBar = new HealthBar(this, STARTING_SIZE, 5f, 4f);
        this.playerMovement = new PlayerMovement();
        this.animationController = new AnimationStateController<>(CancerCellState.class, CancerCellState.IDLE);

        Animation<TextureRegion> walkAnimation = CellAssets.getCancerWalkAnimation();
        TextureRegion idleFrame = walkAnimation.getKeyFrame(0f);
        animationController.setStillFrame(CancerCellState.IDLE, idleFrame);
        animationController.setAnimation(CancerCellState.RUN, walkAnimation);
        animationController.setStillFrame(CancerCellState.JUMP, idleFrame);
        animationController.setStillFrame(CancerCellState.FALL, idleFrame);

        applyProgression(STARTING_SIZE, STARTING_SIZE);
        setHitboxInsets(12f, 7f, 4f, 9f);
        setUseCircularHitbox(true);
        setDrawOffset(0f, 0f);
        setTexture(idleFrame);
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive()) {
            return;
        }
        Vector2 direction = inputMapper.processMovementInput();
        playerMovement.update(this, direction, inputMapper.checkDashAction(), deltaTime);
        animationController.setState(resolveAnimationState(direction), true);
        animationController.update(deltaTime);
        setTexture(animationController.getCurrentFrame());
    }

    @Override
    public void onCollision(Collidable other) {
        // GameScene handles gameplay outcomes.
    }

    public void gainExp(float amount) {
        if (amount <= 0f) {
            return;
        }

        this.exp += amount;
        while (this.exp >= expToNextLevel) {
            this.exp -= expToNextLevel;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        expToNextLevel *= EXP_GROWTH_FACTOR;
        progressionPower += PROGRESSION_POWER_GROWTH;
        float nextVisualSize = Math.min(MAX_VISUAL_SIZE, getWidth() + VISUAL_SIZE_GROWTH);
        applyProgression(progressionPower, nextVisualSize);
    }

    @Override
    public int getCollisionLayer() {
        return GameCollisionLayers.PLAYER;
    }

    @Override
    public int getCollisionMask() {
        return GameCollisionLayers.HEALTHY_CELL | GameCollisionLayers.IMMUNE_CELL;
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public int getLevel() {
        return level;
    }

    public float getExp() {
        return exp;
    }

    public float getExpToNextLevel() {
        return expToNextLevel;
    }

    public TextureRegion getCurrentTexture() {
        return getTexture();
    }

    private CancerCellState resolveAnimationState(Vector2 direction) {
        return direction != null && direction.len2() > MOVEMENT_ANIMATION_THRESHOLD
                ? CancerCellState.RUN
                : CancerCellState.IDLE;
    }

    private void applyProgression(float progressionPower, float visualSize) {
        applySize(progressionPower, visualSize, visualSize);
    }
}
