package io.github.GuardEscape.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseEntity {

    // Entity logic and rendering
    Sprite sprite;
    Rectangle body;

    // Entity body characteristics
    Vector2 position;
    Vector2 velocity;
    Vector2 orientation;

    float speed;
    float drag;

    public BaseEntity(Sprite sprite,
                      Rectangle body,
                      Vector2 position,
                      Vector2 orientation,
                      float speed,
                      float drag)
    {
        this.sprite = sprite;
        this.body = body;
        this.position = position;
        this.orientation = orientation;
        this.speed = speed;
        this.drag = drag;

        velocity = new Vector2(0, 0);   // All entities should start with zero speed
    }

    public void render(Batch batch) {
        sprite.draw(batch);
    }

    public void update(float delta) {
        sprite.setPosition(body.getX(), body.getY());
    }

    public boolean overlaps(Rectangle wall) {
        return body.overlaps(wall);
    }

    public void collisionCorrection(Rectangle wall) {
        float hitboxLeft = body.getX();
        float hitboxRight = body.getX() + body.getWidth();
        float hitboxBottom = wall.getY();
        float hitboxTop = wall.getY() + body.getHeight();

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

        if (minOverlap == overlapLeft)
            body.setX(wallLeft - body.getWidth());
        else if (minOverlap == overlapRight)
            body.setX(wallRight);
        else if (minOverlap == overlapBottom)
            body.setY(wallBottom - body.getHeight());
        else if (minOverlap == overlapTop)
            body.setY(wallTop);
    }

}
