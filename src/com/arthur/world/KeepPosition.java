package com.arthur.world;

public class KeepPosition {
    public int x, y;

    public KeepPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean same(Object object){
        KeepPosition vector = (KeepPosition) object;
        if(vector.x == this.x && vector.y == this.y){
            return true;
        }
        else{
            return false;
        }
    }
}
