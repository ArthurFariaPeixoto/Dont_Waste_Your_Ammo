package com.arthur.graficos;

import com.arthur.main.Game;
import com.arthur.world.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class UI {
    public static BufferedImage minimap;
    public static int[] minimapPixels;

    public UI() {
        minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
        minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(255, 0, 0, 190));
        g2.fillRect(8, 4, 70, 8);
        g2.setColor(new Color(0, 255, 0, 190));
        g2.fillRect(8, 4, (int) ((Game.player.life/ Game.player.max_life)*70), 8);
        g2.setColor(Color.black);
        g2.setFont(new Font("arial", Font.BOLD, 8));
        g2.drawString((int)Game.player.life+" / "+(int)Game.player.max_life, 30,11 );

        /* Renderizando o minimapa */
        renderMinimap();
        g2.drawImage(minimap,190, 120, World.WIDTH, World.HEIGHT,null);

    }

    private void renderMinimap(){
        for(int i =0; i<minimapPixels.length; i++){
            minimapPixels[i] = 0;
        }
        for(int xx = 0; xx<World.WIDTH; xx++){
            for(int yy=0; yy<World.HEIGHT; yy++){
                if(World.tiles[xx + (yy * World.WIDTH)] instanceof WallTile){
                    minimapPixels[xx + (yy * World.WIDTH)] = 0xFF532300;
                }
                if(World.tiles[xx + (yy * World.WIDTH)] instanceof WatterWallTile){
                    minimapPixels[xx + (yy * World.WIDTH)] = 0xFF0109AB;
                }
                if(World.tiles[xx + (yy * World.WIDTH)] instanceof FloorTile){
                    minimapPixels[xx + (yy * World.WIDTH)] = 0xFF007F0E;
                }
                if(World.tiles[xx + (yy * World.WIDTH)] instanceof SandFloorTile){
                    minimapPixels[xx + (yy * World.WIDTH)] = 0xFFF6C268;
                }
                if(World.tiles[xx + (yy * World.WIDTH)] instanceof WatterFloorTile){
                    minimapPixels[xx + (yy * World.WIDTH)] = 0xFF3A35AA;
                }
            }
        }
            int xPLayer = Game.player.getX()/16;
            int yPlayer = Game.player.getY()/16;
            minimapPixels[xPLayer + (yPlayer * World.WIDTH)] = 0xFF513E7C;

        for(int i = 0; i < Game.enemies.size(); i++){
            int xEnemy = Game.enemies.get(i).getX()/16;
            int yEnemy = Game.enemies.get(i).getY()/16;
            minimapPixels[xEnemy + (yEnemy * World.WIDTH)] = 0xFF910000;
        }
    }
}
