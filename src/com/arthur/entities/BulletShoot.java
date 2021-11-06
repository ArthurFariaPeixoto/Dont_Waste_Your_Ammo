package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.world.Camera;

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
        x += directionX * speed;
        y += directionY * speed;
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

