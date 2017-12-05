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
public class Unit extends Simulation_Object{

    public Unit(int x, int y) {
        super(x, y);
    }


    
    public enum Unit_State {
       MOVING,ATTACKING,GATHERING,BUILDING,IDLE,DEAD
    }
    
    Unit_Type unit_type;
    boolean worker;
    int max_health;
    int health;
    int health_regen; //every health_regen seconds they regenerate 1 health.
    int damage_min;
    int damage_max;
    //int movement_speed; for simplicity everyone will have the same movement speed.
    int damage_rate;
    int armor;
    Unit_State state;
     
    

    
}
