package com.arthur.main;

import com.arthur.world.World;

import java.awt.*;
import java.io.*;
import java.util.Objects;

public class Menu {
    public String[] options = {"New Game", "Load Game", "Quit"};
    public int CurrentOptions = 0;
    public int MaxOptions = options.length - 1;
    private boolean minitick = true;
    private int tempminitick = 0;
    public static boolean pause = false;
    public boolean up, down, enter;
    public static boolean saveExists = false;
    public static boolean saveGame = false;

    public void tick(){
        File file = new File("save.txt");
        if(file.exists()){
            saveExists = true;
        }
        else{
            saveExists = false;
        }

        if(up){
            Sound.Clips.menumove.play();
            up = false;
            CurrentOptions--;
            if(CurrentOptions < 0){
                CurrentOptions = MaxOptions;
            }
        }
        if(down){
            Sound.Clips.menumove.play();
            down = false;
            CurrentOptions++;
            if(CurrentOptions > MaxOptions){
                CurrentOptions = 0;
            }
        }
        if(enter){
            enter = false;
            Sound.Clips.menu.play();
            if(options[CurrentOptions] == "New Game" || options[CurrentOptions] == "Continue"){
                Game.gameState = "normal";
                pause = false;
                file = new File("save.txt");
                file.delete();
            }
            else if(options[CurrentOptions] == "Load Game"){
                file = new File("save.txt");
                if(file.exists()){
                    String saver = loadGame(10);
                    applySave(saver);
                }
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
        g2.fillRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString("Don't Waste Your Ammo", Toolkit.getDefaultToolkit().getScreenSize().width/3, Toolkit.getDefaultToolkit().getScreenSize().height/2 - 200);

        //Opções do menu
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 24));
        if(pause == false) {
            g.drawString("New Game", Toolkit.getDefaultToolkit().getScreenSize().width / 3 + 20, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 40);
        }
        else{
            g.drawString("Continue", Toolkit.getDefaultToolkit().getScreenSize().width / 3 + 20, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 40);
        }
        g.drawString("Load Game", Toolkit.getDefaultToolkit().getScreenSize().width/3+20, Toolkit.getDefaultToolkit().getScreenSize().height/2+10);
        g.drawString("Quit", Toolkit.getDefaultToolkit().getScreenSize().width/3+20, Toolkit.getDefaultToolkit().getScreenSize().height/2+60);

        if(Objects.equals(options[CurrentOptions], "New Game")) {
            if (minitick) {
                g.drawString(">", Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 40);
            }
        }
        else if(Objects.equals(options[CurrentOptions], "Load Game")) {
            if (minitick) {
                g.drawString(">", Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2 + 10);
            }
        }
        else if(Objects.equals(options[CurrentOptions], "Quit")) {
            if (minitick) {
                g.drawString(">", Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2 + 60);
            }
        }

    }
    public static void saveGame(String[] val, int[] val1, int cripto){
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<val.length; i++){
            String atual = val[i];
            atual+="#";
            char[] valor = Integer.toString(val1[i]).toCharArray();

            for(int j=0; j<valor.length; j++){
                valor[j]+=cripto;
                atual+=valor[j];
            }
            try {
                writer.write(atual);
                if(i<val.length-1){
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static String loadGame(int cripto){
        String line = "";
        File file = new File("save.txt");

        if(file.exists()){
            try {
                String linhaUnica = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                try {
                    while((linhaUnica = reader.readLine()) != null){
                        String[] campo = linhaUnica.split("#");
                        char[] valor = campo[1].toCharArray();
                        campo[1] = "";
                        for(int i=0;i<valor.length;i++){
                            valor[i]-=cripto;
                            campo[1]+=valor[i];
                        }
                        line+=campo[0];
                        line+="#";
                        line+=campo[1];
                        line+="/";
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return line;
    }
    public static void applySave(String string){
        String[] campos = string.split("/");
        for(int i=0; i<campos.length; i++){
            String[] campos2 = campos[i].split("#");
            switch (campos2[0]){
                case "level":
                    World.Restart("level"+campos2[1]+".png");
                    Game.gameState = "normal";
                    pause = false;
                    break;
            }
        }
    }
}
