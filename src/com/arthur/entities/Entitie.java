package com.arthur.entities;

import com.arthur.main.Game;
import com.arthur.world.Camera;
import com.arthur.world.KeepPosition;
import com.arthur.world.Node;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

public class Entitie{

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    public BufferedImage sprite;
    private int maskx, masky, maskwidht, maskheight;
    protected List<Node> path;
    public int depth;

    public static BufferedImage LIFE_EN = Game.spritesheet.getSprite(96,0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(112,0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(96,16, 16, 16);
    public static BufferedImage ENEMY1_EN = Game.spritesheet.getSprite(112,16, 16, 16);
    public static BufferedImage ENEMYDAMAGE = Game.spritesheet.getSprite(16, 16, 16, 16);
    public static BufferedImage WEAPONRIGHT_EN = Game.spritesheet.getSprite(128, 0, 16, 16);
    public static BufferedImage WEAPONLEFT_EN = Game.spritesheet.getSprite(144, 0, 16, 16);
    public static BufferedImage WEAPONUP_EN = Game.spritesheet.getSprite(144, 32, 16, 16);
    public static BufferedImage WEAPONDOWN_EN = Game.spritesheet.getSprite(128, 32, 16, 16);
    public static BufferedImage WEAPONRIGHTDAMAGED = Game.spritesheet.getSprite(128, 48, 16, 16);
    public static BufferedImage WEAPONLEFTDAMAGED = Game.spritesheet.getSprite(144, 48, 16, 16);
    public static BufferedImage WEAPONUPDAMAGED = Game.spritesheet.getSprite(144, 64, 16, 16);
    public static BufferedImage WEAPONDOWNDAMAGED = Game.spritesheet.getSprite(128, 64, 16, 16);
    public static BufferedImage BOOT_EN = Game.spritesheet.getSprite(96, 32, 16, 16);
    public static BufferedImage BigAmmo = Game.spritesheet.getSprite(96, 48, 16, 16);
    public static BufferedImage SaveCard = Game.spritesheet.getSprite(112, 48, 16, 16);


    public Entitie(int x, int y, int width, int height, BufferedImage sprite){

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskx = 0;
        this.masky = 0;
        this.maskwidht = width;
        this.maskheight = height;
    }
    public void setMask(int maskx, int masky, int maskwidht, int maskheight){
        this.maskx = maskx;
        this.masky = masky;
        this.maskwidht = maskwidht;
        this.maskheight = maskheight;
    }
    public void setX(int newX){
        this.x = newX;
    }
    public void setY(int newY){
        this.y = newY;
    }
    public int getX(){
        return (int) this.x;
    }
    public int getY(){
        return (int) this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public void tick(){

    }
    public double calculateDistance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }
    public void followPath(List<Node> path){
        if(path != null){
            if(path.size()>0){
                KeepPosition target = path.get(path.size()-1).tile;

                if(x<target.x*16 && !entitieIsColliding(this.getX() + 1, this.getY())){
                    x++;
                }
                else if(x>target.x*16 && !entitieIsColliding(this.getX()-1, this.getY())){
                    x--;
                }

                if(y< target.y*16 && !entitieIsColliding(this.getX(), this.getY()+1)){
                    y++;
                }
                else if(y> target.y*16 && !entitieIsColliding(this.getX(), this.getY()-1)){
                    y--;
                }

                if(x == target.x*16 && y == target.y*16){
                    path.remove(path.size()-1);
                }
            }
        }
    }
    public static boolean isColliding(Entitie e1, Entitie e2){
        Rectangle e1Mask = new Rectangle(e1.getX()+e1.maskx, e1.getY()+e1.masky, e1.maskwidht, e1.maskheight);
        Rectangle e2Mask = new Rectangle(e2.getX()+e2.maskx, e2.getY()+e2.masky, e2.maskwidht, e2.maskheight);
        return e1Mask.intersects(e2Mask);
    }
    public boolean entitieIsColliding(int xnext, int ynext){
        Rectangle entitieCurrent = new Rectangle(xnext+maskx, ynext+masky, maskwidht, maskheight);
        for(int i = 0; i < Game.entities.size(); i++){
            Entitie e = Game.entities.get(i);
            if(e == this){
                continue;
            }
            Rectangle targetEntitie = new Rectangle(e.getX()+maskx, e.getY()+masky, maskwidht, maskheight);
            if (entitieCurrent.intersects(targetEntitie)) {
                return true;
            }

        }
        return false;
    }

    public void render(Graphics g){
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, null);
        /*Visualização de colisao
        g.setColor(Color.blue);
        g.fillRect(getX()+maskx - Camera.x, getY()+masky - Camera.y, maskwidht, maskheight);

         /**/

    }

    public static Comparator<Entitie> verifyDepth = new Comparator<Entitie>() {

        @Override
        public int compare(Entitie o1, Entitie o2) {
            if(o2.depth < o1.depth){
                return + 1;
            }
            if(o2.depth > o1.depth){
                return - 1;
            }
            return 0;
        }
    };

}
