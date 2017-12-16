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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
    static Point blue_corner = new Point(195,5);
    static Point red_corner = new Point(5,195);
    static int structure_size = 8;              //hardcoded :(
    static int spawn_size = 35;
    static ArrayList<Structure> all_structures;
    static ArrayList<Structure> blue_structures;
    static ArrayList<Structure> red_structures;
    static HashMap<Point, LinkedList<Point>>[][] paths;
    
  
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


            
            //System.out.println("Loaded map successfully from " + filepath);
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
                            new Point(x + structure_size-1,y),new Point(x+ structure_size-1, y + structure_size-1)
                    );
                    if (terrain == Terrain.RED_STRUCTURE){
                        red_structures.add(struct);
                        struct.player = Sim_Main.red;
                    }
                    else {
                        blue_structures.add(struct);
                        struct.player = Sim_Main.blue;
                    }
                     
                }
            }
        }


    }
    
        
   public static Point[] init_starting_points(boolean red){
       //find corner that is closest to your corner.
       //generate all points in that corner that aren't on a building, on terrain,
       //and are at least two cells away from a building.
       
       Point corner = red ? Map.red_corner : Map.blue_corner;
        ArrayList<Point> list_points = new ArrayList<>();
        Point[] ret_points;
        for (int x = 0; x < MAP_HEIGHT; x++){
            for (int y = 0; y < MAP_WIDTH; y++){
                if (Point.distance(corner, x,y) < spawn_size ){

                    boolean valid = true;
                    for (int _x = x-2; _x < x+2 && valid; _x++){
                        for (int _y = y-2; _y < y + 2 && valid; _y++){
                            if (Point.distance(x,y,_x,_y) > 2)
                                continue;
                            try {
                                if (global_map[_x][_y] != Terrain.GRASS)
                                    valid = false;

                            }
                            catch (Exception e){
                                valid = false;
                            }
                        }
                    }

                    if (valid)
                        list_points.add(new Point(x,y));

                }
            }
        }
        
       
        ret_points = new Point[list_points.size()];
        ret_points = list_points.toArray(ret_points);
        return ret_points;

   }

    
    public static void precalculatePaths() {
        long start_time, end_time;
        String dir = System.getProperty("user.dir") + "/paths/";
       
        File f = new File(dir + "paths");
        if(f.exists() && !f.isDirectory()) { 
            try {
                start_time = System.currentTimeMillis();
                System.out.println("Reading from existing paths file...");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dir + "paths"));
                Map.paths = (HashMap<Point, LinkedList<Point>>[][])ois.readObject();
                end_time = System.currentTimeMillis();
                System.out.println("Readng complete! Took: " + ((end_time - start_time) / 1000.0) + " seconds");
                return;
            }
            catch (Exception e){
                e.printStackTrace();
                
            }
        }
        System.out.println("Precalculating the paths...");
        start_time = System.currentTimeMillis();
       
        paths = new HashMap[MAP_WIDTH][MAP_HEIGHT];
        Unit u = new Unit(Unit_Type.TYPE.TYPE_1);
        
        for (Point p: Sim_Main.blue_starting_points){
            paths[p.x][p.y] = new HashMap<>();
            u.location = p;
            for (Structure s : Map.all_structures) {
                paths[p.x][p.y].put(s.location, u.find_path_to_point(s.location));
            }
        }
        
        for (Point p: Sim_Main.red_starting_points){
            paths[p.x][p.y] = new HashMap<>();
            u.location = p;
            for (Structure s : Map.all_structures) {
                paths[p.x][p.y].put(s.location, u.find_path_to_point(s.location));
            }
        }
        
        end_time = System.currentTimeMillis();
        System.out.println("Calculated paths! Took: " + ((end_time - start_time) / 1000.0) + " seconds");

        
        try (ObjectOutputStream oos =
            new ObjectOutputStream(new FileOutputStream(dir + "paths"))) {

            oos.writeObject(paths);
            System.out.println("Serialized paths");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
   
    static Point[] r1 = new Point[]{new Point(8,184), new Point(8,191), new Point(15,184), new Point(15,191)};
    static Point[] r2 = new Point[]{new Point(19, 140), new Point(19, 147), new Point(26, 140), new Point(26, 147)};
    static Point[] r3 = new Point[]{new Point(47, 152), new Point(47, 159), new Point(54, 152), new Point(54, 159)};
    static Point[] r4 = new Point[]{new Point(50, 178), new Point(50, 185), new Point(57, 178), new Point(57, 185)};

    static Point[] b1 = new Point[]{new Point(184,8), new Point(191,8), new Point(184,15), new Point(191,15)};
    static Point[] b2 = new Point[]{new Point(140,19), new Point(147,19), new Point(26,140), new Point(147,26)};
    static Point[] b3 = new Point[]{new Point(152,47), new Point(159,47), new Point(152,54), new Point(159,54)};
    static Point[] b4 = new Point[]{new Point(178,50), new Point(185,50), new Point(178,57), new Point(185,57)};

    public static ArrayList<Structure> hardcode_red_structs(){
        ArrayList<Structure> ret = new ArrayList<>();
       
        ret.add(new Structure(r1[0],r1[1],r1[2],r1[3]));
        ret.add(new Structure(r2[0],r2[1],r2[2],r2[3]));
        ret.add(new Structure(r3[0],r3[1],r3[2],r3[3]));
        ret.add(new Structure(r4[0],r4[1],r4[2],r4[3]));
        return ret;
       
   }
   
    public static ArrayList<Structure> hardcode_blue_structs(){
        ArrayList<Structure> ret = new ArrayList<>();

        ret.add(new Structure(b1[0],b1[1],b1[2],b1[3]));
        ret.add(new Structure(b2[0],b2[1],b2[2],b2[3]));
        ret.add(new Structure(b3[0],b3[1],b3[2],b3[3]));
        ret.add(new Structure(b4[0],b4[1],b4[2],b4[3]));
        return ret;
   }
    
}
