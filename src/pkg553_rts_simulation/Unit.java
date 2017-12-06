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
    int health;
    Unit_State unit_state;
    LinkedList<Point> path;
    Point movement_target;
    Unit enemy_target;
    Point location;

    
    
    //constructors

    public Unit(){
        
    }
    
    public Unit(Unit_Type _unit_type){
        unit_type = _unit_type;
        unit_state = Unit_State.IDLE;
    }
    
    public Unit(Unit_Type.TYPE type){
        unit_type = Unit_Type.unit_types.get(type);
        unit_state = Unit_State.IDLE;
    }
    
    public double distance(Unit unit){
        return Point.distance(unit.location,location);
    }
    
    
    public static ArrayList<Unit> create_units(Unit_Type.TYPE type,Player _player, Sim_State state){
        ArrayList<Unit> new_units = new ArrayList<>();
        Point starting_points[] = _player.red ? Map.top_starting_points : Map.bottom_starting_points;
        
        for (Point point: starting_points){
            Unit new_unit = new Unit(type);
            new_unit.player = _player;
            
            Point location = Point.find_empty_point(point, state);
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
    public void update_state(Sim_State sim_state){  
        ArrayList<Unit> enemies = player.red ? sim_state.blue_force : sim_state.red_force;
        switch (this.unit_state){
            
            //if its moving, but it discovers an enemy nearby, it should switch to attacking.
            case MOVING: {

                if (path == null){
                    path = find_path_to_point(player.red ? Map.bottom_base : Map.top_base, sim_state);
                }

                if (!path.isEmpty()){
                    Point next = path.pop();
                    location = next;
                }
                else {
                    this.unit_state = Unit_State.IDLE;
                }

                Unit unit = look_for_enemies(sim_state);
                if (unit != null){
                    this.unit_state = Unit_State.ATTACKING;
                    this.enemy_target = unit;
                }
                   
                break;
            }
                
                
            case ATTACKING: {
                if (distance(enemy_target) > 6){
                    this.unit_state = Unit_State.MOVING;
                    System.out.println("Enemy is out of range. Starting to move");
                }
                else {
                    enemy_target.health -= this.unit_type.damage_max;
                    System.out.println("Just hit enemy at " + enemy_target.location + " for " + unit_type.damage_max);
                            
                    if (enemy_target.health <= 0){
                        enemies.remove(enemy_target);
                        enemy_target = null;
                        this.unit_state = Unit_State.MOVING;
                    }
                }

                break;
            }
                
            case IDLE: {
                    Unit unit = look_for_enemies(sim_state);
                    if (unit != null){
                        this.unit_state = Unit_State.ATTACKING;
                        this.enemy_target = unit;
                    }
                break;
            }
                               
        }
   
    }
    
    public Unit look_for_enemies(Sim_State state){
        ArrayList<Unit> enemies = player.red ? state.blue_force : state.red_force;
        for (Unit unit: enemies){
            if ((int)distance(unit) <= 6){
                return unit;
            }
        }
        return null;
    }
    
    
    public void send_out(Player player, Sim_State sim_state){
        path = find_path_to_point(player.red? Map.bottom_base: Map.top_base, sim_state);
        unit_state = Unit_State.MOVING;
    }
    
    
    // find a path from dest to s
    public LinkedList<Point> find_path_to_point(Point dest, Sim_State state){
        
        class Node implements Comparable{
            double cost;
            Point point;
            Node prev = null;
            
            public Node(Point point, Node prev, double cost){
                this.point = point;
                this.prev = prev;
                this.cost = cost;
            }
            
            @Override
            public int compareTo(Object o){
                if (this == o)
                    return 0;
                Node n = (Node)o;
                return Double.compare(this.cost, n.cost);
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
                
                System.out.println("Path length is: " + path.size());

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
                        to_visit.add(new Node(p,node,Point.distance(p, dest) + node.cost));
                    }
                }
            }
            
        } while(!to_visit.isEmpty());
        
        System.err.println("Pathfinding couldn't find path.");
        return null;
    }
            
    
    
    
    

    

    
    
}
