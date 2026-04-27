package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class WatchState extends State {

    public WatchState(GuardEscape app, Player player, Guard guard) {
        super(app, player, guard);
        guard.setOrientation(guard.getDefaultOrientation()); // Why is defaultOrientation null????????????
//        guard.setOrientation(new Vector2(0f, -1f));
    }

    @Override
    public State checkTriggers() {
        if (guard.isColliding(player))
            Gdx.app.exit();
        else if (hasLineOfSight())
            return new ChaseState(app, player, guard);

        return this;    // Guard did not see player
    }

    @Override
    public void update(float delta) {
        float degreeDiff = Math.abs(guard.getOrientation().angleDeg() - guard.getDefaultDegree());
        if (degreeDiff >= 75)
            guard.setRotationDegree(-guard.getRotationDegree());
        guard.rotateOrientation(delta);
    }

}
