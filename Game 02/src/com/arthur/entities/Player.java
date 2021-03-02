package com.arthur.entities;

import com.arthur.graficos.Spritesheet;
import com.arthur.main.Game;
import com.arthur.main.Sound;
import com.arthur.world.Camera;
import com.arthur.world.WallTile;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entitie{

    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage[] upPlayer;
    private BufferedImage[] downPlayer;
    private BufferedImage damagedPlayer;

    private boolean hasGun = false;
    public boolean shoot = false;



    private int frames = 0, max_frames = 11, index = 0, max_index = 3;
    private boolean moved = false;
    public boolean right, left, up, down;
    public double speed = 1.2;
    public static int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
    public static int dir = right_dir;
    public static int maskx = 3, masky = 0, maskwidht = 9, maskheight = 16;
    public double life = 100, max_life = 100;
    public int bullet = 0;
    public boolean isDamaged = false;
    private int damagedFrames = 0;


    public Player(int x, int y, int width, int height, BufferedImage sprite){
        super(x, y, width, height, sprite);
        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        upPlayer = new BufferedImage[4];
        downPlayer = new BufferedImage[4];
        damagedPlayer = Game.spritesheet.getSprite(0, 16, 16, 16);
        for (int i = 0; i < 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            upPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 32, 16, 16);
        }
        for (int i = 0; i < 4; i++) {
            downPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 48, 16, 16);
        }
    }
    public void tick(){
        moved = false;
        if(right && World.isFree((int)(x+speed), this.getY())){
            moved = true;
            dir = right_dir;
            x+=speed;
        }
        else if (left && World.isFree((int)(x-speed), this.getY())){
            moved = true;
            dir = left_dir;
            x-=speed;
        }
        if (up && World.isFree(this.getX(), (int)(y-speed))){
            moved = true;
            dir = up_dir;
            y-=speed;
        }
        else if(down && World.isFree(this.getX(), (int)(y+speed))){
            dir = down_dir;
            moved = true;
            y+=speed;
        }
        if (moved){
            frames++;
            if(frames == max_frames){
                frames = 0;
                index++;

                if(index > max_index){
                    index = 0;
                    Sound.move.loop();

                }
            }
        }
        CollisionLifePack();
        CollisionBullet();
        CollisionGun();
        CollisionBoot();
        CollisionBigAmmo();

        if(shoot){
            shoot = false;
            if(hasGun && bullet>0) {
                Sound.shoot.play();
                bullet--;
                int directionX = 0;
                int directionY = 0;
                int px = 0;
                int py = 0;
                if (dir == right_dir) {
                    directionX = +1;
                    px = 13;
                    py = 5;
                } else if (dir == left_dir) {
                    directionX = -1;
                    px = -3;
                    py = 5;
                }
                if (dir == up_dir) {
                    directionY = -1;
                    px = 10;
                    py = 5;
                } else if (dir == down_dir) {
                    directionY = +1;
                    px = 10;
                    py = 12;
                }
                BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, directionX, directionY);
                Game.bullets.add(bullet);
            }
        }
        if(isDamaged){
            damagedFrames++;

            if(damagedFrames == 10){
                damagedFrames = 0;
                isDamaged = false;
            }
        }
        if (life <= 0){
            life = 0;
            Game.gameState = "Game over";
            Sound.gameover.play();
        }

        Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
    }
    public void CollisionGun() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entitie atual = Game.entities.get(i);
            if (atual instanceof Weapon) {
                if (Entitie.isColliding(this, atual)) {
                    Sound.reload.play();
                    hasGun = true;
                    bullet+=4;
                    Game.entities.remove(atual);
                }
            }
        }
    }
    public void CollisionBullet() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entitie atual = Game.entities.get(i);
            if (atual instanceof Bullet) {
                if (Entitie.isColliding(this, atual)) {
                    Sound.reload.play();
                    bullet+=4;
                    Game.entities.remove(atual);
                }
            }
        }
    }
    public void CollisionLifePack(){
        for(int i = 0; i <Game.entities.size(); i++){
            Entitie atual = Game.entities.get(i);
            if(atual instanceof Life){
                if(Entitie.isColliding(this, atual)){
                    Sound.life.play();
                    life+=10;
                    if(life >= 100){
                        life = 100;
                    }
                    Game.entities.remove(atual);
                }

            }
        }
    }
    public void CollisionBoot(){
        for(int i = 0; i <Game.entities.size(); i++){
            Entitie atual = Game.entities.get(i);
            if(atual instanceof Boot){
                if(Entitie.isColliding(this, atual)){
                    speed += 0.9;
                    Game.entities.remove(atual);
                }
            }
        }
    }
    public void CollisionBigAmmo() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entitie atual = Game.entities.get(i);
            if (atual instanceof BigAmmo) {
                if (Entitie.isColliding(this, atual)) {
                    Sound.reload.play();
                    bullet+=32;
                    Game.entities.remove(atual);
                }
            }
        }
    }
    public void render(Graphics g) {
        if (!isDamaged) {
            if (dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    g.drawImage(WEAPONRIGHT_EN, getX()+8 - Camera.x, getY()+4 - Camera.y,3*Game.SCALE, 3*Game.SCALE, null);
                }
            } else if (dir == left_dir) {
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    g.drawImage(WEAPONLEFT_EN, getX()-2 - Camera.x, getY()+4 - Camera.y,3*Game.SCALE, 3*Game.SCALE, null);
                }
            } else if (dir == up_dir) {
                g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    g.drawImage(WEAPONUP_EN, getX()+8 - Camera.x, getY()+5 - Camera.y,3*Game.SCALE, 3*Game.SCALE, null);
                }
            } else if (dir == down_dir) {
                g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    g.drawImage(WEAPONDOWN_EN, getX()+8 - Camera.x, getY()+7 - Camera.y, 3*Game.SCALE, 3*Game.SCALE, null);
                }
            }


        }
        else if(isDamaged) {
            if (hasGun) {
                g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
                if (dir == right_dir) {
                    g.drawImage(WEAPONRIGHTDAMAGED, getX() + 8 - Camera.x, getY() + 4 - Camera.y, 3 * Game.SCALE, 3 * Game.SCALE, null);
                } else if (dir == left_dir) {
                    g.drawImage(WEAPONLEFTDAMAGED, getX() - 2 - Camera.x, getY() + 4 - Camera.y, 3 * Game.SCALE, 3 * Game.SCALE, null);
                } else if (dir == up_dir) {
                    g.drawImage(WEAPONUPDAMAGED, getX() + 8 - Camera.x, getY() + 5 - Camera.y, 3 * Game.SCALE, 3 * Game.SCALE, null);
                } else if (dir == down_dir) {
                    g.drawImage(WEAPONDOWNDAMAGED, getX() + 8 - Camera.x, getY() + 7 - Camera.y, 3 * Game.SCALE, 3 * Game.SCALE, null);
                }
            } else {
                g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
            }
        }
            /*Visualizador de colisao
            super.render(g);
            g.fillRect(this.getX()+maskx - Camera.x, this.getY()+masky - Camera.y, maskwidht, maskheight);
            /**/
    }
}
