package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class ChaseState implements State {

    private GuardEscape application;

    public ChaseState(GuardEscape application) {
        this.application = application;
    }

    @Override
    public State checkTriggers(Guard guard, Player player) {
        float diffX = player.getX() - guard.getX();
        float diffY = player.getY() - guard.getY();
        float diffMagnitude = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
        float dot = ((diffX / diffMagnitude) * guard.getOrientX()) + ((diffY / diffMagnitude) * guard.getOrientY());
        guard.setOrientation(diffX, diffY);

        if (guard.getHitbox().overlaps(player.getHitbox())) {
            return new DeathState();
        }
        else if (checkWallCollision(guard, player) || checkLOS(dot)) {
            guard.setCenterDegree(guard.getCurrentDegree());
            return new ReturnState(application);
        }

        return this;
    }

    @Override
    public void update(Guard guard, float delta) {
        guard.step(delta);
    }

    public boolean checkWallCollision(Guard guard, Player player) {
        for (Rectangle hitbox : application.wallHitboxes) {
            if (Intersector.intersectSegmentRectangle(
                guard.getX(),
                guard.getY(),
                player.getX(),
                player.getY(),
                hitbox
            )) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLOS(float dot) {
        return dot <= 0.5;
    }
}
