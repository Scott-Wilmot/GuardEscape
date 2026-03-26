package io.github.GuardEscape.Entities.State;

import io.github.GuardEscape.Entities.Guard;
import io.github.GuardEscape.Entities.Player;

public interface State {
    public State checkTriggers(Guard guard, Player player);
    public void update(Guard guard, float delta);
}
