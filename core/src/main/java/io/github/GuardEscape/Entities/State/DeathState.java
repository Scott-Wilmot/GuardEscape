package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;

public class DeathState implements State {
    @Override
    public State checkTriggers(Guard guard, Player player) {
        return this;
    }

    @Override
    public void update(Guard guard, float delta) {
        Gdx.app.exit();
    }
}
