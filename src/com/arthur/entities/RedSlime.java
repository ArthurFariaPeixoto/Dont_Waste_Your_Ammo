package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.main.Sound;
import com.arthur.world.AStar;
import com.arthur.world.Camera;
import com.arthur.world.KeepPosition;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RedSlime extends Entitie{
    private int frames = 0, max_frames = 11, index = 0, max_index = 4;
    private double speed = 1;
    private int maskx = 2, masky = 7, maskwidht =12, maskheight = 9;
    private BufferedImage[] sprites;
    public int life = 10;
    private boolean isDamaged = false;
    private int damagedFrames = 0;
    private boolean seePlayer = false;
    public RedSlime(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);

        sprites = new BufferedImage[5];
        sprites[0] = Game.spritesheet.getSprite(128, 80, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(144,80, 16, 16);
        sprites[2] = Game.spritesheet.getSprite(128, 96, 16, 16);
        sprites[3] = Game.spritesheet.getSprite(144,80, 16, 16);
        sprites[4] = Game.spritesheet.getSprite(144,96, 16, 16);
        setMask(maskx,masky,maskwidht,maskheight);
    }

    public void tick() {
        depth = 0;
        if (this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 145) {
            this.seePlayer = true;
            if (seePlayer == true) {
                if (!isColliding(this, Game.player)) {
                    if (Game.rand.nextInt(100) < 60) {
                        if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(), 12, 16)
                                && !enemyIsColliding((int) (x + speed), this.getY(),maskx, masky, maskwidht, maskheight)
                                || x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(), 12, 16)
                                && !enemyIsColliding((int) (x - speed), this.getY(),maskx, masky, maskwidht, maskheight)
                                || y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed), 12, 16)
                                && !enemyIsColliding(this.getX(), (int) (y + speed),maskx, masky, maskwidht, maskheight)
                                || y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), 12, 16)
                                && !enemyIsColliding(this.getX(), (int) (y - speed),maskx, masky, maskwidht, maskheight)) {

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
                        Game.player.life -= 1;
                        Sound.Clips.hurtEffect.play();
                        Game.player.isDamaged = true;

                    }
                }
            }
        } else {
            seePlayer = false;
        }
        if (Game.rand.nextInt(100) < 70) {
            enemyFollowPath(path);
        }
        frames++;
        if (frames == max_frames) {
            frames = 0;
            index++;
            if (index > max_index) {
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
                    life-=2;
                    isDamaged = true;
                    Game.bullets.remove(i);
                    return;
                }
            }
        }
    }

    public void render(Graphics g){
        if(!isDamaged) {
            g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
        }else{
            g.drawImage(Entitie.DamagedSlime, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
        /*Visualizador de colisao
        super.render(g);
        g.fillRect(this.getX()+maskx - Camera.x, this.getY()+masky - Camera.y, maskwidht, maskheight);
    /**/
    }
}
