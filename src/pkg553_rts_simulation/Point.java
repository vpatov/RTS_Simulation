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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Simulation533
 */

final public class Point{
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
    
    public static double distance(Point a, Point b){
        return Math.sqrt(Math.pow(a.x - b.x,2) + Math.pow(a.y - b.y,2));
    }
    
    public static boolean point_is_inside(Point point, Point top_left, Point bottom_right){
        return  point.x >= top_left.x       &&  point.y >= top_left.y &&
                point.x <= bottom_right.x   &&  point.y <= bottom_right.y;
                
    }
    
    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
    
    public static boolean in_top_half(Point p){
        return p.x > p.y;
    }
    
    public static boolean check_if_passable(Point point){
        return Map.global_map[point.x][point.y] == Map.Terrain.GRASS;
    }
    

     public static boolean check_if_units_here(Point point, ArrayList<Unit> units){
         for (Unit unit: units){
            if (unit.location.equals(point)){
                return true;
            }
        }
         return false;
     }
    
    public static Point find_empty_point(Point point, Sim_State state){
        HashSet<Point> visited = new HashSet<>();
        LinkedList<Point> to_visit = new LinkedList<>();
        boolean in_top_half = Point.in_top_half(point);
        
        to_visit.add(point);
        Point result;
        do {
            point = to_visit.pop();
            visited.add(point);
            
            if (!check_if_units_here(point,state.blue_force) && !check_if_units_here(point,state.red_force) && check_if_passable(point)){
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
