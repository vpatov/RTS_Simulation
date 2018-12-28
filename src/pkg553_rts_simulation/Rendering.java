package pkg553_rts_simulation;

import javafx.scene.paint.Color;


public class Rendering {
	final static int BLACK 	= colorToInt(Color.BLACK);
        final static int GREEN 	= colorToInt(Color.GREEN);
        final static int WHITE 	= colorToInt(Color.WHITE);

	final static int BLUE_1 = colorToInt(Color.rgb(135, 206, 235));
	final static int BLUE_2 = colorToInt(Color.rgb(70, 130, 180));
	final static int BLUE_3 = colorToInt(Color.rgb(0, 0, 255));
	final static int BLUE_STRUCT_ALIVE = colorToInt(Color.rgb(0, 0, 128));
        final static int BLUE_STRUCT_DEAD = colorToInt(Color.rgb(216, 242, 255));
        final static int BLUE_STRUCT_CENTER = colorToInt(Color.rgb(71, 255, 245));
        
	final static int RED_1	= colorToInt(Color.rgb(205, 92, 92));
	final static int RED_2	= colorToInt(Color.rgb(220, 20, 60));
	final static int RED_3 	= colorToInt(Color.rgb(255, 0, 0));
	final static int RED_STRUCT_ALIVE	= colorToInt(Color.rgb(139, 0, 0));
        final static int RED_STRUCT_DEAD = colorToInt(Color.rgb(255, 242, 216));
        final static int RED_STRUCT_CENTER = colorToInt(Color.rgb(255, 71, 230));
	
	
	static int[] colors;
	static int[] buffer;
	
	static int _cellSize;
	static int _height;
	static int _width;
        static int _unitSize;
	
	public Rendering(int width, int height, int cellSize, int unitSize) {
		_cellSize = cellSize;
		_height = height;
		_width = width;
                _unitSize = unitSize;
		
		initColors();
		initBuffer();
	}
	
	public static void initColors() {
            colors = new int[Map.Terrain.values().length];
            colors[Map.Terrain.CLIFF.ordinal()] 		= BLACK;
            colors[Map.Terrain.GRASS.ordinal()] 		= GREEN;
            colors[Map.Terrain.RED_STRUCTURE.ordinal()]         = RED_STRUCT_DEAD;
            colors[Map.Terrain.BLUE_STRUCTURE.ordinal()]        = BLUE_STRUCT_DEAD;
            colors[Map.Terrain.NON_TERRAIN.ordinal()]           = BLACK;
    }
	
	private static int colorToInt(Color c) {
        return
                (                      255  << 24) |
                ((int) (c.getRed()   * 255) << 16) |
                ((int) (c.getGreen() * 255) << 8)  |
                ((int) (c.getBlue()  * 255));
    }
    
    public static void initBuffer() {
    	buffer = new int[_width * _height];
    	for(int row = 0; row < _height; row+= _cellSize) {
    		for(int col = 0; col < _width; col+= _cellSize) {
    			
    			//Needed for cells bigger than 1x1 pixel
    			for(int cellX = 0; cellX < _cellSize; cellX++) {
    				for(int cellY = 0; cellY < _cellSize; cellY++) {
    					buffer[col + cellY + ((row + cellX) * _width)] = 
    							colors[Map.global_map[row / _cellSize][col / _cellSize].ordinal()];
    				}
    			}
    		}
    	}
    }

