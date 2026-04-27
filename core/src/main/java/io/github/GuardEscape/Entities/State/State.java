package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public abstract class State {

    protected GuardEscape app;
    protected Player player;
    protected Guard guard;

    public State(GuardEscape app, Player player, Guard guard) {
        this.app = app;
        this.player = player;
        this.guard = guard;
    }

    public abstract State checkTriggers();
    public abstract void update(float delta);

    protected boolean hasLineOfSight() {
        Vector2 playerGuardVector = player.getPosition().cpy().sub(guard.getPosition());
        Vector2 guardOrientation = guard.getOrientation();
        float cosValue = playerGuardVector.nor().dot(guardOrientation.nor());

        for (Rectangle wall : app.getWallHitboxes()) {
            if (Intersector.intersectSegmentRectangle(
                guard.getPosition(),
                player.getPosition(),
                wall
            )) {
                return false;
            }
        }
        return cosValue >= 0.5f; // implicit true AND-ed with this because of no wall in way
    }

}
