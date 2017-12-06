package pkg553_rts_simulation;

import javafx.scene.paint.Color;

public class Rendering {
	final static int BLACK 	= colorToInt(Color.BLACK);
	final static int BLUE_1 = colorToInt(Color.rgb(135, 206, 235));
	final static int BLUE_2 = colorToInt(Color.rgb(70, 130, 180));
	final static int BLUE_3 = colorToInt(Color.rgb(0, 0, 255));
	final static int BLUE_4 = colorToInt(Color.rgb(0, 0, 128));
	final static int GREEN 	= colorToInt(Color.GREEN);
	final static int RED_1	= colorToInt(Color.rgb(205, 92, 92));
	final static int RED_2	= colorToInt(Color.rgb(220, 20, 60));
	final static int RED_3 	= colorToInt(Color.rgb(255, 0, 0));
	final static int RED_4	= colorToInt(Color.rgb(139, 0, 0));
	final static int WHITE 	= colorToInt(Color.WHITE);
	
	static int[] colors;
	static int[] buffer;
	
	static int _cellSize;
	static int _height;
	static int _width;
	
	public Rendering(int width, int height, int cellSize) {
		_cellSize = cellSize;
		_height = height;
		_width = width;
		
		initColors();
		initBuffer();
	}
	
	public static void initColors() {
		colors = new int[Map.Terrain.values().length];
		colors[Map.Terrain.CLIFF.ordinal()] 		= BLACK;
    	colors[Map.Terrain.GRASS.ordinal()] 		= GREEN;
    	colors[Map.Terrain.STRUCTURE.ordinal()] 	= WHITE;
    	colors[Map.Terrain.NON_TERRAIN.ordinal()] 	= WHITE;
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
    	int[] map = buffer;
    	Sim_State state = Sim_Main.state_buffer[Sim_Main.ticks  % Sim_Main.STATE_BUFFER_SIZE];
    	
    	//Blue Team
    	for(Structure blue : state.blue_structures) {
    		for(int row = blue.top_left.y * _cellSize; row < (blue.bottom_left.y + 1) * _cellSize; row++) {
    			for(int col = blue.top_left.x * _cellSize; col < (blue.top_right.x + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = BLUE_4;
    			}
    		}
    	}
    	for(Unit blue : state.blue_force) {
    		int color;
    		if(blue.unit_type.type_enum == Unit_Type.TYPE.TYPE_1) color = BLUE_1;
    		else color = (blue.unit_type.type_enum == Unit_Type.TYPE.TYPE_2) ? BLUE_2 : BLUE_3;
    		
    		for(int row = blue.location.x * _cellSize; row < (blue.location.x + 1) * _cellSize; row++) {
    			for(int col = blue.location.y * _cellSize; col < (blue.location.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = color;
    			}
    		}
    	}
    	
    	//Red Team
    	for(Structure red : state.red_structures) {
    		for(int row = red.top_left.y * _cellSize; row <= (red.bottom_left.y + 1) * _cellSize; row++) {
    			for(int col = red.top_left.x * _cellSize; col <= (red.top_right.x + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = RED_4;
    			}
    		}
    	}
    	for(Unit red : state.red_force) {
    		int color;
    		if(red.unit_type.type_enum == Unit_Type.TYPE.TYPE_1) color = RED_1;
    		else color = (red.unit_type.type_enum == Unit_Type.TYPE.TYPE_2) ? RED_2 : RED_3;
    		
    		for(int row = red.location.x * _cellSize; row < (red.location.x + 1) * _cellSize; row++) {
    			for(int col = red.location.y * _cellSize; col < (red.location.y + 1) * _cellSize; col++) {
    				map[col + (row * _width)] = color;
    			}
    		} 
    	}
    	
    	return map;
    }
}
