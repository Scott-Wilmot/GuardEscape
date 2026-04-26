package io.github.GuardEscape.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.GuardEscape.Entities.State.State;
import io.github.GuardEscape.Entities.State.WatchState;
import io.github.GuardEscape.GuardEscape;
import io.github.GuardEscape.Pathfinding.Node;

public class Guard extends BaseEntity {

    public Guard(String spritePath,
                 Vector2 dimensions,
                 Vector2 position,
                 Vector2 orientation,
                 float speed,
                 float drag) {
        super(
            spritePath,
            dimensions,
            position,
            orientation,
            speed,
            drag
        );
    }

    @Override
    public void update(float delta, Array<Rectangle> walls, int maxX, int maxY) {
        velocity.add(delta, 0f);

        super.update(delta, walls, maxX, maxY);
    }

}
