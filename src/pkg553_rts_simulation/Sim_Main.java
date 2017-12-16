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
import java.util.Random;

public class Sim_Main{
    static Map map;
    static int MAP_WIDTH = 200;
    static int MAP_HEIGHT = 200;

    static Point[] red_starting_points;
    static Point[] blue_starting_points;
    static Policy[] policies;
    static StochasticInput[] stchs;
    
    static int ticks = 0;
    static int ticks_until_next_arrival = 0;
    static Player red, blue;
    static Statistics stats = Statistics.get_statistics();
    static Player winner = null;
    static StochasticInput stch;
    static int simul_count = 0;
    
    static public void gold_disbursal(){
        int gold;
        if (ticks_until_next_arrival == 0){
            gold = stch.get_gold();
            red.gold += gold;
            blue.gold += gold;
            stats.totalGold += gold;
            
            ticks_until_next_arrival = stch.get_arrival_time();
        }
        else {
            ticks_until_next_arrival--;
        }
    }
    
    

    
    public static void policy_enactment(Player player){
        int i;
        ArrayList<Unit> new_units = null;
        double tot_units = (double)player.force.size();
        for (int m = 0; m < player.policy.gold.length; m++){
            i = (int)(player.rule_num % (player.policy.gold.length));
            player.rule_num += 1;
            if (player.gold > player.policy.gold[i] && 
                (Unit_Type.count_unit_type(player.force, Unit_Type.types[i]) / tot_units <= 
                    player.policy.unit_thresholds[i] || tot_units == 0)
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
                    unit.send_out();
            }
        }
        
        //if no units of any type were created because of thresholds and not because of gold
        //we need to make at least some kind of unit.
         
    }
    
    
    //movement and attacking
    public static boolean update_state(){

        //System.out.println("Ticks: " + Sim_Main.ticks);
        gold_disbursal();
        policy_enactment(Sim_Main.red);
        policy_enactment(Sim_Main.blue);
        
        for (Unit unit: red.force){
            unit.update_state();
        }
        red.trigger_sendout = false;
        for (Unit unit: blue.force){
            unit.update_state();
        }
        blue.trigger_sendout = false;
        
        if (red.structures.isEmpty()){
            winner = blue;
        }
        if (blue.structures.isEmpty()){
            winner = red;
        }
        

        if (ticks % 300 == 0){
            red.trigger_sendout = true;
            blue.trigger_sendout = true;
        }

        
//        if (ticks % 20 == 0)
//            StatsSummary();
        
        ticks++;
        
//        if (ticks % 20 == 0 && ticks > 5000){
//            System.out.println("red force:" + red.force.size() + ", blue force:" + blue.force.size());
//            System.out.println("red struc:" + red.structures.size() + ", blue struc:" + blue.structures.size());
//           
//        }
        
        if (ticks >= 2000){
            return false;
        }
        return true;

        
    }
    
    public static void init_players(Policy red_policy, Policy blue_policy){
        red = new Player(true, Map.red_structures, red_policy); //red on top
        blue = new Player(false, Map.blue_structures, blue_policy); // blue on bottom
        
        red.structures = Map.hardcode_red_structs();
        blue.structures = Map.hardcode_blue_structs();
        
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
        
        
        
    }
    
    
    
    
    public static void init_simulation(){
        ticks = 0;
        ticks_until_next_arrival = 0;

        Map.load_terrain("maps/map_01_mirrored.bmp");
        Unit_Type.init_unit_types("params/unit_types.txt");
        Map.load_structures();
        
        Map.all_structures = new ArrayList<>();
        Map.all_structures.addAll(Map.hardcode_blue_structs());
        Map.all_structures.addAll(Map.hardcode_red_structs());
        red_starting_points = Map.init_starting_points(true);
        blue_starting_points = Map.init_starting_points(false);

        Map.precalculatePaths();
        

        
        
    }
    
