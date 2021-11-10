package com.arthur.world;

import com.arthur.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Tile {
    public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0,0,16,16);
    public static BufferedImage SAND_FLOOR = Game.spritesheet.getSprite(16,144,16,16);
    public static BufferedImage WATERSAND_FLOOR = Game.spritesheet.getSprite(16,128,16,16);
    public static BufferedImage WATERSAND_FLOOR2 = Game.spritesheet.getSprite(32,128,16,16);
    public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16,0,16,16);
    public static BufferedImage STONE_WALL = Game.spritesheet.getSprite(0,144,16,16);
    public static BufferedImage WATER_WALL = Game.spritesheet.getSprite(0,128,16,16);

    private BufferedImage sprite;
    private int x, y;

    public Tile(int x, int y, BufferedImage sprite){
        this.x = x;
        this.y = y;
        this.sprite = sprite;

    }

    public void render(Graphics g){
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}
