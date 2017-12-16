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

/**
 *
 * @author Simulation533
 */

import java.util.ArrayList;
import java.util.Random;
//import org.apache.commons.math3.distribution;

public class StochasticInput {
    
    Random random_point;
    Random random_gold;
    Random random_time;
    
    

    
    int gold_ceiling;
    int time_ceiling;
    
    //default seeds
    public StochasticInput(){
        gold_ceiling = 100;
        time_ceiling = 20;
        random_point = new Random(5);
        random_gold = new Random(5);
        random_time = new Random(5);
    }
    
    
    public int pick_point(int length){
        return random_point.nextInt(length);
    }
    
    public int get_gold(){
        return random_gold.nextInt(gold_ceiling);
    }
    
    public int get_time(){
        return random_time.nextInt(time_ceiling); 
    }
    
    
}
