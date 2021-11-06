package com.arthur.world;

import com.arthur.entities.*;
import com.arthur.graficos.Spritesheet;
import com.arthur.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class World {

    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static int TILE_SIZE = 16;

    public World(String path){
        try{
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH =map.getWidth();
            HEIGHT =map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0,0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for(int xx = 0; xx<map.getWidth(); xx++){
                for(int yy = 0; yy<map.getHeight(); yy++){
                    int pixelAtual = pixels[xx +(yy*map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    if(pixelAtual == 0xFF000000){
                        //chão
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                    else if(pixelAtual == 0xFFFFFFFF){
                        //parede
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    }
                    else if(pixelAtual == 0xFF0026FF){
                        //player
                        Game.player.setX(xx * 16);
                        Game.player.setY(yy * 16);
                    }
                    else if(pixelAtual ==0xFFFF006E){
                        //vida
                        Life life = new Life(xx * 16, yy * 16, 16, 16, Entitie.LIFE_EN);
                        Game.entities.add(life);
                        life.setMask(2, 5, 11, 10);
                    }
                    else if(pixelAtual == 0xFFFFD800){
                        //arma
                        Weapon arma = new Weapon(xx * 16, yy * 16, 16, 16, Entitie.WEAPON_EN);
                       Game.entities.add(arma);
                       arma.setMask(2, 0, 11, 15);
                    }
                    else if(pixelAtual == 0xFF404040){
                        //flecha
                        Bullet bullet = new Bullet(xx * 16, yy * 16, 16, 16, Entitie.BULLET_EN);
                        Game.entities.add(bullet);
                        bullet.setMask(3, 3, 12, 12);
                    }
                    else if(pixelAtual == 0xFFFF0000){
                        //inimigo 1
                        Enemy en1 = new Enemy(xx * 16, yy * 16, 16, 16, Entitie.ENEMY1_EN);
                        Game.entities.add(en1);
                        Game.enemies.add(en1);
                    }
                    else if (pixelAtual == 0xFF51D0FF){
                        //bota
                        Boot boot = new Boot(xx * 16, yy * 16, 16, 16, Entitie.BOOT_EN);
                        Game.entities.add(boot);
                    }
                    else if (pixelAtual == 0xFF606060){
                        //bota
                        BigAmmo ba = new BigAmmo(xx * 16, yy * 16, 16, 16, Entitie.BigAmmo);
                        Game.entities.add(ba);


                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static boolean isFree(int xnext, int ynext){
        int x1 = xnext / TILE_SIZE;
        int y1 = ynext / TILE_SIZE;

        int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
        int y2 = ynext / TILE_SIZE;

        int x3 = xnext / TILE_SIZE;
        int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;

        int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
        int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)]instanceof WallTile) ||
                (tiles[x2 + (y2 * World.WIDTH)]instanceof WallTile) ||
                (tiles[x3 + (y3 * World.WIDTH)]instanceof WallTile) ||
                (tiles[x4 + (y4 * World.WIDTH)]instanceof WallTile));
    }
    public static void Restart(String level){

        Game.entities = new ArrayList<Entitie>();
        Game.enemies = new ArrayList<Enemy>();
        Game.spritesheet = new Spritesheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/" +level);
        return;
    }
    public void render(Graphics g){

        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart +(Game.WIDTH >> 4)+1;
        int yfinal = ystart +(Game.HEIGHT >> 4)+1;

        for(int xx = xstart; xx <= xfinal; xx++){
            for(int yy = ystart; yy <= yfinal; yy++){
                if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT){
                    continue;
                }
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
