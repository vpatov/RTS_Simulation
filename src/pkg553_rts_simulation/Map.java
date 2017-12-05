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
import java.util.Random;
/**
 *
 * @author Vasia
 */
public class Map {
    int MAP_HEIGHT;
    int MAP_WIDTH;
    Terrain[][] global_map; //Singleton
    
    public Map(int height, int width){
        MAP_HEIGHT = height;
        MAP_WIDTH = width;
        global_map = new Terrain[height][width];
    }
    
    
    public enum Terrain {
        WATER,CLIFF,GRASS,NON_TERRAIN;
    }
    
}
