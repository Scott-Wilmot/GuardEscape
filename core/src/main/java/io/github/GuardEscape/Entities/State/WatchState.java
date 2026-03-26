package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class WatchState implements State {

    private GuardEscape application;

    public WatchState(GuardEscape application) {
        this.application = application;

    }

    @Override
    public State checkTriggers(Guard guard, Player player) {
        float diffX = player.getX() - guard.getX();
        float diffY = player.getY() - guard.getY();
        float diffMagnitude = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
        float dot = ((diffX / diffMagnitude) * guard.getOrientX()) + ((diffY / diffMagnitude) * guard.getOrientY());

        if (guard.getHitbox().overlaps(player.getHitbox())) {
            return new DeathState();
        }
        else if (dot >= 0.5 && !checkWallCollision(guard, player)) {
            return new ChaseState(application);
        }

        return this;
    }

    @Override
    public void update(Guard guard, float delta) {
        float degreeDiff = Math.abs(guard.getCenterDegree() - guard.getCurrentDegree());
        if (degreeDiff >= 75) { guard.setRotationDegree(-guard.getRotationDegree()); }
        guard.rotateOrient(guard.getRotationDegree(), delta);
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
}
