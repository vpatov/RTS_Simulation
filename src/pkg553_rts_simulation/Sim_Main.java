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

    static int ticks = 0;
    static int ticks_since_last_arrival = 0;
    static int current_gold_index = 0;
    static int gold_arrival[] = {2,8,12,8,24,9,13,5,8}; //test values
    static int gold_values[] = {100,150,80,250,100,290,180,240,170};
    static Player red, blue;
    static Statistics stats = new Statistics();
    static Player winner = null;
    
    static public void gold_disbursal(){
        if (ticks_since_last_arrival == 0){
            red.gold += gold_values[current_gold_index];
            blue.gold += gold_values[current_gold_index];
            stats.totalGold += gold_values[current_gold_index];
            
            ticks_since_last_arrival = gold_arrival[current_gold_index];
            current_gold_index = (current_gold_index + 1) % gold_arrival.length;
        }
        else {
            ticks_since_last_arrival--;
        }
    }
    
    

    
    public static void policy_enactment(Player player){
        ArrayList<Unit> new_units = null;
        for (int i = 0; i < player.policy.gold.length; i++){
            if (player.gold > player.policy.gold[i] &&
                Unit_Type.count_unit_type(player.force, Unit_Type.types[i]) < 
                    player.policy.unit_thresholds[i]
                ){
                new_units = Unit.create_units(Unit_Type.types[i], player);
                player.force.addAll(new_units);
                
                if (player == blue) stats.unitsBuiltBlue += new_units.size();
                else stats.unitsBuiltRed += new_units.size();
            }
        }
        if (Unit.count_units_in_state(player.force, Unit.Unit_State.IDLE) >= player.policy.max_idle_units ){
            for (Unit unit: player.force){
                if (unit.unit_state == Unit.Unit_State.IDLE)
                    unit.send_out(player);
            }
        }
        
        //if no units of any type were created because of thresholds and not because of gold
        //we need to make at least some kind of unit.
         
    }
    
    
    //movement and attacking
    public static void update_state(){

        System.out.println("Ticks: " + Sim_Main.ticks);
        gold_disbursal();
        policy_enactment(Sim_Main.red);
        policy_enactment(Sim_Main.blue);
        
        for (Unit unit: red.force){
            unit.update_state();
        }
        for (Unit unit: blue.force){
            unit.update_state();
        }
        
        if (red.structures.isEmpty()){
            System.out.println("Blue won");
            winner = blue;
        }
        if (blue.structures.isEmpty()){
            System.out.println("Red won");
            winner = red;
        }

        ticks++;
    }
    
    public static void init_players(){
        red = new Player(true, Map.red_structures); //red on top
        blue = new Player(false, Map.blue_structures); // blue on bottom
        
        red.enemy = blue;
        red.enemy_force = blue.force;
        red.enemy_structures = blue.structures;
        red.enemy_dead_structures = blue.dead_structures;
        
        blue.enemy = red;
        blue.enemy_force = red.force;
        blue.enemy_structures = red.structures;
        blue.enemy_dead_structures = red.dead_structures;
        
        for (Structure struct: red.structures)
            struct.player = red;
        for (Structure struct: blue.structures)
            struct.player = blue;
        
        
        Map.init_starting_points(red);
        Map.init_starting_points(blue);
    }
    
    public static void init_simulation(){
        Map.load_terrain("maps/map_01_mirrored.bmp");
        Unit_Type.init_unit_types("params/unit_types.txt");
        Map.load_structures();

       
        init_players();

        System.out.println(red.structures.get(0).player);
        
        red.policy.max_idle_units = 24;
        blue.policy.max_idle_units = 0;
        red.policy.unit_thresholds[2] = 20;
        red.policy.unit_thresholds[0] = 0;
        red.policy.unit_thresholds[1] = 0;
        red.policy.gold[2] = Unit_Type.unit_types.get(Unit_Type.TYPE.TYPE_3).gold_cost;
        
        
    }
    

    
 
    public static void main(String []args){

//        init_simulation();
//        
//        while (true){
//            
//            gold_disbursal();
//            policy_enactment(red);
//            policy_enactment(blue);
//

//            update_state();
//        }
    }
    
    public static void StatsSummary() {
    	
    	int buildingHealthBlue = 0, buildingHealthRed = 0;
    	int enemyTerritoryUnitsBlue = 0;
    	int enemyTerritoryUnitsRed = 0;
        int unitsLostBlue = stats.unitsBuiltBlue - blue.force.size();
        int unitsLostRed = stats.unitsBuiltRed - red.force.size();

//		for (Structure b : current_state.blue_structures) { buildingHealthBlue += b.health; }
//		for (Structure r : current_state.red_structures) 	{ buildingHealthRed += r.health; }
        for (Unit b : blue.force) { 
                if (b.location.y > b.location.x) enemyTerritoryUnitsBlue++; 
        }
        for (Unit r : red.force) { 
                if (r.location.y < r.location.x) enemyTerritoryUnitsRed++; 
        }

//		String summary = "Player\tGold Collected\tGold Spent\tUnits Built\tUnits Lost\tBuildings Standing\tBuilding Health\tDamage "
//				+ "Dealt\tUnits in Enemy Territory";
        String summaryBlue = String.format("%d,%d,%d,%d,%d,%d,%d,%d",
                        stats.totalGold, stats.totalGold - blue.gold, stats.unitsBuiltBlue, unitsLostBlue, 
                        blue.structures.size(), buildingHealthBlue, stats.damageDealtBlue, enemyTerritoryUnitsBlue);
        String summaryRed =String.format("%d,%d,%d,%d,%d,%d,%d,%d",
                        stats.totalGold, stats.totalGold - red.gold, stats.unitsBuiltRed, unitsLostRed, 
                        red.structures.size(), buildingHealthRed, stats.damageDealtRed, enemyTerritoryUnitsRed);
//		return summary + "\n" + summaryBlue + "\n" + summaryRed;
        stats.appendToStatsFile(summaryBlue + "," + summaryRed + "\n");
	}
}
