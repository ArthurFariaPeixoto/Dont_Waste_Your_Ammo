package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.main.Sound;
import com.arthur.world.AStar;
import com.arthur.world.Camera;
import com.arthur.world.KeepPosition;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entitie{

    private int frames = 0, max_frames = 9, index = 0, max_index = 3;
    private double speed = 1;
    private int maskx = 4, masky = 0, maskwidht =10, maskheight = 17;
    private BufferedImage[] sprites;
    public int life = 12;
    private boolean isDamaged = false;
    private int damagedFrames = 0;
    private boolean seePlayer = false;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[4];
        sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(112+16, 16, 16, 16);
        sprites[2] = Game.spritesheet.getSprite(112+32, 16, 16, 16);
        sprites[3] = Game.spritesheet.getSprite(112, 32, 16, 16);
        setMask(maskx, masky, maskwidht, maskheight);
    }

    public void tick(){
        depth=0;
        if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 145) {
            this.seePlayer = true;
            if (seePlayer == true) {
                if (!isCollidingWithPlayer()) {
                    if (Game.rand.nextInt(100) < 60) {
                        if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(),12,16)
                                && !enemyIsColliding((int) (x + speed), this.getY())
                                || x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(),12,16)
                                && !enemyIsColliding((int) (x - speed), this.getY())
                                || y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed),12,16)
                                && !enemyIsColliding(this.getX(), (int) (y + speed))
                                || y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed),12,16)
                                && !enemyIsColliding(this.getX(), (int) (y - speed))) {

                            if (path == null || path.size() == 0) {
                                KeepPosition start = new KeepPosition((int) x / 16, (int) y / 16);
                                KeepPosition finish = new KeepPosition((int) Game.player.x / 16, (int) Game.player.y / 16);
                                path = AStar.findPath(Game.world, start, finish);
                            }
                        }
                        if (Game.rand.nextInt(100) < 10) {
                            KeepPosition start = new KeepPosition((int) x / 16, (int) y / 16);
                            KeepPosition finish = new KeepPosition((int) Game.player.x / 16, (int) Game.player.y / 16);
                            path = AStar.findPath(Game.world, start, finish);
                        }

                    }
                } else {
                    if (Game.rand.nextInt(100) < 10) {
                        Game.player.life -= Game.rand.nextInt(3);
                        Sound.Clips.hurtEffect.play();
                        Game.player.isDamaged = true;

                    }
                }
            }
        }else{
            seePlayer = false;
        }
        if(Game.rand.nextInt(100)<70){
            enemyFollowPath(path);
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
            World.generateParticles(7,(int) x,(int) y, Color.RED);
            SelfDestroy();
            Sound.Clips.enemydeath.play();
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
                    World.generateParticles(4,(int) x,(int) y, Color.RED);
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
