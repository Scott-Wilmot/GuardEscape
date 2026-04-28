package io.github.GuardEscape.Entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.GuardEscape.Constants.SpritePaths;
import io.github.GuardEscape.Entities.State.State;
import io.github.GuardEscape.Entities.State.WatchState;
import io.github.GuardEscape.GuardEscape;
import io.github.GuardEscape.Pathfinding.Node;

public class Guard extends BaseEntity {

    private Player player;
    private State state;
    private Node home;

    private final Vector2 defaultOrientation;
    private float defaultDegree;
    private float rotationDegree = 20f;

    public Guard(GuardEscape app,
                 Node home,
                 Player player,
                 Vector2 position,
                 Vector2 orientation,
                 float acceleration,
                 float drag) {
        super(
            SpritePaths.GUARD_SPRITE,
            new Vector2(1f, 1f),
            position,
            orientation,
            acceleration,
            drag
        );

        this.player = player;
        this.home = home;
        defaultOrientation = orientation.cpy();
        defaultDegree = orientation.angleDeg();

        state = new WatchState(app, player, this);
    }

    @Override
    public void update(float delta, Array<Rectangle> walls, int maxX, int maxY) {
        state = state.checkTriggers();
        state.update(delta);

        super.update(delta, walls, maxX, maxY);
    }

    public void applyUnitVelocity(Vector2 velocity, float delta) {
        this.velocity.add(
            acceleration * velocity.x * delta,
            acceleration * velocity.y * delta
        );
    }

    public void rotateOrientation(float delta) {
        orientation.rotateDeg(rotationDegree * delta);
    }

    public float getDefaultDegree() { return defaultDegree; }
    public void setDefaultDegree(float defaultDegree) { this.defaultDegree = defaultDegree; }
    public Vector2 getDefaultOrientation() { return defaultOrientation; }
    public float getRotationDegree() { return rotationDegree; }
    public void setRotationDegree(float rotationDegree) { this.rotationDegree = rotationDegree; }
    public Node getHome() { return home; }

}
