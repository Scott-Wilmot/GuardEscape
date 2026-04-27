package io.github.GuardEscape.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.GuardEscape.Constants.SpritePaths;

public class Player extends BaseEntity {

    public Player(Vector2 position,
                  Vector2 orientation,
                  float acceleration,
                  float drag) {
        super(
            SpritePaths.PLAYER_SPRITE,
            new Vector2(1f, 1f),
            position,
            orientation,
            acceleration,
            drag
        );
    }

    @Override
    public void update(float delta, Array<Rectangle> walls, int maxX, int maxY) {
        input(delta);

        super.update(delta, walls, maxX, maxY);
    }

    private void input(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            velocity.add(-acceleration * delta, 0f);
        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            velocity.add(acceleration * delta, 0f);

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            velocity.add(0f, acceleration * delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.S))
            velocity.add(0f, -acceleration * delta);
    }

}
