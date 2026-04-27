package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class DeathState extends State {

    public DeathState(GuardEscape app, Player player, Guard guard) {
        super(app, player, guard);
    }

    @Override
    public State checkTriggers() {
        return this;
    }

    @Override
    public void update(float delta) {
        Gdx.app.exit();
    }
}
