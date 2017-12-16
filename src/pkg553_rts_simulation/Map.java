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
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
/**
 *
 * @author Vasia
 */
public class Map {
    public enum Terrain {
        CLIFF,GRASS,NON_TERRAIN,RED_STRUCTURE, BLUE_STRUCTURE;
    }
    
    
    static int MAP_WIDTH;
    static int MAP_HEIGHT;
    static Terrain[][] global_map = null;  //Singleton
    static Sim_Obj[][] unit_map;    //Singleton
    static Point[] red_starting_points;
    static Point[] blue_starting_points; 
    static Point blue_base = new Point(182,21);
    static Point red_base = new Point(21,182);
    static int structure_size = 8;
    static ArrayList<Structure> all_structures;
    static ArrayList<Structure> blue_structures;
    static ArrayList<Structure> red_structures;
    
  
    static final int GRASS_COLOR =          ((255 & 0xFF) << 24) | ((32 & 0xFF) << 16)  | ((192 & 0xFF) << 8)   | ((64 & 0xFF) << 0);
    static final int UNPASSABLE_COLOR =     ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16)   | ((0 & 0xFF) << 8)     | ((0 & 0xFF) << 0);
    static final int RED_STRUCTURE_COLOR =  ((255 & 0xFF) << 24) | ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8)     | ((0 & 0xFF) << 0);
    static final int BLUE_STRUCTURE_COLOR = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16)   | ((0 & 0xFF) << 8)     | ((255 & 0xFF) << 0);
    
   

    
    public static Terrain[][] load_terrain(String filepath){
        BufferedImage img;
        Color color;

        if (filepath == null){
            if (global_map != null)
                return global_map;
            else {
                System.err.println("global_map is null. You must provid a filename.");
                System.exit(1);
            }
        }
        
        try {
            /*
            IMPORTANT: global_map is stored as [rows][cols]
            Visually points are represented (row,col) -> (x,y)
            That is, x is the height, and y is the width.
            */
            
            img = ImageIO.read(new File(filepath));
            MAP_WIDTH = img.getWidth();
            MAP_HEIGHT = img.getHeight();
            global_map = new Terrain[MAP_WIDTH][MAP_HEIGHT];
            unit_map = new Sim_Obj[MAP_WIDTH][MAP_HEIGHT];
            for (int x = 0; x < img.getHeight(); x++){
                for (int y = 0; y < img.getWidth(); y++){

                    switch(img.getRGB(y, x)){
                        case GRASS_COLOR:
                            global_map[x][y] = Terrain.GRASS; break;
                        case UNPASSABLE_COLOR:
                            global_map[x][y] = Terrain.CLIFF; break;
                        case RED_STRUCTURE_COLOR:
                            global_map[x][y] = Terrain.RED_STRUCTURE; break;
                        case BLUE_STRUCTURE_COLOR:
                            global_map[x][y] = Terrain.BLUE_STRUCTURE; break;
                        default:
                            System.out.println("Unexpected color in map: " +  new Color(img.getRGB(y,x)) + "," + img.getRGB(y,x));
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
        all_structures = new ArrayList<>();
        red_structures = new ArrayList<>();
        blue_structures = new ArrayList<>();
  
        for (int x = 0; x < MAP_HEIGHT; x++){
            for (int y = 0; y < MAP_WIDTH; y++){
                Terrain terrain = global_map[x][y];
                if (terrain == Terrain.RED_STRUCTURE || terrain == Terrain.BLUE_STRUCTURE){
                    boolean cont = false;
                    for (Structure struct: all_structures){
                        if (struct != null){
                            if (Point.point_is_inside(
                                    new Point(x,y), struct.top_left, struct.bottom_right)
                                    ){
                                y += 8-1;
                                cont = true;
                            }
                        }
                    }
                    if (cont)
                        continue;
                    
                    Structure struct = new Structure(
                            new Point(x,y),new Point(x, y+ structure_size-1),
                            new Point(x + structure_size-1,y),new Point(x+ structure_size-1, y + structure_size-1),
                            terrain == Terrain.RED_STRUCTURE ? Sim_Main.red : Sim_Main.blue
                    );
                    all_structures.add(struct);
                    (terrain == Terrain.RED_STRUCTURE ? red_structures : blue_structures).add(struct);
                    System.out.println(struct);   
                }
            }
        }


        // shamelessly hard-coded
        red_starting_points = new Point[]
            {new Point(22,149), new Point(45,160), new Point(48,182)};
        blue_starting_points = new Point[]
            {new Point(149,22), new Point(160,45), new Point(182, 48)};
    }
    
        
   

    
    
}
