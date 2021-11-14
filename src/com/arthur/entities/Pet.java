package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.world.AStar;
import com.arthur.world.Camera;
import com.arthur.world.KeepPosition;
import com.arthur.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pet extends Entitie {
    private BufferedImage[] sprites;
    private double speed = 2;
    private int frames = 0, max_frames = 11, index = 0, max_index = 4;

    public Pet(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);

        sprites = new BufferedImage[5];
        sprites[0] = Game.spritesheet.getSprite(0, 32, 16, 16);
        sprites[1] = Game.spritesheet.getSprite(16,32, 16, 16);
        sprites[2] = Game.spritesheet.getSprite(0, 48, 16, 16);
        sprites[3] = Game.spritesheet.getSprite(0, 64, 16, 16);
        sprites[4] = Game.spritesheet.getSprite(16,48, 16, 16);
    }

    public void tick() {
        depth = 0;
        if (!isColliding(this, Game.player)) {
            if (Game.rand.nextInt(100) < 99) {
                if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(), 16, 16)
                        || x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(), 16, 16)
                        || y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed), 16, 16)
                        || y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), 16, 16)) {
                    if (path == null || path.size() == 0) {
                        KeepPosition start = new KeepPosition((int) x / 16, (int) y / 16);
                        KeepPosition finish = new KeepPosition((int) Game.player.x / 16, (int) Game.player.y / 16);
                        path = AStar.findPath(Game.world, start, finish);
                    }
                }
                if (Game.rand.nextInt(100) < 90) {
                    KeepPosition start = new KeepPosition((int) x / 16, (int) y / 16);
                    KeepPosition finish = new KeepPosition((int) Game.player.x / 16, (int) Game.player.y / 16);
                    path = AStar.findPath(Game.world, start, finish);
                }
                enemyFollowPath(path);
            }
        }
        frames++;
        if(frames ==max_frames){
            frames = 0;
            index++;
            if(index == 3){
                World.generateParticles(7,(int) x+8,(int) y+16, Color.cyan);
            }
            if (index > max_index) {
                index = 0;
            }
        }
    }

    public void render(Graphics g){
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

    }
}
