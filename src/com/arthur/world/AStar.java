package com.arthur.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

    public static double lastTime = System.currentTimeMillis();
    private static Comparator<Node> nodeSorter = new Comparator<Node>() {

        @Override
        public int compare(Node o1, Node o2) {
            if(o2.fCost < o1.fCost){
                return + 1;
            }
            if(o2.fCost > o1.fCost){
                return - 1;
            }
            return 0;
        }
    };

    public static boolean clear(){
        if(System.currentTimeMillis() - lastTime >= 1000){
            return true;
        }
        return false;
    }

    public static List<Node> findPath(World world, KeepPosition start, KeepPosition finish){
        lastTime = System.currentTimeMillis();
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();

        Node current = new Node(start, null, 0, calculateDistance(start, finish));
        openList.add(current);
        while (openList.size() > 0){
            Collections.sort(openList, nodeSorter);
            current = openList.get(0);

            if(current.tile.same(finish)){
                //Final da lista
                List<Node> path = new ArrayList<Node>();
                while(current.parent != null){
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                return path;
            }

            openList.remove(current);
            closedList.add(current);

            for(int i=0; i<9; i++){
                if(i==4){
                    continue;
                }
                int x = current.tile.x;
                int y= current.tile.y;
                int xi = (i % 3) - 1;
                int yi = (i / 3) - 1;

                Tile tile = World.tiles[x + xi + ( (y + yi) * World.WIDTH)];

                if(tile == null){
                    continue;
                }
                if(tile instanceof WallTile){
                    continue;
                }
                if(i==0){
                    Tile test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
                    Tile test2 = World.tiles[x+xi+((y+yi+1)*World.WIDTH)];
                    if(test instanceof WallTile || test2 instanceof WallTile){
                        continue;
                    }
                }
                else if(i==2){
                    Tile test = World.tiles[x+xi-1+((y+yi)*World.WIDTH)];
                    Tile test2 = World.tiles[x+xi+((y+yi+1)*World.WIDTH)];
                    if(test instanceof WallTile || test2 instanceof WallTile){
                        continue;
                    }
                }
                else if(i==6){
                    Tile test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
                    Tile test2 = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
                    if(test instanceof WallTile || test2 instanceof WallTile){
                        continue;
                    }
                }
                else if(i==8){
                    Tile test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
                    Tile test2 = World.tiles[x+xi-1+((y+yi)*World.WIDTH)];
                    if(test instanceof WallTile || test2 instanceof WallTile){
                        continue;
                    }
                }
                KeepPosition a = new KeepPosition(x+xi, y+yi);
                double gCost = current.gCost+calculateDistance(current.tile, a);
                double hCost = calculateDistance(a, finish);
                Node node = new Node(a, current, gCost, hCost);

                if(inList(closedList, a) && gCost >= current.gCost){
                    continue;
                }
                if(!inList(openList, a)){
                    openList.add(node);
                }
                else if(gCost < current.gCost){
                    openList.remove(current);
                    openList.add(node);
                }
            }
        }
        closedList.clear();
        return null;
    }

    private static boolean inList(List<Node> list, KeepPosition vec){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).tile.same(vec)){
                return true;
            }
        }
        return false;
    }

    private static double calculateDistance(KeepPosition tile, KeepPosition goal){
        double distanceX = tile.x - goal.x;
        double distanceY = tile.y - goal.y;

        return Math.sqrt((distanceX)*(distanceX)+(distanceY)*(distanceY));
    }

}
