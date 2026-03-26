package io.github.GuardEscape.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.State.State;
import io.github.GuardEscape.Entities.State.WatchState;
import io.github.GuardEscape.GuardEscape;
import io.github.GuardEscape.Pathfinding.Node;

public class Guard {

    Sprite guard;
    Rectangle hitbox;
    Node home;

    private State state;
    Vector2 defaultOrientation;
    Vector2 orientation;

    float centerDegree;
    float rotationDegree;

    float speed = 4f;

    public Guard(float initX, float initY, GuardEscape parent, Node home) {
        guard = new Sprite(new Texture("tileMaps\\guard.png"));
        guard.setPosition(initX, initY);
        guard.setSize(1f, 1f);
        hitbox = new Rectangle(initX, initY, guard.getWidth(), guard.getHeight());
        state = new WatchState(parent);
        orientation = new Vector2(0, -1);
        defaultOrientation = orientation.cpy();
        this.home = home;

        centerDegree = orientation.angleDeg();
        rotationDegree = 20;
    }

    public void update(Player player, float delta) {
        state = state.checkTriggers(this, player);
        state.update(this, delta);
    }

    public void step(float delta) {
        translateX(getOrientX() * speed * delta);
        translateY(getOrientY() * speed * delta);
    }
    private void translateX(float value) {
        guard.translateX(value);
        hitbox.setX(guard.getX());
    }
    private void translateY(float value) {
        guard.translateY(value);
        hitbox.setY(guard.getY());
    }

    public void rotateOrient(float degrees, float delta) { orientation.rotateDeg(degrees * delta); }
    public void printOrientDegree() { System.out.println(orientation.angleDeg()); }

    public Sprite getSprite() { return guard; }
    public Rectangle getHitbox() { return hitbox; }
    public Node getHome() { return home; }

    public float getCenterDegree() { return centerDegree; }
    public void setCenterDegree(float degree) { centerDegree = degree; }
    public void resetCenterDegree() { centerDegree = defaultOrientation.angleDeg(); }

    public float getCurrentDegree() { return orientation.angleDeg(); }

    public float getRotationDegree() { return rotationDegree; }
    public void setRotationDegree(float rotationDegree) { this.rotationDegree = rotationDegree; }

    public float getX() { return guard.getX(); }
    public float getY() { return guard.getY(); }
    public float getOrientX() { return orientation.x; }
    public float getOrientY() { return orientation.y; }
    public void setOrientation(float x, float y) {
        float magnitude = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        float unitX = x / magnitude;
        float unitY = y / magnitude;
        orientation.set(unitX, unitY);
    }
    public Vector2 getOrientation() { return orientation; }
    public void resetOrientation() {
        orientation = defaultOrientation.cpy();
    }

    public void setX(float x) { guard.setX(x); }
    public void setY(float y) { guard.setY(y); }
    public void setPosition(float x, float y) { guard.setPosition(x, y); }

    public float getWidth() { return guard.getWidth(); }
    public float getHeight() { return guard.getHeight(); }

}
