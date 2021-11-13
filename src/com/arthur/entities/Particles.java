package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Particles extends Entitie{
    public int lifeTime=10;
    public int life=0;
    public double speed = 0.5;
    public double directionX=0;
    public double directionY=0;
    public Color color;

    public Particles(int x, int y, int width, int height, BufferedImage sprite, Color color) {
        super(x, y, width, height, sprite);

        directionX = Game.rand.nextGaussian();
        directionY = Game.rand.nextGaussian();
        this.color = color;
    }

    public void tick(){
        x+=directionX*speed;
        y+=directionY*speed;

        life++;
        if(lifeTime==life){
            Game.entities.remove(this);
        }
    }

    public void render(Graphics g){
        g.setColor(color);
        g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
    }
}
