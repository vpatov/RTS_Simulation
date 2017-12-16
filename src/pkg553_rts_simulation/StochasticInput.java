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
import org.apache.commons.math3.distribution.PoissonDistribution;

public class StochasticInput {
    
    Random random_point;
    Random random_gold;
    PoissonDistribution random_time;
    
    
    
    int gold_ceiling;
    int time_ceiling;
    int ps, gs, ts, tm;
    
    //default seeds
    public StochasticInput(){
        this(100,100,5,5,5,20);
    }
    
    public StochasticInput(int gold_ceiling, int time_ceiling, int point_seed, int gold_seed, int time_seed, int time_mean){
        this.gold_ceiling = 100;
        this.time_ceiling = 100;
        ps = point_seed;
        gs = gold_seed;
        ts = time_seed;
        tm = time_mean;
        random_point = new Random(point_seed);
        random_gold = new Random(gold_seed);
        random_time = new PoissonDistribution(time_mean);
        random_time.reseedRandomGenerator(time_seed);
    }
    
    
    public int pick_point(int length){
        return random_point.nextInt(length);
    }
    
    public int get_gold(){
        return random_gold.nextInt(gold_ceiling);
    }
    
    public int get_arrival_time(){
        long time;
        while ((time = random_time.sample()) > time_ceiling){};
        return random_time.sample();
    }
    
    @Override
    public String toString(){
        return gold_ceiling + ", " + time_ceiling + ", " + ps + ", " + gs + ", " + ts + ", " + tm;
    }
    
    public static StochasticInput[] generate_configurations(){
        StochasticInput[] stchs;
        int gc, tc, ps, gs, ts, tm; //Stochastic Input constructor arguments
        int lgc, ltc, lps, lgs, lts, ltm; //how many different values for each of the arguments we will iterate through
        int bgc, btc, bps, bgs, bts, btm; //the base value. we iterate through multiples of the base value.
        
        /*
            handpicked, doesn't have to be these values, but they seemed good
            This means there will be lgc different gold_ceiling values, ltc different 
            time_ceiling values, lps different point_seed values, and so forth...
        */
        lgc = 3;    ltc = 3;    lps = 2; 
        lgs = 3;    lts = 3;    ltm = 2;
        
        bgc = 30;   btc = 20;   bps = 1;
        bgs = 1;    bts = 1;    btm = 10;

        stchs = new StochasticInput[lgc * ltc * lps * lgs * lts * ltm];
        
        int index = 0;
        for (int i = 1; i <= lgc; i++){
            gc = (bgc * i);
            for (int j = 1; j <= ltc; j++){
                tc = (btc * j);
                for (int k = 1; k <= lps; k++){
                    ps = (bps * k);
                    for (int l = 1; l <= lgs; l++){
                        gs = (bgs * l);
                        for (int m = 1; m <= lts;  m++){
                            ts = (bts * m);
                            for (int n = 1; n <= ltm; n++){
                                tm = (btm * n);
                                stchs[index++] = new StochasticInput(gc,tc,ps,gs,ts,tm);
                            }
                        }
                    }
                }
            }
        }
        
        return stchs;
    }
    
}
