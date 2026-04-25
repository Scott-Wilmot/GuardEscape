package io.github.GuardEscape.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class BaseEntity {

    // Entity logic and rendering
    protected Sprite sprite;
    protected Rectangle body;

    // Entity body characteristics
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 orientation;

    protected float acceleration;
    protected float drag;

    public BaseEntity(String spritePath,
                      Vector2 dimensions,
                      Vector2 position,
                      Vector2 orientation,
                      float speed,
                      float drag)
    {
        sprite = generateSprite(spritePath, dimensions, position);
        body = new Rectangle(position.x, position.y, dimensions.x, dimensions.y);
        this.position = position;
        this.orientation = orientation;
        this.acceleration = speed;
        this.drag = drag;

        velocity = new Vector2(0, 0);   // All entities should start with zero speed
    }

    public void render(Batch batch) {
        sprite.setPosition(body.getX(), body.getY());
        sprite.draw(batch);
    }

    public void update(float delta, Array<Rectangle> walls, int maxX, int maxY) {
        velocity.scl(drag);

        // X direction update
        updateHitbox(velocity.x * delta, 0f);
        for (Rectangle wall : walls) {
            if (body.overlaps(wall)) {
                // Player hitting wall from the left
                if (velocity.x > 0)
                    body.setX(wall.getX() - body.getWidth());
                // Player hitting wall from left
                else if (velocity.x < 0)
                    body.setX(wall.getX() + wall.getWidth());

                velocity.x = -velocity.x * 0.5f;
                position.x = body.x;
            }
        }

        // Y direction update
        updateHitbox(0f, velocity.y * delta);
        for (Rectangle wall : walls) {
            if (body.overlaps(wall)) {
                // Player contacts wall from bottom
                if (velocity.y > 0)
                    body.setY(wall.getY() - body.getHeight());
                // Player contacts wall from top
                else if (velocity.y < 0)
                    body.setY(wall.getY() + wall.getHeight());

                velocity.y = -velocity.y * 0.5f;
                position.y = body.y;
            }
        }

        // World bound checks
        if (body.getX() < 0) {
            body.setX(0);
            position.x = 0;
        }
        else if (body.getX() > maxX - body.getWidth()) {
            body.setX(maxX - body.getWidth());
            position.x = body.getX();
        }

        if (body.getY() < 0) {
            body.setY(0);
            position.y = 0;
        }
        else if (body.getY() > maxY - body.getHeight()) {
            body.setY(maxY - body.getHeight());
            position.y = body.getY();
        }
    }

    public boolean overlaps(Rectangle wall) {
        return body.overlaps(wall);
    }

    public void collisionCorrection(Rectangle wall) {
        float hitboxLeft = body.getX();
        float hitboxRight = body.getX() + body.getWidth();
        float hitboxBottom = body.getY();
        float hitboxTop = body.getY() + body.getHeight();

        float wallLeft = wall.getX();
        float wallRight = wall.getX() + wall.getWidth();
        float wallBottom = wall.getY();
        float wallTop = wall.getY() + wall.getHeight();

        // Denotes the overlap on the side of the wall being claimed
        // e.g. "overlapRight" is the overlap on the right side of the wall
        float overlapLeft = hitboxRight - wallLeft;
        float overlapRight = wallRight - hitboxLeft;
        float overlapBottom = hitboxTop - wallBottom;
        float overlapTop = wallTop - hitboxBottom;

        float minOverlap = Math.min(
            Math.min(overlapLeft, overlapRight),
            Math.min(overlapBottom, overlapTop)
        );

        if (minOverlap == overlapLeft) {
            body.setX(wallLeft - body.getWidth());
            velocity.x = 0f;
        }
        else if (minOverlap == overlapRight) {
            body.setX(wallRight);
            velocity.x = 0f;
        }
        else if (minOverlap == overlapBottom) {
            body.setY(wallBottom - body.getHeight());
            velocity.y = 0f;
        }
        else if (minOverlap == overlapTop) {
            body.setY(wallTop);
            velocity.y = 0f;
        }
    }

    private void updateHitbox(float dx, float dy) {
        position.x += dx;
        body.setX(position.x);

        position.y += dy;
        body.setY(position.y);
    }

    private Sprite generateSprite(String spritePath, Vector2 dimensions, Vector2 position) {
        Texture texture = new Texture(spritePath);
        Sprite sprite = new Sprite(texture);
        sprite.setSize(dimensions.x, dimensions.y);
        return sprite;
    }

}
