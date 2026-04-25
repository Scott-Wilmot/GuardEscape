package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;
import io.github.GuardEscape.Pathfinding.AStar;
import io.github.GuardEscape.Pathfinding.Node;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class ReturnState implements State {

    private GuardEscape application;
    private ArrayDeque<Node> path;
    private Node currentTarget;

    public ReturnState(GuardEscape application) {
        this.application = application;

        AStar star = new AStar(application.getGuardPosition(), application.getGuardHome());
        path = star.getPath();
        currentTarget = path.pollLast();
        System.out.println("NEW RETURN STATE, PATH SIZE = " + path.size());
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
        else if (path.isEmpty()) {
            guard.resetOrientation();
            guard.resetCenterDegree();
            return new WatchState(application);
        }
        return this;
    }

    @Override
    public void update(Guard guard, float delta) {
        float diffX = currentTarget.getX() - guard.getX();
        float diffY = currentTarget.getY() - guard.getY();

        if (Math.abs(diffX) > 0.1 || Math.abs(diffY) > 0.1) {
            guard.setOrientation(diffX, diffY);
        }
        else {
            System.out.println("DIFF: " + diffX + ", " + diffY);
            System.out.println("ARRIVED AT: " + currentTarget.getX() + ", " + currentTarget.getY());
            System.out.println("CURRENT: " + guard.getX() + ", " + guard.getY());
            currentTarget = path.pollLast();
            System.out.println("NEW TARGET: " + currentTarget.getX() + ", " + currentTarget.getY());
            guard.setOrientation(currentTarget.getX(), currentTarget.getY());
        }

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
}
