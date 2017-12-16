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
public class Policy {
    


    int gold[];
    int unit_thresholds[];
    int max_idle_units;

    //most basic default policy
    public Policy(){
        gold = new int[Unit_Type.types.length];
        for (int i = 0; i < gold.length; i++){
            gold[i] = Unit_Type.unit_types.get(Unit_Type.types[i]).gold_cost;
        }
        unit_thresholds = new int[]{20,10,5};
        max_idle_units = 3;
    }
    
    public Policy(int g1, int g2, int g3, int t1, int t2, int t3, int idle){
        gold = new int[]{g1,g2,g3};
        unit_thresholds = new int[]{t1,t2,t3};
        max_idle_units = idle;
    }

    
    //Very similar to 
    static Policy[] generate_configurations(){
        Policy[] policies;
        int g1, g2, g3, t1, t2, t3, idle; //Policy constructor arguments
        int lg1, lg2, lg3, lt1, lt2, lt3, lidle; //how many different values for each of the arguments we will iterate through
        int bg1, bg2, bg3, bt1, bt2, bt3, bidle; //the base value. we iterate through multiples of the base value.
        
        /*
            handpicked, doesn't have to be these values, but they seemed good
        */
        lg1 = 3;   lg2 = 3;    lg3 = 3; 
        lt1 = 3;   lt2 = 3;    lt3 = 3;
        lidle = 5;
        
        bg1 = 60;   bg2 = 80;   bg3 = 200;
        bt1 = 5;    bt2 = 5;    bt3 = 5;
        bidle = 4;

        policies = new Policy[lg1 * lg2 * lg3 * lt1 * lt2 * lt3 * lidle];
        
        int index = 0;
        for (int i = 1; i <= lg1; i++){
            g1 = (bg1 * i);
            for (int j = 1; j <= lg2; j++){
                g2 = (bg2 * j);
                for (int k = 1; k <= lg3; k++){
                    g3 = (bg3 * k);
                    for (int l = 1; l <= lt1; l++){
                        t1 = (bt1 * l);
                        for (int m = 1; m <= lt2;  m++){
                            t2 = (bt2 * m);
                            for (int n = 1; n <= lt3; n++){
                                t3 = (bt3 * n);
                                for (int o = 1; o <= lidle; o++){
                                    idle = (bidle * o);
                                        policies[index++] = new Policy(g1,g2,g3,t1,t2,t3,idle);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return policies;
    }
    
    @Override
    public String toString(){
        return        gold[0] + "," + gold[1] + "," + gold[2] + 
                "," + unit_thresholds[0] + "," + unit_thresholds[1] + "," + unit_thresholds[2] + 
                "," + max_idle_units;
    }

    
    
}
