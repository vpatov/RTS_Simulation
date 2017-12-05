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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Vasia
 */
public class Unit_Type {
    
    static public enum TYPE {
        TYPE_1, TYPE_2, TYPE_3;
    }

    static EnumMap<TYPE, Unit_Type> unit_types;
    
    String name;
    int gold_cost;
    int max_health;
    int damage_min;
    int damage_max;
    int armor;
    TYPE type_enum;

    




    

    
    
    public static int count_unit_type(ArrayList<Unit> units, TYPE unit_type){
        int count = 0;
        for (Unit unit : units) {
            if (unit.unit_type.type_enum == unit_type){
                count++;
            }
        }
        return count;
    }
    
    
    public static void init_unit_types(String filepath){
        File f = new File(filepath);
        unit_types = new EnumMap<>(TYPE.class);
        try {
            Scanner sc = new Scanner(f);
            int type_index = 0;
            
            while(sc.hasNext()){
                Unit_Type type = new Unit_Type();
                type.name = sc.nextLine();
                type.gold_cost = sc.nextInt();
                type.damage_min = sc.nextInt();
                type.damage_max = sc.nextInt();
                type.armor = sc.nextInt();
                type.type_enum = TYPE.values()[type_index++];
                unit_types.put(type.type_enum,type);
            }
        }
        catch (FileNotFoundException e){
            
        }
    }

}
