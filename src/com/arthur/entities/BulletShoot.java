package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.world.Camera;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entitie{

    private int directionX;
    private int directionY;
    private double speed = 3;
    private int life = 100, curLife = 0;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int directionX, int directionY) {
        super(x, y, width, height, sprite);
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public void tick(){
        if(World.isFree((int ) (x+(directionX*speed)),(int) (y+(directionY*speed)),3,3)){
        x += directionX * speed;
        y += directionY * speed;
        }else{
            World.generateParticles(6,(int) x,(int) y, Color.yellow);
            Game.bullets.remove(this);
            return;
        }
        curLife++;
        if(curLife==life){
            Game.bullets.remove(this);
            return;
        }
    }

    public void render(Graphics g){
        g.setColor(Color.yellow);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);

    }
}

