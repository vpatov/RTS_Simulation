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

/**
 *
 * @author Vasia
 */


/*

For simplification - may be dropping vision and movement speed.

Units - either workers or soldiers. Workers collect lumber from trees, 
and gold from gold mines. Workers can also build structures. Structures take 
some amount of time to build. Soldiers are meant to fight in combat. Players 
can order a unit to move to a specific location, or to attack a specific unit. 
Every unit has the following information associated with them at any time:

Maximum Health
Current Health
Health Regeneration Rate
Movement Speed
Attack Damage Distribution
Attack Rate
Armor
Current position
Production Time Distribution
Production Resource Cost
//Vision (set of cells within certain distance)
//** If visualizing vision, cells that are in vision should be brighter, and cells on the very edge of vision should be dimmer
//Vision might be dropped.
Current state (either moving, attacking, gathering, building, idle)

*/
public class Unit extends Sim_Obj{
    
    public enum Unit_State {
        MOVING,ATTACKING,IDLE,DEAD;
    }
    
    Unit_Type unit_type;
    Player player;
    int health;
    Unit_State state;
    ArrayList<Point> intermediary_targets;
    Point movement_target;
    Point location;

    
    
    
    //constructors

    public Unit(){
        
    }
    
    public Unit(Unit_Type _unit_type){
        unit_type = _unit_type;
    }
    
    public Unit(Unit_Type.TYPE type){
        unit_type = Unit_Type.unit_types.get(type);
    }
    
    
    public static ArrayList<Unit> create_units(Unit_Type.TYPE type,Player _player, Sim_State state){
        ArrayList<Unit> new_units = new ArrayList<>();
        Point starting_points[] = _player.red ? Map.top_starting_points : Map.bottom_starting_points;
        
        for (Point point: starting_points){
            Unit new_unit = new Unit(type);
            new_unit.player = _player;
            
            Point location = Map.find_empty_point(point, state);
            if (location == null){
                System.out.println("Map.find_empty_point(" + point +") has failed.");
                System.exit(0);
            }
            new_unit.location = location;
            new_units.add(new_unit);

        }
        _player.gold -= Unit_Type.unit_types.get(type).gold_cost;
        return new_units;
    }
    
    
    //methods
    public void move(){
        
    }
    

    
    
}