    public int[] getUpdatedDisplay() {
        initBuffer();
    	int[] map = buffer;
    	
    	//Blue Team
    	for(Structure blue : Sim_Main.blue.structures) {
    		for(int row = blue.top_left.x * _cellSize; row < (blue.bottom_left.x + 1) * _cellSize; row++) {
    			for(int col = blue.top_left.y * _cellSize; col < (blue.top_right.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = BLUE_STRUCT_ALIVE;
    			}
    		}
    	}
        
        
        for(Structure blue : Sim_Main.blue.dead_structures) {
    		for(int row = blue.top_left.x * _cellSize; row < (blue.bottom_left.x + 1) * _cellSize; row++) {
    			for(int col = blue.top_left.y * _cellSize; col < (blue.top_right.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = BLUE_STRUCT_DEAD;
    			}
    		}
    	}
    	for(Unit blue : Sim_Main.blue.force) {
    		int color;
                
                switch(blue.unit_type.type_enum){
                    case TYPE_1: color = BLUE_1; break;
                    case TYPE_2: color = BLUE_2; break;
                    case TYPE_3: color = BLUE_3; break;     
                    default: color = BLUE_1;
                }

    		
    		for(int row = blue.location.x * _cellSize; row < (blue.location.x + _unitSize) * _cellSize; row++) {
    			for(int col = blue.location.y * _cellSize; col < (blue.location.y + _unitSize) * _cellSize; col++) {
    				map[col + (row * _width)] = color;
    			}
    		}
    	}
    	
    	//Red Team
    	for(Structure red : Sim_Main.red.structures) {
    		for(int row = red.top_left.x * _cellSize; row <= (red.bottom_left.x + 1) * _cellSize; row++) {
    			for(int col = red.top_left.y * _cellSize; col <= (red.top_right.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = RED_STRUCT_ALIVE;
    			}
    		}
    	}
        for(Structure blue : Sim_Main.red.dead_structures) {
    		for(int row = blue.top_left.x * _cellSize; row < (blue.bottom_left.x + 1) * _cellSize; row++) {
    			for(int col = blue.top_left.y * _cellSize; col < (blue.top_right.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = RED_STRUCT_DEAD;
    			}
    		}
    	}
    	for(Unit red : Sim_Main.red.force) {
    		int color;
                
                switch(red.unit_type.type_enum){
                    case TYPE_1: color = RED_1; break;
                    case TYPE_2: color = RED_2; break;
                    case TYPE_3: color = RED_3; break;    
                    default: color = RED_1;
                }
                

    		
    		for(int row = red.location.x * _cellSize; row < (red.location.x + _unitSize) * _cellSize; row++) {
    			for(int col = red.location.y * _cellSize; col < (red.location.y + _unitSize) * _cellSize; col++) {
    				map[col + (row * _width)] = color;
    			}
    		} 
    	}
    	
        
        // this works the way you expectr it to, with rows increasing in row number down vertically
        // and with columns increasing in col number to the right horizontally
//        for( int row = 30 * _cellSize; row < 150 * _cellSize; row++){
//            for (int col = 70 * _cellSize; col < 90 * _cellSize; col++){
//                map[col + (row * _width)] = RED_STRUCT_CENTER;
//            }
//        }
        
        
        
//        for (Structure struct: Sim_Main.red.structures){
//            for (Point p: new Point[]{struct.top_left, struct.top_right, struct.bottom_left, struct.bottom_right, struct.location}){
////                System.out.println("Render red_structures");
////                System.out.println(struct.top_left + "," + struct.top_right + "," + struct.bottom_left + "," + struct.bottom_right + "," + struct.location);
//                for(int row = p.x * _cellSize; row < (p.x + _unitSize) * _cellSize; row++) {
//                    for(int col = p.y * _cellSize; col < (p.y + _unitSize) * _cellSize; col++) {
//                            map[col + (row * _width)] = RED_STRUCT_CENTER;
//                    }
//                } 
//            }
//        }
        

//        for (Structure struct: Sim_Main.blue.structures){
//            for (Point p: new Point[]{struct.top_left, struct.top_right, struct.bottom_left, struct.bottom_right, struct.location}){
////                System.out.println("Render blue_structures");
////                System.out.println(struct.top_left + "," + struct.top_right + "," + struct.bottom_left + "," + struct.bottom_right + "," + struct.location);
//                for(int row = p.x * _cellSize; row < (p.x + _unitSize) * _cellSize; row++) {
//                    for(int col = p.y * _cellSize; col < (p.y + _unitSize) * _cellSize; col++) {
//                            map[col + (row * _width)] = BLUE_STRUCT_CENTER;
//                    }
//                } 
//            }
//        }

    	return map;
    }
}
