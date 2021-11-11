package com.arthur.world;

import com.arthur.main.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Light {
    public BufferedImage light;
    public int[] lightPixels;

    public Light(){
        try {
            light = ImageIO.read(getClass().getResource("/light.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lightPixels = new int[light.getWidth()*light.getHeight()];

        light.getRGB(0,0,light.getWidth(),light.getHeight(),lightPixels,0,light.getWidth());
    }

    public void render(){
        for(int xx = 0; xx< Game.WIDTH;xx++){
            for(int yy=0; yy<Game.HEIGHT; yy++){
                if(lightPixels[(xx+(yy*Game.WIDTH))] == 0xFFFFFFFF){
                    Game.pixels[xx+(yy*Game.WIDTH)] = 0xFF000000;
                }
            }
        }
    }
}
