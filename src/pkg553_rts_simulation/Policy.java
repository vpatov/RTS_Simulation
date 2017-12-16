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
<<<<<<< HEAD
    double unit_thresholds[];
=======
    int unit_thresholds[];
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
    int max_idle_units;

    //most basic default policy
    public Policy(){
        gold = new int[Unit_Type.types.length];
        for (int i = 0; i < gold.length; i++){
            gold[i] = Unit_Type.unit_types.get(Unit_Type.types[i]).gold_cost;
        }
<<<<<<< HEAD
        unit_thresholds = new double[]{.2, .1, 1 - (.1 + .2)};
        max_idle_units = 3;
    }
    
    public Policy(int g1, int g2, int g3, double t1, double t2, double t3, int idle){
        gold = new int[]{g1,g2,g3};
        unit_thresholds = new double[]{t1,t2,t3};
=======
        unit_thresholds = new int[]{20,10,5};
        max_idle_units = 3;
    }
    
    public Policy(int g1, int g2, int g3, int t1, int t2, int t3, int idle){
        gold = new int[]{g1,g2,g3};
        unit_thresholds = new int[]{t1,t2,t3};
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
        max_idle_units = idle;
    }

    
    //Very similar to 
    static Policy[] generate_configurations(){
        Policy[] policies;
<<<<<<< HEAD
        int g1, g2, g3, idle; //Policy constructor arguments

        int lg1, lg2, lg3,  lidle; //how many different values for each of the arguments we will iterate through
        int bg1, bg2, bg3, bidle; //the base value. we iterate through multiples of the base value.
        
        int thresh_varieties = 21;
=======
        int g1, g2, g3, t1, t2, t3, idle; //Policy constructor arguments
        int lg1, lg2, lg3, lt1, lt2, lt3, lidle; //how many different values for each of the arguments we will iterate through
        int bg1, bg2, bg3, bt1, bt2, bt3, bidle; //the base value. we iterate through multiples of the base value.
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
        
        /*
            handpicked, doesn't have to be these values, but they seemed good
        */
<<<<<<< HEAD
        lg1 = 3;   lg2 = 3;    lg3 = 3; 
        lidle = 3;
        
        bg1 = 50;   bg2 = 50;   bg3 = 50;
        bidle = 4;

        policies = new Policy[lg1 * lg2 * lg3 * lidle * thresh_varieties];
=======
        lg1 = 5;   lg2 = 5;    lg3 = 5; 
        lt1 = 3;   lt2 = 3;    lt3 = 3;
        lidle = 5;
        
        bg1 = 30;   bg2 = 20;   bg3 = 1;
        bt1 = 5;    bt2 = 5;    bt3 = 5;
        bidle = 4;

        policies = new Policy[lg1 * lg2 * lg3 * lt1 * lt2 * lt3 * lidle];
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
        
        int index = 0;
        for (int i = 1; i <= lg1; i++){
            g1 = (bg1 * i);
            for (int j = 1; j <= lg2; j++){
                g2 = (bg2 * j);
                for (int k = 1; k <= lg3; k++){
                    g3 = (bg3 * k);
<<<<<<< HEAD
                    for (int l = 1; l <= lidle; l++){
                        idle = (bidle * l);
                        for(double t1 = 0; t1 <= 1; t1 += 0.2){
                            for (double t2 = 0; t2 + t1 <= 1; t2 += 0.2){
                                double t3 = (1 - t1 - t2);
                                policies[index++] = new Policy(g1,g2,g3,t1,t2,t3,idle);
=======
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
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
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
<<<<<<< HEAD
        return String.format("%d %d %d %.2f %.2f %.2f %d", gold[0], gold[1], gold[2], unit_thresholds[0], unit_thresholds[1], unit_thresholds[2], max_idle_units);
=======
        return        gold[0] + "," + gold[1] + "," + gold[2] + 
                "," + unit_thresholds[0] + "," + unit_thresholds[1] + "," + unit_thresholds[2] + 
                "," + max_idle_units;
>>>>>>> 72d583dbf70f3fcdfb92c43d526c78a04b860b72
    }

    
    
}
