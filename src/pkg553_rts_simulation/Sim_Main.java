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

import java.util.ArrayList;

public class Sim_Main{
    static Map map;
    static int MAP_WIDTH = 200;
    static int MAP_HEIGHT = 200;
    static int STATE_BUFFER_SIZE = 500;
    static Sim_State state_buffer[];
    static int ticks = 0;
    static int ticks_since_last_arrival = 0;
    static int current_gold_index = 0;
    static int gold_arrival[] = {2,8,12,2000,8,9,13,5,8}; //test values
    static int gold_values[] = {100,150,80,250,100,290,180,240,170};
    static Player red, blue;
    
    
    static public void gold_disbursal(){
        if (ticks_since_last_arrival == 0){
            red.gold += gold_values[current_gold_index];
            blue.gold += gold_values[current_gold_index];
            
            ticks_since_last_arrival = gold_arrival[current_gold_index];
            current_gold_index = (current_gold_index + 1) % gold_arrival.length;
        }
        else {
            ticks_since_last_arrival--;
        }
    }
    
    

    
    public static void policy_enactment(Player player){
        Sim_State current = state_buffer[ticks % STATE_BUFFER_SIZE];
        ArrayList<Unit> force = (player.red)?current.red_force:current.blue_force;
        ArrayList<Unit> new_units = null;
        for (int i = 0; i < player.policy.gold.length; i++){
            if (player.gold > player.policy.gold[i] &&
                Unit_Type.count_unit_type(force, Unit_Type.types[i]) < 
                    player.policy.unit_thresholds[i]
                ){
                new_units = Unit.create_units(Unit_Type.types[i], player, current);
                force.addAll(new_units);
            }
        }
        if (Unit.count_units_in_state(force, Unit.Unit_State.IDLE) >= player.policy.max_idle_units ){
            for (Unit unit: force){
                if (unit.unit_state == Unit.Unit_State.IDLE)
                    unit.send_out(player,current);
            }
        }
        
        //if no units of any type were created because of thresholds and not because of gold
        //we need to make at least some kind of unit.
         
    }
    
    
    //movement and attacking
    public static void update_state(){
        Sim_State current = state_buffer[ticks % STATE_BUFFER_SIZE];
        for (Unit unit: current.red_force){
            unit.update_state(current);
        }
        for (Unit unit: current.blue_force){
            unit.update_state(current);
        }
        state_buffer[(ticks + 1) % STATE_BUFFER_SIZE] = state_buffer[ticks % STATE_BUFFER_SIZE];
        ticks++;
    }
    
    
    public static void init_simulation(){
        Map.load_terrain("data/map_01_mirrored.bmp");
        Map.load_structures();
        Unit_Type.init_unit_types("data/unit_types.txt");
        state_buffer = new Sim_State[STATE_BUFFER_SIZE];
        state_buffer[0] = new Sim_State();
        red = new Player(true); //red on top
        blue = new Player(false); // blue on bottom
        
        
    }
    
    public static void run_simulation(){
        init_simulation();
    }
    
    
    
 
    public static void main(String []args){

        init_simulation();
        
        while (true){
            
            gold_disbursal();
            policy_enactment(red);
            policy_enactment(blue);

            System.out.println(red.gold + "," + blue.gold);
//            System.out.println(state_buffer[ticks % STATE_BUFFER_SIZE].red_force);
//            System.out.println(state_buffer[ticks % STATE_BUFFER_SIZE].blue_force);
                    
            update_state();

        }
    }
    
}
