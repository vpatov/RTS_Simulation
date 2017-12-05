/*
 * Copyright (C) 2017 Vasia
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

/**
 *
 * @author simulation533
 */
public class Simulation_Object {
    
//    class Point {
//        int x;
//        int y;
//        
//        public Point(int x, int y){
//            this.x = x;
//            this.y = y;
//        }
//    }
    
    private enum Type{
        UNIT,STRUCTURE;
    }
    
    int x,y;
    Type type;

//    Point coordinates;
    
    public Simulation_Object(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Simulation_Object(){}
    

    

    

}
