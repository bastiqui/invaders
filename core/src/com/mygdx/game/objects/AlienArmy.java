package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Timer;

import java.util.Random;

public class AlienArmy {

    int x, y, maxX, downSpeed;

    float speed;
    boolean bajar;

    public Array<Alien> aliens;
    Array<AlienShoot> shoots;

    Timer moveTimer, shootTimer;
    Random random = new Random();
    boolean endGame = false;

    AlienArmy(int WORLD_WIDTH, int WORLD_HEIGHT, float speed, int downSpeed){

        this.x = 0;
        this.y = WORLD_HEIGHT-30;
        this.maxX = 65;
        this.speed = speed;
        this.downSpeed = downSpeed;

        aliens = new Array<Alien>();
        shoots = new Array<AlienShoot>();

        moveTimer = new Timer(0.8f);
        shootTimer = new Timer(random.nextFloat()%5+1);

        positionAliens();
    }


    void render(SpriteBatch batch){
        for(Alien alien: aliens) {
            alien.render(batch);
        }

        for (AlienShoot shoot: shoots) {
            shoot.render(batch);
        }
    }

    public void update(float delta, Assets assets) {
        moveTimer.update(delta);
        shootTimer.update(delta);

        if (!endGame) {

            move();
            shoot(assets);

            for (Alien alien : aliens) {
                alien.update(delta, assets);
            }

            for (AlienShoot shoot : shoots) {
                shoot.update(delta, assets);
            }


            removeDeadAliens();
            removeShoots();
        }
    }


    void positionAliens(){
        for (int i = 0; i < 5; i++) {  // fila
            for (int j = 0; j < 11; j++) {  // columna
                aliens.add(new Alien(j*30 + 10, y - i*12));
            }
        }
    }


    void move() {
        if (moveTimer.check()){
            x += speed;

            if(x > maxX){
                x = maxX;
                speed *= -1;
                y -= downSpeed;
                bajar = true;
            } else if(x < 0){
                x = 0;
                speed *= -1;
                y -= downSpeed;
                bajar = true;
            }

            for (Alien alien : aliens) {
                alien.position.x += speed;
                if (bajar) {
                    alien.position.y -= downSpeed;
                }
            }
            bajar = false;
        }
    }

    void shoot(Assets assets){
        if(shootTimer.check() && !aliens.isEmpty()){
            int alienNum = random.nextInt(aliens.size);

            Alien alien = aliens.get(alienNum);

            shoots.add(new AlienShoot(new Vector2(alien.position)));

            assets.alienSound.play();

            shootTimer.set(random.nextFloat()%5+1);
        }
    }

    private void removeDeadAliens() {
        Array<Alien> aliensToRemove = new Array<Alien>();
        for(Alien alien: aliens){
            if(alien.state == Alien.State.DEAD){
                aliensToRemove.add(alien);
            }
        }

        for (Alien alien: aliensToRemove){
            aliens.removeValue(alien, true);
        }
    }
    public void removeShoots(){
        Array<AlienShoot> shootsToRemove = new Array<AlienShoot>();
        for(AlienShoot shoot:shoots){
            if(shoot.state == AlienShoot.State.TO_REMOVE){
                shootsToRemove.add(shoot);
            }
        }

        for (AlienShoot shoot: shootsToRemove){
            shoots.removeValue(shoot, true);
        }
    }

}
