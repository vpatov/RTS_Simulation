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

public class Sim_Main{
    static Map map;
    static int MAP_WIDTH = 200;
    static int MAP_HEIGHT = 200;
    static int STATE_BUFFER_SIZE = 500;
    static Sim_State state_buffer[];
    static int ticks = 0;
    static int ticks_since_last_arrival = 0;
    static int current_gold_index = 0;
    static int gold_arrival[] = {5,4,3,4,2,4,5,10,8}; //test values
    static int gold_values[] = {60,44,35,51,54,39,95,87,70};
    static Player red, blue;
    
    static public void gold_disbursal(){
        if (ticks_since_last_arrival == gold_arrival[current_gold_index]){
            red.gold += gold_values[current_gold_index];
            blue.gold += gold_values[current_gold_index];
            
            ticks_since_last_arrival = 0;
            current_gold_index = (current_gold_index + 1) % gold_arrival.length;
        }
        else {
            ticks_since_last_arrival++;
        }
    }
    
 
    public static void main(String []args){
        Map.load_terrain("data/map_01_mirrored.bmp");
        state_buffer = new Sim_State[STATE_BUFFER_SIZE];
        red = new Player(); //red on top
        blue = new Player(); // blue on bottom
        
        while (true){
            
            gold_disbursal();
            // 1) if it is time for gold disbursal according to gold interrarival.
            //      disburse gold
            // 2) For every unit
            //      If unit is attacking
            //          Update the health of the unit that it is attacking
            //      If unit is moving
            //          Update the units position
            //          Update the health regen if necessary
            //      If unit is idle
            //          Leave it be
            // Seems simple but there WILL be bugs. meny bugz.
            
            ticks++;
            break;
            
            
            
            
        }
    }
    
}