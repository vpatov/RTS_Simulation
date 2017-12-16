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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Vasia
 */


/*

For simplification - may be dropping vision and movement speed.

Units - either workers or soldiers. Workers collect lumber from trees, 
and gold from gold mines. Workers can also build structures. Structures take 
some amount of time to build. Soldiers are meant to fight in combat. Players 
can order a unit to move to a specific location, or to attack a specific unit. 
Every unit has the following information associated with them at any time:

Maximum Health
Current Health
Health Regeneration Rate
Movement Speed
Attack Damage Distribution
Attack Rate
Armor
Current position
Production Time Distribution
Production Resource Cost
//Vision (set of cells within certain distance)
//** If visualizing vision, cells that are in vision should be brighter, and cells on the very edge of vision should be dimmer
//Vision might be dropped.
Current state (either moving, attacking, gathering, building, idle)

*/
public class Unit extends Sim_Obj implements Cloneable{

    
    
    public enum Unit_State {
        MOVING,ATTACKING,IDLE,DEAD;
    }
    
    Unit_Type unit_type;
    Player player;
    Unit_State unit_state;
    LinkedList<Point> path;
    Point movement_target;
    Sim_Obj enemy_target;
    static int structure_target_index;

    
    
    //constructors


    

    
    public Unit(Unit_Type.TYPE type){
        unit_type = Unit_Type.unit_types.get(type);
        unit_state = Unit_State.IDLE;
        this.type = Sim_Obj.Type.UNIT;
    }
    
    @Override
    public String toString(){
        return (player.red?  "Red":"Blue") + " unit at " + this.location;
    }
    
    public static ArrayList<Unit> create_units(Unit_Type.TYPE type,Player _player){
        ArrayList<Unit> new_units = new ArrayList<>();
        Point starting_points[] = _player.red ? Sim_Main.red_starting_points : Sim_Main.blue_starting_points;
        Point point;
        int index;
        
        for (int i = 0; i < _player.structures.size(); i++){
            Unit new_unit = new Unit(type);
            new_unit.player = _player;
            
            index = Sim_Main.stch.pick_point(starting_points.length);
            point = starting_points[index];
            
            Point location = Point.find_empty_point(point);
            if (location == null){
                System.out.println("Map.find_empty_point(" + point +") has failed.");
                System.exit(0);
            }
            
            new_unit.location = location;
            new_unit.health = new_unit.unit_type.max_health;
            new_units.add(new_unit);
        }
        
        _player.gold -= Unit_Type.unit_types.get(type).gold_cost;
        return new_units;
    }
    
    

    public static int count_units_in_state(Collection<Unit> units, Unit_State unit_state){
        int count = 0;
        for (Unit unit: units){
            if (unit.unit_state == unit_state)
                count++;
        }
        return count;
    }
    
    //is it returning a copy of a new unit?
    public void update_state(){  
        ArrayList<Unit> enemies = player.enemy_force;
        ArrayList<Structure> enemy_structures = player.enemy_structures;
        ArrayList<Structure> enemy_dead_structures = player.enemy_dead_structures;
        switch (this.unit_state){
            
            //if its moving, but it discovers an enemy nearby, it should switch to attacking.
            case MOVING: {

                if (path == null){
                    send_out();
                    break;
                }

                if (!path.isEmpty()){
                    Point next = path.pop();
                    location = next;
                }
                else {
                    this.unit_state = Unit_State.IDLE;
                }
                
                Structure struct = look_for_enemy_structures();
                if (struct != null){
                    this.unit_state = Unit_State.ATTACKING;
                    this.enemy_target = struct;
                    this.path = null;
                    break;
                }
                

                Unit unit = look_for_enemies();
                if (unit != null){
                    this.unit_state = Unit_State.ATTACKING;
                    this.enemy_target = unit;
                }
                   
                break;
            }
                
                
            case ATTACKING: {
                int min_target_distance = enemy_target.type == Sim_Obj.Type.UNIT ? 12:15;
                
                if (distance(enemy_target) > min_target_distance){
                    this.unit_state = Unit_State.MOVING;
//                    System.out.println("Enemy is out of range. Starting to move");
                }
                else {
                    enemy_target.health -= this.unit_type.damage_max;
                    if (enemy_target.type == Sim_Obj.Type.STRUCTURE){
                        player.enemy.trigger_sendout = true;
                    }
//                    System.out.println((player.red?"Red":"Blue") + " unit at " + location + "just hit enemy " +(enemy_target.type == Sim_Obj.Type.UNIT ? "unit":"structure")+ " at " + enemy_target.location + " for " + unit_type.damage_max);
                    Statistics.updateDamage(!this.player.red, this.unit_type.damage_max);
                            
                    if (enemy_target.health <= 0){
                        if (enemy_target.type == Sim_Obj.Type.UNIT){
                            enemies.remove(enemy_target);
                        }
                        else{
                            Structure enemy_structure = (Structure)enemy_target;
                            enemy_structures.remove(enemy_structure);
                            enemy_dead_structures.add(enemy_structure);
//                            for (int x = enemy_structure.top_left.x; x <= enemy_structure.bottom_right.x; x++){
//                                for (int y = enemy_structure.top_left.y; y <= enemy_structure.bottom_right.y; y++){
//                                    Map.global_map[x][y] = Map.Terrain.GRASS;
//                                }
//                            }
                        }
                            
                        enemy_target = null;
                        this.unit_state = Unit_State.MOVING;
                    }
                }

                break;
            }
                
            case IDLE: {
                    Unit unit = look_for_enemies();
                    if (unit != null){
                        this.unit_state = Unit_State.ATTACKING;
                        this.enemy_target = unit;
                    }
                    
                    if (player.trigger_sendout){
                        send_out();
                    }
                break;
            }
                               
        }
   
    }
    
