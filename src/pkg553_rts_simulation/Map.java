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
import fastNoise.FastNoise;
import java.util.Random;
/**
 *
 * @author Vasia
 */
public class Map {
    int MAP_HEIGHT;
    int MAP_WIDTH;
    Simulation_Object[][] global_map; //Singleton
    
    public static Simulation_Object[][] procedurally_generate_map(
            int seed, int width, int height){
        Simulation_Object[][] generated_map = new Simulation_Object[width][height];
        
        FastNoise fast_noise = new FastNoise(seed); // Create a FastNoise object
        fast_noise.SetNoiseType(FastNoise.NoiseType.ValueFractal); // Set the desired noise type

        
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                double noise = fast_noise.GetNoise(i, j);
                //System.out.println(noise);
                if (noise < -0.10){
                    generated_map[i][j] = new Simulation_Object(Simulation_Object.Terrain.WATER,i,j);
                }
                else if (noise < 0.15){
                    generated_map[i][j] = new Simulation_Object(Simulation_Object.Terrain.GRASS,i,j);
                }
                else {
                    generated_map[i][j] = new Simulation_Object(Simulation_Object.Terrain.CLIFF,i,j);
                }
            }
            
        }
      
        return generated_map;
    }
}
