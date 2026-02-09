package io.github.some_example_name.tests.unit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.some_example_name.engine.collision.Collidable;
import io.github.some_example_name.engine.collision.CollisionManager;

public class TestCollision extends ApplicationAdapter {
    private ShapeRenderer shape;
    private CollisionManager collisionManager;
    private Circle circle;
    private Triangle triangle;

    @Override
    public void create() {
        shape = new ShapeRenderer();

        circle = new Circle(100, 300, 40, Color.RED);
        triangle = new Triangle(300, 200, 60, Color.GREEN);

        collisionManager = new CollisionManager();
        collisionManager.addCollidable(circle);
        collisionManager.addCollidable(triangle);

        System.out.println("=== Collision Test Started ===");
        System.out.println("Use ARROW keys to move the red circle");
        System.out.println("Collide with the green triangle");
        System.out.println("Triangle will disappear on collision");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        float delta = Gdx.graphics.getDeltaTime();

        circle.update(delta);

        collisionManager.update();

        if (!triangle.isActive()) {
            collisionManager.removeCollidable(triangle);
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        circle.draw(shape);
        triangle.draw(shape);
        shape.end();
    }

    @Override
    public void dispose() {
        shape.dispose();
    }

    // Inner class: Circle
    class Circle implements Collidable {
        private float x, y;
        private float radius;
        private Color color;
        private boolean active = true;
        private float speed = 200f;
        private Rectangle bounds;

        public Circle(float x, float y, float radius, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
            this.bounds = new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
        }

        public void update(float delta) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y -= speed * delta;
            }
        }

        public void draw(ShapeRenderer shape) {
            if (active) {
                shape.setColor(color);
                shape.circle(x, y, radius);
            }
        }

        @Override
        public Rectangle getBounds() {
            bounds.setPosition(x - radius, y - radius);
            return bounds;
        }

        @Override
        public void onCollision(Collidable other) {
            if (other instanceof Triangle) {
                System.out.println("Circle hit triangle!");
            }
        }

        public boolean isActive() {
            return active;
        }
    }

    // Inner class: Triangle
    class Triangle implements Collidable {
        private float x, y;
        private float size;
        private Color color;
        private boolean active = true;
        private Rectangle bounds;

        public Triangle(float x, float y, float size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.bounds = new Rectangle(x, y, size, size);
        }

        public void draw(ShapeRenderer shape) {
            if (active) {
                shape.setColor(color);
                shape.triangle(
                        x, y,
                        x + size, y,
                        x + size / 2, y + size);
            }
        }

        @Override
        public Rectangle getBounds() {
            bounds.setPosition(x, y);
            return bounds;
        }

        @Override
        public void onCollision(Collidable other) {
            if (other instanceof Circle) {
                System.out.println("Triangle hit by circle! Triangle disappearing...");
                active = false;
            }
        }

        public boolean isActive() {
            return active;
        }
    }
}