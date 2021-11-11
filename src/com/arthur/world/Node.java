package com.arthur.world;

public class Node {

    public KeepPosition tile;
    public Node parent;
    public double fCost, gCost, hCost;

    public Node(KeepPosition tile, Node parent, double gCost, double hCost) {
        this.tile = tile;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost+hCost;
    }
}
