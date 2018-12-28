package pkg553_rts_simulation;

/*
 * Copyright (C) 2017 Simulation553
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




/*
Blue Structure (0): (19, 140)(19, 147)(26, 140)(26, 147) Center: (143, 16)
Blue Structure (0): (23, 174)(23, 181)(30, 174)(30, 181) Center: (177, 20)
Blue Structure (0): (47, 152)(47, 159)(54, 152)(54, 159) Center: (155, 44)
Blue Structure (0): (50, 178)(50, 185)(57, 178)(57, 185) Center: (181, 47)
Red Structure (0): (140, 19)(140, 26)(147, 19)(147, 26) Center: (22, 137)
Red Structure (0): (152, 47)(152, 54)(159, 47)(159, 54) Center: (50, 149)
Red Structure (0): (174, 23)(174, 30)(181, 23)(181, 30) Center: (26, 171)
Red Structure (0): (178, 50)(178, 57)(185, 50)(185, 57) Center: (53, 175)
*/

/**
 *
 * @author Simulation553
 */
public class Structure extends Sim_Obj{


    Point top_left, top_right, bottom_left, bottom_right;
    Player player;
    String player_color;
    
    public Structure(Point tl, Point tr, Point bl, Point br){
        health = 3000;
        top_left = tl;
        top_right = tr;
        bottom_left = bl;
        bottom_right = br;
        player_color = player == Sim_Main.red ? "Red" : "Blue";
        
        type = Sim_Obj.Type.STRUCTURE;

        // (0,0) is on the top_left of the map
        location = Point.midpoint(top_left, bottom_right);
    }
    
    public boolean point_is_inside(Point p){
        return Point.point_is_inside(p, top_left, bottom_right);
    }
    
    public int distance_to_center(Point p){
        return Point.manhattan_distance(p, location);
    }
    
    @Override
    public String toString(){
        String out = player_color +  " Structure (" + health + "): ";
        for (Point p: new Point[]{top_left, top_right,bottom_left,bottom_right}){
            out+= p.toString();
        }
        out += " Center: " + location;
        return out;
    }
    
}
