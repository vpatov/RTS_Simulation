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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import javax.imageio.ImageIO;
/**
 *
 * @author Vasia
 */
public class Map {
    public enum Terrain {
        CLIFF,GRASS,NON_TERRAIN,STRUCTURE;
    }
    
    
    static int MAP_WIDTH;
    static int MAP_HEIGHT;
    static Terrain[][] global_map;  //Singleton
    static Sim_Obj[][] unit_map;    //Singleton
    static Point[] bottom_starting_points;
    static Point[] top_starting_points; 
    static int structure_size = 8;
    static Structure[] bottom_structures;
    static Structure[] top_structures;
    
    static final int GRASS_COLOR = -14630848;
    static final int UNPASSABLE_COLOR = -16777216;
    static final int STRUCTURE_COLOR = -1;
    
   

    
    public static Terrain[][] load_terrain(String filepath){
        BufferedImage img;
        Color color;
        
        try {
            img = ImageIO.read(new File(filepath));
            MAP_WIDTH = img.getWidth();
            MAP_HEIGHT = img.getHeight();
            global_map = new Terrain[MAP_WIDTH][MAP_HEIGHT];
            unit_map = new Sim_Obj[MAP_WIDTH][MAP_HEIGHT];
            for (int i = 0; i < img.getWidth(); i++){
                for (int j = 0; j < img.getHeight(); j++){
                    switch(img.getRGB(i,j)){
                        case GRASS_COLOR:
                            global_map[i][j] = Terrain.GRASS; break;
                        case UNPASSABLE_COLOR:
                            global_map[i][j] = Terrain.CLIFF; break;
                        case STRUCTURE_COLOR:
                            global_map[i][j] = Terrain.STRUCTURE; break;
                        default:
                            System.out.println("Unexpected color in map: " +  new Color(img.getRGB(i,j)));
                    }

                }
            }

            
            
            
            System.out.println("Loaded map successfully from " + filepath);
        }
        catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
        return global_map;
        
    }
    
    public static void load_structures(){

        bottom_structures = new Structure[4];
        top_structures = new Structure[4];

        int bottom_structures_index = 0;
        //bottom_structures
        for (int i = 0; i < MAP_WIDTH; i++){
            for (int j = i + 1; j < MAP_HEIGHT; j++){
                if (global_map[i][j] == Terrain.STRUCTURE){

                    boolean cont = false;
                    for (Structure struct: bottom_structures){
                        if (struct != null){
                            if (Point.point_is_inside(
                                    new Point(i,j), struct.top_left, struct.bottom_right)
                                    ){
                                j += 8-1;
                                cont = true;
                            }
                        }
                    }
                    if (cont)
                        continue;

                    Structure struct = new Structure();
                    struct.top_left = new Point(i,j);
                    struct.top_right = new Point(i + structure_size-1,j);
                    struct.bottom_left = new Point(i, j+ structure_size-1);
                    struct.bottom_right = new Point(i+ structure_size-1, j + structure_size-1);
                    bottom_structures[bottom_structures_index++] = struct;
                }
            }

        }


        int top_structures_index = 0;
        //top_structures
        for (int j = 0; j < MAP_HEIGHT; j++){
            for (int i = j + 1; i < MAP_HEIGHT; i++){
                if (global_map[i][j] == Terrain.STRUCTURE){

                    boolean cont = false;
                    for (Structure struct: top_structures){
                        if (struct != null){
                            if (Point.point_is_inside(
                                    new Point(i,j), struct.top_left, struct.bottom_right)
                                    ){
                                i += 8-1;
                                cont = true;
                            }
                        }
                    }
                    if (cont)
                        continue;

                    Structure struct = new Structure();
                    struct.top_left = new Point(i,j);
                    struct.top_right = new Point(i + structure_size-1,j);
                    struct.bottom_left = new Point(i, j+ structure_size-1);
                    struct.bottom_right = new Point(i+ structure_size-1, j + structure_size-1);
                    top_structures[top_structures_index++] = struct;
                }
            }

        }
        
        

        for (Structure struct: bottom_structures){
            System.out.println(struct.top_left + "," + 
                    struct.top_right + "," + struct.bottom_left + "," + struct.bottom_right);
        }
        
        for (Structure struct: top_structures){
            System.out.println(struct.top_left + "," + 
                    struct.top_right + "," + struct.bottom_left + "," + struct.bottom_right);
        }

        // shamelessly hard-coded
        bottom_starting_points = new Point[]
            {new Point(22,149), new Point(45,160), new Point(44,183)};
        top_starting_points = new Point[]
            {new Point(149,22), new Point(160,45), new Point(183, 44)};
    }
    
    
    public static void find_empty_point(Point point, Sim_State state){
        HashSet<Point> visited = new HashSet<>();
        
        
        
    }
        

    
    
}
