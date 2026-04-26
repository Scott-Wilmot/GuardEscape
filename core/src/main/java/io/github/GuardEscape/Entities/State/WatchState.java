package io.github.GuardEscape.Entities.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;
import io.github.GuardEscape.GuardEscape;

public class WatchState implements State {


    @Override
    public State checkTriggers(Guard guard, Player player) {
        return null;
    }

    @Override
    public void update(Guard guard, float delta) {

    }
}
