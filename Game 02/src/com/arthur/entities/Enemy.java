package com.arthur.entities;

import com.arthur.graficos.Spritesheet;
import com.arthur.main.Game;
import com.arthur.world.Camera;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends Entitie{

    private int frames = 0, max_frames = 9, index = 0, max_index = 3;
    private double speed = 1;
    private int maskx = 3, masky = 0, maskwidht = 10, maskheight = 17;
    private BufferedImage[] sprites;
    public int life = 10;
    private boolean isDamaged = false;
    private int damagedFrames = 0;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[4];
        sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(112+16, 16, 16, 16);
        sprites[2] = Game.spritesheet.getSprite(112+32, 16, 16, 16);
        sprites[3] = Game.spritesheet.getSprite(112, 32, 16, 16);
    }
    public void tick(){

        if (!this.isCollidingWithPlayer()) {
            ///* COLISAO ENTRE INIMIGOS (BUG DE COLISÃO AO MORRER)
            if (Game.rand.nextInt(100) < 60) {
                if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
                        && !isColliding((int) (x + speed), this.getY())) {
                    x += speed;
                } else if (x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
                        && !isColliding((int) (x - speed), this.getY())) {
                    x -= speed;
                }
                if (y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))
                        && !isColliding(this.getX(), (int) (y + speed))) {
                    y += speed;
                } else if (y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))
                        && !isColliding(this.getX(), (int) (y - speed))) {
                    y -= speed;
                }
            }
                /**/
           /* MOVIMENTAÇÃO ALEATORIA DOS INIMIGOS (SEM COLISÃO ENTRE ELES)
             if(Game.rand.nextInt(100) < 60) {
                if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())){
                    x += speed;
                } else if (x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())){
                    x -= speed;
                }
                if (y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed))){
                    y += speed;
                } else if (y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed))){
                    y -= speed;
                }
            }
            /**/
        }
        else {
            if (Game.rand.nextInt(100) < 8) {
                Game.player.life -= Game.rand.nextInt(3);
                Game.player.isDamaged = true;

                if (Game.player.life <= 0) {

                    Game.entities = new ArrayList<Entitie>();
                    Game.enemies = new ArrayList<Enemy>();
                    Game.spritesheet = new Spritesheet("/spritesheet.png");
                    Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
                    Game.entities.add(Game.player);
                    Game.world = new World("/map.png");
                    return;
                }
            }
        }

            frames++;
            if(frames == max_frames){
                frames = 0;
                index++;
                if(index > max_index){
                    index = 0;

                }
            }
            CollidingBullet();
            if(life<=0) {
                SelfDestroy();
                return;
            }
            if(isDamaged) {
                damagedFrames++;
                if (damagedFrames == 10) {
                    damagedFrames = 0;
                    isDamaged = false;
                }
            }
    }
    public void SelfDestroy(){
        Game.entities.remove(this);
        Game.enemies.remove(this);
    }
    public void CollidingBullet(){
        for(int i=0; i<Game.bullets.size(); i++){
            Entitie e = Game.bullets.get(i);
            if(e instanceof BulletShoot){
                if(Entitie.isColliding(this, e)){
                    life-=4;
                    isDamaged = true;
                    Game.bullets.remove(i);
                    return;
                }
            }
        }
    }
    public boolean isCollidingWithPlayer(){
        Rectangle enemyCurrent = new Rectangle(getX()+maskx, getY()+masky, maskwidht, maskheight);
        Rectangle Player = new Rectangle(Game.player.getX() + Game.player.maskx, Game.player.getY() + Game.player.masky, Game.player.maskwidht, Game.player.maskheight);

        return enemyCurrent.intersects(Player);
    }
    public boolean isColliding(int xnext, int ynext){
        Rectangle enemyCurrent = new Rectangle(xnext+maskx, ynext+masky, maskwidht, maskheight);
        for(int i = 0; i < Game.enemies.size(); i++){
            Enemy e = Game.enemies.get(i);
            if(e == this){
                continue;
            }
            Rectangle targetEnemy = new Rectangle(e.getX()+maskx, e.getY()+masky, maskwidht, maskheight);
                if (enemyCurrent.intersects(targetEnemy)) {
                    return true;
                }

        }
        return false;
    }
    public void render(Graphics g){
        if(!isDamaged) {
            g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }else{
            g.drawImage(Entitie.ENEMYDAMAGE, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    /*Visualizador de colisao
       super.render(g);
        g.fillRect(this.getX()+maskx - Camera.x, this.getY()+masky - Camera.y, maskwidht, maskheight);
    /**/
    }

}
