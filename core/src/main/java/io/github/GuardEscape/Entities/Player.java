package io.github.GuardEscape.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    Sprite player;
    Rectangle hitbox;

    public Player(float initX, float initY) {
        player = new Sprite(new Texture("tileMaps\\character.png"));
        player.setPosition(initX, initY);
        player.setSize(1f, 1f);
        hitbox = new Rectangle(initX, initY, player.getWidth(), player.getHeight());
    }

    public Sprite getSprite() {
        return player;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void translateX(float value) {
        player.translateX(value);
        hitbox.setX(player.getX());
    }

    public void translateY(float value) {
        player.translateY(value);
        hitbox.setY(player.getY());
    }

    public float getX() { return player.getX(); }
    public float getY() { return player.getY(); }
    public void setX(float x) { player.setX(x); }
    public void setY(float y) { player.setY(y); }
    public void setPosition(float x, float y) { player.setPosition(x, y); }

    public float getWidth() { return player.getWidth(); }
    public float getHeight() { return player.getHeight(); }

}
