package com.arthur.main;

import java.awt.*;
import java.util.Objects;

public class Menu {
    public String[] options = {"New Game", "Load Game", "Quit"};
    public int CurrentOptions = 0;
    public int MaxOptions = options.length - 1;
    private boolean minitick = true;
    private int tempminitick = 0;
    public static boolean pause = false;

    public boolean up, down, enter;
    public void tick(){
        if(up){
            Sound.menumove.play();
            up = false;
            CurrentOptions--;
            if(CurrentOptions < 0){
                CurrentOptions = MaxOptions;
            }
        }
        if(down){
            Sound.menumove.play();
            down = false;
            CurrentOptions++;
            if(CurrentOptions > MaxOptions){
                CurrentOptions = 0;
            }
        }
        if(enter){
            enter = false;
            Sound.menu.play();
            if(options[CurrentOptions] == "New Game" || options[CurrentOptions] == "Continue"){
                Game.gameState = "normal";
                pause = false;
            }
            else if(options[CurrentOptions] == "Quit"){
                System.exit(1);
            }
        }
        tempminitick++;
        if(tempminitick == 24) {
            tempminitick = 0;
            if (minitick) {
                minitick = false;
            } else {
                minitick = true;
            }
        }

    }
    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0,0,0, 180));
        g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString("Don't Waste Your Ammo", (Game.WIDTH)/2+50, (Game.HEIGHT*Game.SCALE)/2 - 200);

        //Opções do menu
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 24));
        if(pause == false) {
            g.drawString("New Game", (Game.WIDTH) / 2 + 20, (Game.HEIGHT * Game.SCALE) / 2 - 40);
        }
        else{
            g.drawString("Continue", (Game.WIDTH) / 2 + 20, (Game.HEIGHT * Game.SCALE) / 2 - 40);
        }
        g.drawString("Load Game", (Game.WIDTH)/2+20, (Game.HEIGHT*Game.SCALE)/2+10);
        g.drawString("Quit", (Game.WIDTH)/2+20, (Game.HEIGHT*Game.SCALE)/2+60);

        if(Objects.equals(options[CurrentOptions], "New Game")) {
            if (minitick) {
                g.drawString(">", (Game.WIDTH) / 2, (Game.HEIGHT * Game.SCALE) / 2 - 40);
            }
        }
        else if(Objects.equals(options[CurrentOptions], "Load Game")) {
            if (minitick) {
                g.drawString(">", (Game.WIDTH) / 2, (Game.HEIGHT * Game.SCALE) / 2 + 10);
            }
        }
        else if(Objects.equals(options[CurrentOptions], "Quit")) {
            if (minitick) {
                g.drawString(">", (Game.WIDTH) / 2, (Game.HEIGHT * Game.SCALE) / 2 + 60);
            }
        }

    }
}