    public static boolean run_simulation(Policy red_policy, Policy blue_policy,StochasticInput cur_stch){
        winner = null;
        stch = cur_stch;
        ticks = 0;
        ticks_until_next_arrival = 0;
        init_players(red_policy,blue_policy);
        
        while (true){
            
            if (winner != null){
                String data = String.format("%d,%d,%d,%d,%.2f,%d,%.2f,%d,%.2f,%d,%d,%.2f,%d,%.2f,%d,%.2f,%d\n", simul_count, ticks,
            		System.currentTimeMillis(), red.policy.gold[0], red.policy.unit_thresholds[0], red.policy.gold[1], 
            		red.policy.unit_thresholds[1], red.policy.gold[2], red.policy.unit_thresholds[2], red.policy.max_idle_units,
            		blue.policy.gold[0], blue.policy.unit_thresholds[0], blue.policy.gold[1], blue.policy.unit_thresholds[1], 
            		blue.policy.gold[2], blue.policy.unit_thresholds[2], blue.policy.max_idle_units);
                stats.append_to_file(winner == red ? Statistics.WIN_FILE: Statistics.LOSS_FILE, data);
                break;
            }
            gold_disbursal();
            policy_enactment(red);
            policy_enactment(blue);


            if (!update_state()){
                return true;
            }
            
        }
        return false;
        
        

    }
    

       
    
 
    public static void run_many(){
        
        int i,j,k;
        long start_time, end_time;
        
        init_simulation();
        Random r = new Random(10);
        policies = Policy.generate_configurations();
        stchs = StochasticInput.generate_configurations();
        
//        for (Policy plc: policies){
//            System.out.println(plc);
//        }
//        
//        for (StochasticInput st: stchs){
//            System.out.println(st);
//        }
        int count_draws = 0;
        boolean draw;
        System.out.println("About to perform " + policies.length * stchs.length + " simulations.");
        for (i = 0; i < policies.length; i++){
            count_draws = 0;
            System.out.println("============ Policy #" + i);
            while ((j = r.nextInt(policies.length)) == i);
            
            
            //iterate from front and back
            for (int pair: new int[]{i,policies.length - i - 1}){
                for (k = 0; k < stchs.length; k++){

                    start_time = System.currentTimeMillis();

                    //returns false if successful, true if draw
                    draw = run_simulation(policies[pair],policies[j],stchs[k]);
                    end_time = System.currentTimeMillis();
                    System.out.println("Simulation: " + simul_count + (winner == red ? "\tRed Won.": "\tBlue Won.") + 
                            "\tTicks: " + ticks + "\tElapsed time: " + ((end_time - start_time) / 1000.0));
                    //System.out.println("Red policy: " + policies[i] + "\tBlue policy: " + policies[j] + "\t Stochastic Input: " + stchs[k]);
                    simul_count++;
                    if (draw){
                        count_draws++;
                    }
                    if (count_draws > 2){
                        count_draws = 0;
                        break;
                    }
                }
            }
            
        }
        
    }
    
    public static void main(String[]args){
        run_many();
    }
    
    
    

    
    public static void StatsSummary() {
    	
    	int buildingHealthBlue = 0, buildingHealthRed = 0;
    	int enemyTerritoryUnitsBlue = 0;
    	int enemyTerritoryUnitsRed = 0;
        int unitsLostBlue = stats.unitsBuiltBlue - blue.force.size();
        int unitsLostRed = stats.unitsBuiltRed - red.force.size();

        for (Structure b : blue.structures) { buildingHealthBlue += b.health; }
        for (Structure r : red.structures) 	{ buildingHealthRed += r.health; }
        for (Unit b : blue.force) { 
                if (b.location.y > b.location.x) enemyTerritoryUnitsBlue++; 
        }
        for (Unit r : red.force) { 
                if (r.location.y < r.location.x) enemyTerritoryUnitsRed++; 
        }

//		String summary = "Player\tGold Collected\tGold Spent\tUnits Built\tUnits Lost\tBuildings Standing\tBuilding Health\tDamage "
//				+ "Dealt\tUnits in Enemy Territory";
        String summaryBlue = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d",
                        ticks,stats.totalGold, stats.totalGold - blue.gold, stats.unitsBuiltBlue, unitsLostBlue, 
                        blue.structures.size(), buildingHealthBlue, stats.damageDealtBlue, enemyTerritoryUnitsBlue);
        String summaryRed =String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d",
                        ticks,stats.totalGold, stats.totalGold - red.gold, stats.unitsBuiltRed, unitsLostRed, 
                        red.structures.size(), buildingHealthRed, stats.damageDealtRed, enemyTerritoryUnitsRed);
//		return summary + "\n" + summaryBlue + "\n" + summaryRed;
        stats.append_to_file(stats.STATS_FILE, summaryBlue + "," + summaryRed + "\n");
	}
}