    public Unit look_for_enemies(){
        ArrayList<Unit> enemies = player.enemy_force;
        for (Unit unit: enemies){
            if (Point.manhattan_distance(location,unit.location) <= 6){
                return unit;
            }
        }
        return null;
    }
    
    public Structure look_for_enemy_structures(){
        ArrayList<Structure> enemy_structures = player.enemy_structures;
        for (Structure struct: enemy_structures){
            if (struct.distance_to_center(location) <= 8){
                return struct;
            }
        }
        return null;
    }
    
    /** @TODO generate starting points */
    public void send_out(){
        if (player.enemy_structures.isEmpty())
            return;
        structure_target_index = (structure_target_index + 1) % player.enemy_structures.size();
        Point target = player.enemy_structures.get(structure_target_index).location;
        path = find_cached_path_to_point(target);
        unit_state = Unit_State.MOVING;
    }
    
    public LinkedList<Point> find_cached_path_to_point(Point dest){
        HashMap<Point, LinkedList<Point>> cur = Map.paths[location.x][location.y];
        LinkedList<Point> new_path;
        if (cur != null){
            new_path = Map.paths[location.x][location.y].get(dest);
            if (new_path == null){
                new_path = find_path_to_point(dest);
                Map.paths[location.x][location.y].put(dest,new_path);
            }
            return new_path;
        }
        else {
            new_path = find_path_to_point(dest);
            Map.paths[location.x][location.y] = new HashMap<>();
            Map.paths[location.x][location.y].put(dest,new_path);
            return new_path;
        }
    }
    
    
    // find a path from dest to s
    public LinkedList<Point> find_path_to_point(Point dest){
        
        class Node implements Comparable{
            int cost;
            Point point;
            Node prev = null;
            
            public Node(Point point, Node prev, int cost){
                this.point = point;
                this.prev = prev;
                this.cost = cost;
            }
            
            @Override
            public int compareTo(Object o){
                return Double.compare(this.cost, ((Node)o).cost);
            }
            
            
            
        }
        
        Point point;
        Node node;
        double cost = 0;
        
        PriorityQueue<Node> to_visit = new PriorityQueue<>();
        LinkedList<Point> path = new LinkedList<>();
        ArrayList<Point> unit_points = new ArrayList<>();
        HashSet<Point> visited = new HashSet<>();
        
        to_visit.add(new Node(location,null,0));
        visited.add(location);
        
      
        do {
            node = to_visit.remove();

            if (node.point.equals(dest)){
                while (node.prev != null){
                    path.addFirst(node.point);
                    node = node.prev;
                }
                assert(node.point.equals(location));
                //path.addFirst(node.point); //not necessary to add the point we are currently at to the path.
                
                //System.out.println("Path length is: " + path.size());

                this.movement_target = dest;
                return path;
                
                
            }
            
            //calculate cost of including neighbors in path
            point = node.point;
            Point neighbors[] = new Point[]
                    {new Point(point.x+1,point.y), new Point(point.x,point.y+1),
                     new Point(point.x-1,point.y), new Point(point.x,point.y-1)};
            
            for (Point p: neighbors){
                if (!visited.contains(p)){
                    if (Point.check_if_passable(point)){
                        visited.add(p);
//                        to_visit.add(new Node(p,node,(int)Point.distance(p, dest) + node.cost));
                        to_visit.add(new Node(p, node, Point.manhattan_distance(dest,p) + node.cost));
                    }
                }
            }
            
        } while(!to_visit.isEmpty());
        
        System.err.println("Pathfinding couldn't find path.");
        return null;
    }
            

    
    
    

    

    
    
}
