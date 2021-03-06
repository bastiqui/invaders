package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Controls {
    public static boolean isLeftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
    }

    public static boolean isRightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
    }

    public static boolean isShootPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W);
    }

    public static boolean reset() {
        return Gdx.input.isKeyPressed(Input.Keys.R);
    }

    public static boolean closeGame() {
        return Gdx.input.isKeyPressed(Input.Keys.ESCAPE);
    }
}
