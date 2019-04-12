package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

import static com.mygdx.game.objects.Ship.State.*;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT, DYING, DEAD
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;

    TextureRegion frame;

    Weapon weapon;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        state = IDLE;
        stateTime = 0;

        weapon = new Weapon();
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            case DYING:
                frame = assets.navedying.getKeyFrame(stateTime, true);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);
        weapon.render(batch);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;

        if (state != DYING) {
            if (Controls.isLeftPressed()) {
                moveLeft();
            } else if (Controls.isRightPressed()) {
                moveRight();
            } else if (state != DYING) {
                idle();
            }

            if(Controls.isShootPressed()) {
                shoot();
                assets.shootSound.play();
            }
        }

        if (state == DYING) {
            if (assets.navedying.isAnimationFinished(stateTime)) {
                state = DEAD;
            }
        }

        if (state == DEAD) {
            state = IDLE;
            position.x = 0;
            stateTime = 0;
        }

        setFrame(assets);

        weapon.update(delta, assets);
    }

    void idle(){
        state = IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = RIGHT;
    }

    void shoot(){
        state = SHOOT;
        weapon.shoot(position.x +16);
    }

    void damage() {
        state = DYING;
        stateTime = 0;
    }
}
