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
 * @author simulation533
 */


/*
Anytime any of the four conditions are met, the player takes a specific action. 
The conditions and actions are going to be of the following form:
Rule 1: if there is at least G1 gold, and at most  X1 units of type A on the field, 
    then produce a unit of type A.
Rule 2: if there is at least G2 gold, and at most X2 units of type B on the field, 
    then produce a unit of type B.
Rule 3: if there is at least G3 gold, and at most X3 units of type C on the field, 
    then produce a unit of type C.
Rule 4: if there at least X4 units in the home base, send them all out to attack.
As we design and implement the simulation, the conditions and actions may be of a 
slightly different variety. However, these rules are easy enough to represent as a
tuple of four tuples: ((G1,X1),(G2,X2),(G3,X3),(X4)). This makes optimization fairly 
straightforward, and gets rid of the need for complicated ANNs. 
The domain of likely effective policies is not too large either. 

*/

public class Player {
    int gold;
    boolean red;
    Policy policy;
    ArrayList<Point> starting_unit_points;
    
    class Policy {

        
        int gold[];
        int unit_thresholds[];
        int max_idle_units;
        
        //most basic default policy
        public Policy(){
            gold = new int[Unit_Type.types.length];
            for (int i = 0; i < gold.length; i++){
                gold[i] = Unit_Type.unit_types.get(Unit_Type.types[i]).gold_cost;
            }
            unit_thresholds = new int[]{3,2,1};
            max_idle_units = 3;
            
        }
    }
    
    public Player(boolean _red){
        gold = 0;
        policy = new Policy();
        red = _red;
    }
    
    
}
