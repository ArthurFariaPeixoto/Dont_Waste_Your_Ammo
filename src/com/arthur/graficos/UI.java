package com.arthur.graficos;

import com.arthur.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {


    public void render(Graphics g){

        g.setColor(Color.red);
        g.fillRect(8, 4, 70, 8);
        g.setColor(Color.green);
        g.fillRect(8, 4, (int) ((Game.player.life/ Game.player.max_life)*70), 8);
        g.setColor(Color.black);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString((int)Game.player.life+" / "+(int)Game.player.max_life, 30,11 );


    }
}
