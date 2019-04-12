package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Assets;

public class World {
    Space space;
    Ship ship;
    public AlienArmy alienArmy;
    BitmapFont font = new BitmapFont();

    int WORLD_WIDTH, WORLD_HEIGHT;

    public World(int WORLD_WIDTH, int WORLD_HEIGHT, float alienSpeed, int alienGoDown){
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;

        space = new Space();
        ship = new Ship(WORLD_WIDTH/2, WORLD_WIDTH, WORLD_HEIGHT);
        alienArmy = new AlienArmy(WORLD_WIDTH, WORLD_HEIGHT, alienSpeed, alienGoDown);
    }

    public void render(float delta, SpriteBatch batch, Assets assets){

        update(delta, assets, batch);

        batch.begin();
        space.render(batch);
        ship.render(batch);
        alienArmy.render(batch);
        batch.end();
    }

    void update(float delta, Assets assets, SpriteBatch batch){
        space.update(delta, assets);
        ship.update(delta, assets);
        alienArmy.update(delta, assets);

        checkCollisions(assets);
    }

    private void checkCollisions(Assets assets) {
        checkNaveInWorld();
        checkShootsInWorld();
        checkShootsToAlien(assets);
        checkShootsToShip();
        checkEndGame();
    }

    private void checkEndGame() {
        for (Alien alien: alienArmy.aliens) {
            if (alien.position.y <= (ship.position.y + 20)) {
                alienArmy.endGame = true;
                ship.endGame = true;
            }
        }
    }

    private void checkShootsToShip() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for(AlienShoot shoot: alienArmy.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());

            if (Intersector.overlaps(shootRectangle, shipRectangle)) {
                ship.damage();
                shoot.remove();
            }
        }
    }

    private void checkShootsToAlien(Assets assets) {
        for(Shoot shoot: ship.weapon.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());
            for(Alien alien: alienArmy.aliens){
                if(alien.isAlive()) {
                    Rectangle alienRectangle = new Rectangle(alien.position.x, alien.position.y, alien.frame.getRegionWidth(), alien.frame.getRegionHeight());

                    if (Intersector.overlaps(shootRectangle, alienRectangle)) {
                        alien.kill();
                        shoot.remove();
                        assets.aliendieSound.play();
                    }
                }
            }
        }
    }

    private void checkShootsInWorld() {
        for(Shoot shoot: ship.weapon.shoots){
            if(shoot.position.y > WORLD_HEIGHT){
                shoot.remove();
            }
        }

        for(AlienShoot shoot: alienArmy.shoots){
            if(shoot.position.y < 0){
                shoot.remove();
            }
        }
    }

    private void checkNaveInWorld() {
        if(ship.position.x > WORLD_WIDTH-32){
            ship.position.x = WORLD_WIDTH-32;
        } else if(ship.position.x < 0){
            ship.position.x = 0;
        }
    }
}
