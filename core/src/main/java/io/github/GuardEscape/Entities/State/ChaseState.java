package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class ChaseState extends State {

    public ChaseState(GuardEscape app, Player player, Guard guard) {
        super(app, player, guard);
    }

    @Override
    public State checkTriggers() {
        if (guard.isColliding(player))
            Gdx.app.exit();
        else if (!hasLineOfSight()) {
            return new ReturnState(app, player, guard);
        }

        return this;
    }

    @Override
    public void update(float delta) {
        guard.setOrientation(player.getPosition().cpy().sub(guard.getPosition()));

        int STEP_SIZE = 3;
        Vector2 futurePlayerPos = player.getPosition().cpy().add(player.getVelocity().cpy().scl(STEP_SIZE));
        Vector2 desiredVelocity = futurePlayerPos.sub(guard.getPosition());
        Vector2 steeringForce = desiredVelocity.sub(guard.getVelocity());
        Vector2 appliedVelocity = steeringForce.nor();

        guard.applyUnitVelocity(appliedVelocity, delta);
    }

}
