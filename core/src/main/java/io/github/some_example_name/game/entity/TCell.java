package io.github.some_example_name.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.movement.MovementManager;
import io.github.some_example_name.game.movement.NpcBehaviour;

public class TCell extends GameEntity {
  
  private final HealthBar healthBar;
  private final MovementManager movementManager;

  private static final float TCELL_SIZE = 56f;
  private static final float DAMAGE_TO_CANCER = 15f;
  private static final float ATTACK_COOLDOWN = 1.0f;

  // Npc movement state
  private Vector2 wanderDir = new Vector2(1, 0);
  private float wanderTimer = 0f;
  private static final float WANDER_INTERVAL = 2f;
  private static final float CHASE_RANGE = 200f;
  private static final float SPEED = 80f;
  private CancerCell target; // needs to be set after spawn

  private float attackCooldown = 0f;

  public TCell(float x, float y) {
    super(x, y, TCELL_SIZE);
    applySize(TCELL_SIZE);
    this.healthBar = new HealthBar(this, TCELL_SIZE, 5f, 4f);
    this.movementManager = new MovementManager();
    this.texture = TextureFactory.createEnemyTexture(false); // Placehholder texture!
  }

  @Override
  public void update(float deltaTime) {
    if (!isAlive()) {
      setActive(false);
      return;
    }
    if (attackCooldown > 0f) attackCooldown -= deltaTime;

    // Wander movement logic
    wanderTimer += deltaTime;
    wanderDir = NpcBehaviour.updateWanderDirection(wanderDir, wanderTimer, WANDER_INTERVAL);
    if (wanderTimer >= WANDER_INTERVAL) wanderTimer = 0f;

    // Chase or wander logic
    if (target != null && movementManager.getDistanceBetween(this, target) < CHASE_RANGE) {
        NpcBehaviour.chase(this, target, SPEED, deltaTime, movementManager);
    } else {
        NpcBehaviour.wander(this, wanderDir, SPEED, deltaTime, movementManager);
    }

    // keep within bounds
    float x = Math.max(0, Math.min(getPositionX(), 800 - getWidth()));
    float y = Math.max(0, Math.min(getPositionY(), 600 - getHeight()));
    setPosition(x, y);
  }
  
  // Targeting logic
  public void setTarget(CancerCell target) {
    this.target = target;
  }

  @Override
  public void onCollision(Collidable other) {
    if (other instanceof CancerCell) {
      CancerCell cancer = (CancerCell) other;
      // TCell always damages cancer cell on contact
      if (attackCooldown <= 0f) {
        cancer.takeDamage(DAMAGE_TO_CANCER);
        attackCooldown = ATTACK_COOLDOWN;
      }
      // Cancer can only eat TCell if big enough
      if (cancer.canEat(this)) {
        takeDamage(cancer.getDamage());
      }
    }
  }

  @Override
  public int getCollisionLayer() { return 1 << 2; }

  @Override
  public int getCollisionMask() { return 1 << 0; }

  @Override
  public Rectangle getBounds() { return super.getBounds(); }

  public HealthBar getHealthBar() { return healthBar; }
}
