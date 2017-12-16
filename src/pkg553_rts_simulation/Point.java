/*
 * Copyright (C) 2017 Simulation533
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package pkg553_rts_simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Simulation533
 */

final public class Point implements Serializable{
    final int x;
    final int y;
    
    public Point(int _x, int _y){
        x = _x;
        y = _y;
    }


    
    
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Point))
            return false;
        if (obj == this)
            return true;
        Point p = (Point)obj;
        return p.x == x && p.y == y;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
    public static double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
    }
    
    public static double distance(Point a, Point b){
        return Math.sqrt(Math.pow(a.x - b.x,2) + Math.pow(a.y - b.y,2));
    }
    
    public static double distance(Point a, int x, int y){
        return Math.sqrt(Math.pow(a.x - x,2) + Math.pow(a.y - y,2));
    }
    
    public static int manhattan_distance(Point a, Point b){
         return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    
    public static boolean point_is_inside(Point point, Point top_left, Point bottom_right){
        boolean res =
                point.x >= top_left.x       &&  point.x <= bottom_right.x &&
                point.y >= top_left.y       &&  point.y <= bottom_right.y;
        return res;
                
    }
    
    public static Point midpoint(Point a, Point b){
        int new_x, new_y;
        new_x = a.x > b.x ? ((a.x - b.x) / 2) + b.x : ((b.x - a.x) / 2) + a.x;
        new_y = a.y > b.y ? ((a.y - b.y) / 2) + b.y : ((b.y - a.y) / 2) + a.y;
        return new Point(new_x, new_y);
    }
    
    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
    
    public static boolean in_top_half(Point p){
        return p.x < p.y;
    }
    
    public static boolean check_if_passable(Point point){
        return Map.global_map[point.x][point.y] != Map.Terrain.CLIFF;
    }
    

     public static boolean check_if_units_here(Point point, ArrayList<Unit> units){
         for (Unit unit: units){
            if (unit.location.equals(point)){
                return true;
            }
        }
         return false;
     }
    
    public static Point find_empty_point(Point point){
        HashSet<Point> visited = new HashSet<>();
        LinkedList<Point> to_visit = new LinkedList<>();
        boolean in_top_half = Point.in_top_half(point);
        
        to_visit.add(point);
        Point result;
        do {
            point = to_visit.pop();
            visited.add(point);
            
            if (!check_if_units_here(point,Sim_Main.blue.force) && !check_if_units_here(point,Sim_Main.red.force) && check_if_passable(point)){
                return point;
            }
            else {
                Point neighbors[] = new Point[]
                    {new Point(point.x+1,point.y), new Point(point.x,point.y+1),
                     new Point(point.x-1,point.y), new Point(point.x,point.y-1)};

                for (Point p: neighbors){
                    if (!visited.contains(p)){
                        to_visit.add(p);
                    }
                }
            }
        } while(!to_visit.isEmpty());
        System.out.println("Fatal");
        return null;
   
    }
        
}
