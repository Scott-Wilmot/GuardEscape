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


    @Override
    public State checkTriggers(Guard guard, Player player) {
        return null;
    }

    @Override
    public void update(Guard guard, float delta) {

    }
}
