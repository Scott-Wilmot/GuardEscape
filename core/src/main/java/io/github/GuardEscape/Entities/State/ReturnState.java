package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;
import io.github.GuardEscape.Pathfinding.AStar;
import io.github.GuardEscape.Pathfinding.Node;

import java.util.ArrayDeque;

public class ReturnState extends State {

    private ArrayDeque<Node> path;
    private Node currentTarget;

    public ReturnState(GuardEscape app, Player player, Guard guard) {
        super(app, player, guard);

        Vector2 guardPos = guard.getPosition();
        Node currentGuardNode = app.getNodeMap().get(Node.getHash((int) guardPos.x, (int) guardPos.y));
        AStar star = new AStar(currentGuardNode, guard.getHome());
        path = star.getPath();
        currentTarget = path.pollLast();
    }

    @Override
    public State checkTriggers() {
        if (guard.isColliding(player))
            Gdx.app.exit();
        else if (hasLineOfSight())
            return new ChaseState(app, player, guard);
        else if (path.isEmpty())
            return new WatchState(app, player, guard);

        return this;
    }

    @Override
    public void update(float delta) {
        Vector2 guardPos = guard.getPosition();
        float diffX = currentTarget.getX() - guardPos.x;
        float diffY = currentTarget.getY() - guardPos.y;

        if (Math.abs(diffX) > 0.1f || Math.abs(diffY) > 0.1f) {
            guard.setOrientation(guard.getVelocity());

//            Vector2 desiredVelocity = new Vector2(currentTarget.getX(), currentTarget.getY()).sub(guard.getPosition()).nor();
//            Vector2 guardVelocity = guard.getVelocity().nor();
//            Vector2 appliedVelocity = desiredVelocity.cpy().sub(guardVelocity);

            int STEP_SIZE = 1;
            Vector2 currentTargetPos = new Vector2(currentTarget.getX(), currentTarget.getY());
            Vector2 desiredVelocity = currentTargetPos.sub(guardPos);
            Vector2 steeringForce = desiredVelocity.sub(guard.getVelocity());
            Vector2 appliedVelocity = steeringForce.nor();

            guard.applyUnitVelocity(appliedVelocity, delta);
        }
        else {
            currentTarget = path.pollLast();
        }
    }

}
